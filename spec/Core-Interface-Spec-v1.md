# PCF-X v0.1 — Core Interface Specs

**v0.1** of the **Personal Data Vault (PDV)** and **Event Bus** minimal APIs. These are deliberately small, capability-gated, and easy to implement on any stack.


* **Status:** Draft (v0.1)
* **Scope:** Minimal, stable surfaces for local interoperability
* **Security model:** Capability headers + signatures; localhost by default
* **Schemas:** JSON Schema (2020-12)
* **Transports:** HTTP(S) for PDV; WebSocket (or gRPC) for Event Bus

---

## 1) Personal Data Vault (PDV) — HTTP API

**Base URL (local default):** `http://localhost:7777/`
**Auth & Capability headers (all write ops, most reads):**

* `X-PCFX-Component`: `<component-id>@<version>` (e.g., `com.acme.android/1.0.3`)
* `X-PCFX-Caps`: comma-sep capabilities used in this call (e.g., `events.write,blobs.write`)
* `X-PCFX-Consent`: `consent-uuid`
* `X-PCFX-Nonce`: random 16+ byte base64
* `X-PCFX-Signature`: Ed25519 over `(method|path|nonce|sha256(body))` base64

**Idempotency:** provide `Idempotency-Key` on POST/PUT to dedupe retries
**Errors:** JSON with `error`, `code`, `details` (HTTP 4xx/5xx)

### 1.1 Health

`GET /health`

```json
{ "status": "ok", "pdv_version": "0.1.0" }
```

### 1.2 Blobs (media/artifacts; immutable)

* `PUT /blobs/{hash}`  (cap: `blobs.write`)

  * Body: raw bytes; `{hash}` must match `sha256(base64url(bytes))`
  * 201 on first write; 200 if already exists
* `GET /blobs/{hash}` (cap: `blobs.read`)

  * Returns raw bytes; `Content-Type` set by `?ct=` optional query

### 1.3 Events (ingestion)

* `POST /events` (cap: `events.write`)

  * Body: `ExposureEvent` JSON
  * 201 with `{ "id": "..." }`
* `GET /events` (cap: `events.read`)

  * Filters: `since`, `until` (ISO8601), `device`, `adapter_id`, `surface`, `kind`, `limit` (≤1000), `cursor`
  * Returns:

```json
{ "items": [ /* ExposureEvent */ ], "next_cursor": "..." }
```

### 1.4 Atoms, Relations, Metrics (analysis outputs)

* `POST /atoms` (cap: `atoms.write`) → body: `KnowledgeAtom`
* `GET /atoms` (cap: `atoms.read`)

  * Filters: `since`, `until`, `entity`, `adapter_id`, `analysis_node_id`, `q` (text contains), `limit`, `cursor`
* `POST /relations` (cap: `relations.write`) → body: `Relation`
* `GET /relations` (cap: `relations.read`)

  * Filters: `src`, `dst`, `type`, `since`, `until`, `limit`, `cursor`
* `POST /metrics` (cap: `metrics.write`) → body: `Metric`
* `GET /metrics` (cap: `metrics.read`)

  * Filters: `scope`, `name`, `window`, `since`, `until`, `limit`, `cursor`

### 1.5 Example — POST /events

Request headers:

```
X-PCFX-Component: com.acme.android.accessibility/1.0.3
X-PCFX-Caps: events.write
X-PCFX-Consent: consent-7f3a
X-PCFX-Nonce: h3bdc2nE7+0nXkq1...
X-PCFX-Signature: z3Ew... (Ed25519)
Idempotency-Key: 2e1c1a1c-...
Content-Type: application/json
```

Body:

```json
{
  "schema":"pcfx.exposure_event/0.1",
  "id":"35b0a8e0-e0c3",
  "ts":"2025-10-20T14:32:11.123Z",
  "device":"android:pixel8",
  "adapter_id":"com.acme.android.accessibility/1.0.3",
  "capabilities_used":["screen.focus.read","notifications.read"],
  "source":{"surface":"app","app":"com.instagram.android","frame":"main"},
  "content":{"kind":"text","text":"Buy Brand X now!","lang":"en","blob_ref":null},
  "privacy":{"consent_id":"consent-7f3a","pii_flags":["voice"],"retention_days":14},
  "signature":"eddsa-p256:..."
}
```

Response:

```json
{ "id": "35b0a8e0-e0c3" }
```

---

## 2) Event Bus — Minimal Messaging Spec

Two reference transports are allowed; pick one (or support both):

### 2.1 WebSocket Bus (default local)

**URL:** `ws://localhost:7788/ws`
**Framing:** JSON messages, newline-delimited
**Auth handshake (first message):**

```json
{
  "type": "hello",
  "component": "com.acme.node.atomizer/0.2.1",
  "caps": ["events.read","atoms.write"],
  "consent": "consent-7f3a",
  "nonce": "base64...",
  "signature": "base64..."
}
```

**Subscribe:**

```json
{ "type":"subscribe", "topics": ["pcfx.exposures.*","pcfx.exposures.audio"] }
```

**Publish (only where allowed by caps):**

```json
{ "type":"publish", "topic":"pcfx.atoms.text", "payload": { /* KnowledgeAtom */ } }
```

**Message envelope (delivered on subscriptions):**

```json
{
  "type":"message",
  "topic":"pcfx.exposures.app",
  "payload": { /* ExposureEvent */ },
  "offset":"kinesis-like-cursor",
  "ts":"2025-10-20T14:32:12.000Z"
}
```

**Ack (optional for at-least-once):**

```json
{ "type":"ack", "offset":"kinesis-like-cursor" }
```

**Topic taxonomy (v0.1):**

* `pcfx.exposures.*` (raw events):

  * `pcfx.exposures.app`, `.browser`, `.audio`, `.tv`, `.wearable`
* `pcfx.atoms.*` (analysis outputs):

  * `pcfx.atoms.text`, `.audio`, `.image`
* `pcfx.relations.*`
* `pcfx.metrics.*` (e.g., `.daily`, `.weekly`)

### 2.2 gRPC Bus (optional, binary/typed)

* Service: `Bus.Subscribe(Subscription) -> stream<Message>`
* Service: `Bus.Publish(PublishRequest) -> PublishAck`
* Metadata headers carry component/cap/consent/signature (same semantics as HTTP)

---

## 3) JSON Schemas (draft 2020-12, minimal)

> Keep schemas small; extend via `extensions` objects. Version with `schema` field.

### 3.1 ExposureEvent

```json
{
  "$id": "https://pcfx.org/schemas/exposure_event-0.1.json",
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "type": "object",
  "required": ["schema","id","ts","device","adapter_id","capabilities_used","source","content","privacy","signature"],
  "properties": {
    "schema": { "const": "pcfx.exposure_event/0.1" },
    "id": { "type": "string" },
    "ts": { "type": "string", "format": "date-time" },
    "device": { "type": "string" },
    "adapter_id": { "type": "string" },
    "capabilities_used": { "type": "array", "items": { "type": "string" } },
    "source": {
      "type":"object",
      "required":["surface"],
      "properties":{
        "surface":{"type":"string","enum":["app","browser","audio","tv","wearable","system"]},
        "app":{"type":"string"},
        "url":{"type":"string"},
        "frame":{"type":"string"}
      },
      "additionalProperties": false
    },
    "content": {
      "type":"object",
      "required":["kind"],
      "properties":{
        "kind":{"type":"string","enum":["text","audio","image","video","ad","system"]},
        "text":{"type":"string"},
        "lang":{"type":"string"},
        "blob_ref":{"type":"string"}
      },
      "additionalProperties": false
    },
    "privacy": {
      "type":"object",
      "required":["consent_id","retention_days"],
      "properties":{
        "consent_id":{"type":"string"},
        "pii_flags":{"type":"array","items":{"type":"string"}},
        "retention_days":{"type":"integer","minimum":0}
      },
      "additionalProperties": false
    },
    "extensions": { "type": "object" },
    "signature": { "type": "string" }
  },
  "additionalProperties": false
}
```

### 3.2 KnowledgeAtom

```json
{
  "$id": "https://pcfx.org/schemas/knowledge_atom-0.1.json",
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "type":"object",
  "required":["schema","id","ts","provenance","text","tone","confidence","signature"],
  "properties":{
    "schema":{"const":"pcfx.knowledge_atom/0.1"},
    "id":{"type":"string"},
    "ts":{"type":"string","format":"date-time"},
    "provenance":{
      "type":"object",
      "required":["event_id","adapter_id","analysis_node_id"],
      "properties":{
        "event_id":{"type":"string"},
        "adapter_id":{"type":"string"},
        "analysis_node_id":{"type":"string"}
      },
      "additionalProperties":false
    },
    "text":{"type":"string"},
    "entities":{"type":"array","items":{"type":"object","properties":{
      "id":{"type":"string"},"type":{"type":"string"},"name":{"type":"string"}
    },"additionalProperties":false}},
    "tone":{"type":"object"}, 
    "vector_ref":{"type":"string"},
    "confidence":{"type":"object"},
    "blob_refs":{"type":"array","items":{"type":"string"}},
    "extensions":{"type":"object"},
    "signature":{"type":"string"}
  },
  "additionalProperties": false
}
```

### 3.3 Relation

```json
{
  "$id":"https://pcfx.org/schemas/relation-0.1.json",
  "$schema":"https://json-schema.org/draft/2020-12/schema",
  "type":"object",
  "required":["schema","id","ts","src","dst","type","analysis_node_id","signature"],
  "properties":{
    "schema":{"const":"pcfx.relation/0.1"},
    "id":{"type":"string"},
    "ts":{"type":"string","format":"date-time"},
    "src":{"type":"string"},
    "dst":{"type":"string"},
    "type":{"type":"string","enum":["supports","refutes","near_duplicate","same_template","co_timed","quotes","mentions"]},
    "weight":{"type":"number","minimum":0,"maximum":1},
    "analysis_node_id":{"type":"string"},
    "extensions":{"type":"object"},
    "signature":{"type":"string"}
  },
  "additionalProperties": false
}
```

### 3.4 Metric

```json
{
  "$id":"https://pcfx.org/schemas/metric-0.1.json",
  "$schema":"https://json-schema.org/draft/2020-12/schema",
  "type":"object",
  "required":["schema","id","ts","scope","name","value","window","signature"],
  "properties":{
    "schema":{"const":"pcfx.metric/0.1"},
    "id":{"type":"string"},
    "ts":{"type":"string","format":"date-time"},
    "scope":{"type":"string"}, 
    "name":{"type":"string"},
    "value":{"type":"number"},
    "window":{"type":"string"}, 
    "explain":{"type":"object"},
    "extensions":{"type":"object"},
    "signature":{"type":"string"}
  },
  "additionalProperties": false
}
```

### 3.5 ConsentManifest

```json
{
  "$id":"https://pcfx.org/schemas/consent-0.1.json",
  "$schema":"https://json-schema.org/draft/2020-12/schema",
  "type":"object",
  "required":["schema","consent_id","subject","adapter_id","grants","created_at","expires_at","signature"],
  "properties":{
    "schema":{"const":"pcfx.consent/0.1"},
    "consent_id":{"type":"string"},
    "subject":{"type":"string"},
    "adapter_id":{"type":"string"},
    "grants":{"type":"array","items":{"type":"object","required":["cap","purpose","retention_days"],"properties":{
      "cap":{"type":"string"},
      "purpose":{"type":"string"},
      "retention_days":{"type":"integer","minimum":0}
    },"additionalProperties":false}},
    "created_at":{"type":"string","format":"date-time"},
    "expires_at":{"type":"string","format":"date-time"},
    "extensions":{"type":"object"},
    "signature":{"type":"string"}
  },
  "additionalProperties": false
}
```

---

## 4) Capabilities (v0.1 registry)

* **Ingestion:** `events.write`, `blobs.write`
* **Read raw:** `events.read`, `blobs.read`
* **Analysis write:** `atoms.write`, `relations.write`, `metrics.write`
* **Analysis read:** `atoms.read`, `relations.read`, `metrics.read`
* **Network (optional):** `net.out` (blocked by default)
* **Admin (dev only):** `pdv.admin`

*PDV must verify that the capabilities presented are granted by the referenced `X-PCFX-Consent`.*

---

## 5) Error Model

```json
{
  "error":"capability_denied",
  "code":403,
  "details":"missing capability events.write"
}
```

Common codes:

* `400 invalid_schema`
* `401 signature_invalid`
* `403 capability_denied`
* `404 not_found`
* `409 idempotency_conflict`
* `413 payload_too_large`
* `429 rate_limited`
* `500 internal_error`

---

## 6) Pagination & Cursors

* `GET` list endpoints accept `limit` (≤1000) and return `next_cursor`.
* Clients pass `cursor` to resume.
* Time filters (`since`, `until`) narrow scans.

---

## 7) Minimal AsyncAPI (WebSocket) Snippet

```yaml
asyncapi: '2.6.0'
info:
  title: PCF-X Event Bus
  version: 0.1.0
servers:
  local:
    url: ws://localhost:7788/ws
    protocol: ws
channels:
  pcfx.exposures.app:
    subscribe:
      message:
        $ref: '#/components/messages/ExposureEventMsg'
  pcfx.atoms.text:
    publish:
      message:
        $ref: '#/components/messages/KnowledgeAtomMsg'
components:
  messages:
    ExposureEventMsg:
      name: ExposureEvent
      payload:
        $ref: 'https://pcfx.org/schemas/exposure_event-0.1.json'
    KnowledgeAtomMsg:
      name: KnowledgeAtom
      payload:
        $ref: 'https://pcfx.org/schemas/knowledge_atom-0.1.json'
```

---

## 8) Security Notes

* **Local-first:** bind PDV and Bus to `localhost` by default.
* **mTLS (optional):** if remote PDV is enabled, support component certs + ACLs.
* **Signatures:** Ed25519 recommended; include `nonce` to defeat replay.
* **Immutability:** artifacts are append-only; updates create new ids referencing prior.
* **Retention:** PDV enforces `retention_days` for raw blobs; atoms/metrics policy is configurable.

---

## 9) Ready-to-build Checklist

* [ ] Implement **/health, /events, /blobs, /atoms, /relations, /metrics** with schema validation.
* [ ] Enforce capability gates using `X-PCFX-*` headers + ConsentManifest store.
* [ ] Provide **WebSocket Bus** with `hello / subscribe / publish / ack`.
* [ ] Ship example fixtures for each artifact and golden tests.
* [ ] Document supported capabilities and topic names at runtime (`GET /capabilities`, `GET /topics` optional).

---