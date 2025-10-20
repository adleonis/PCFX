# PCF-X Node Specification (v0.1)

## 0) Scope & intent

A **Node** is a sandboxed processing component that:

* **ingests** standardized events (primarily `ExposureEvent`) from the local Event Bus / PDV,
* **emits** standardized artifacts (`KnowledgeAtom`, `Relation`, `Metric`) to the PDV (and optionally to bus topics),
* obeys **capability-based access**, **consent**, and **privacy** rules.

Nodes are strictly **non-interactive** with users; they serve **Clients** by persisting outputs the Clients can read.

---

## 1) Roles & trust model

* **Adapters** → publish `ExposureEvent`s (and store blobs) with consent.
* **Nodes** → read events/blobs, transform, write `KnowledgeAtom`/`Relation`/`Metric`.
* **Clients** → read atoms/relations/metrics, never raw events unless granted.
* **PDV** (Personal Data Vault) → the only writable store of record. Nodes must not persist outside PDV.

**Zero-trust**: Adapters, Nodes, Clients are mutually untrusted; PDV enforces consent/capabilities.

---

## 2) Required capabilities (Node)

A Node MUST declare these capabilities in its **Node Manifest**:

* `pdv.read.events` (scoped by event kinds/topics)
* `pdv.read.blobs` (optional; for audio/image/video)
* `pdv.write.atoms`
* `pdv.write.relations` (optional)
* `pdv.write.metrics` (optional)
* `bus.sub.exposures.*` (optional; if consuming via bus)
* `bus.pub.*` (optional; if re-emitting summaries)
* `net.out` (DENIED by default; explicit approval required)

Consent must include the allowed **surfaces** (e.g., `audio`, `browser`) and **retention constraints**.

---

## 3) Artifacts & Contracts

### 3.1 Ingress (Node inputs)

* **Primary:** `ExposureEvent` (immutable)

  * Source: PDV (`GET /events`) and/or Event Bus (`pcfx.exposures.*`)
  * Schema: `pcfx.exposure_event/0.1` (see repo)

* **Secondary:** `Blob` (audio/image/video/text)

  * Source: PDV Blob API `GET /blobs/{hash}`
  * Must respect `retention_days`, `pii_flags`

### 3.2 Egress (Node outputs)

* **KnowledgeAtom** (required for Atomizer-class nodes)

  * Schema: `pcfx.knowledge_atom/0.1`
* **Relation** (template/duplicate/contradiction/…)

  * Schema: `pcfx.relation/0.1`
* **Metric** (rolling analytics, e.g., velocity, burstiness)

  * Schema: `pcfx.metric/0.1`

All egress payloads MUST include cryptographic signature and provenance.

---

## 4) Node manifest & identity

Every Node ships a signed **Component Manifest**:

```json
{
  "schema": "pcfx.node_manifest/0.1",
  "node_id": "pcfx.atomizer/0.2.1",
  "name": "Atomizer Node",
  "maintainer": "PCF-X Foundation",
  "repo": "https://example.org/pcfx/atomizer",
  "hash": "sha256:…binary-or-wasm-module…",
  "capabilities": ["pdv.read.events","pdv.read.blobs","pdv.write.atoms"],
  "inputs": ["pcfx.exposure_event/0.1"],
  "outputs": ["pcfx.knowledge_atom/0.1"],
  "requires_net": false,
  "signature": "eddsa-p256:…"
}
```

The PDV **Component Registry** records manifests and refuses unsigned/unregistered Nodes (dev mode can relax).

---

## 5) Runtime, lifecycle & scheduling

* **Runtime:** WASM+WASI (preferred). Alternative: OCI container.
* **Startup:** Node registers with PDV bus broker; submits its `SubscriptionSpec`.
* **Steady state:** Pulls from PDV (poll or cursor) or subscribes to bus; processes; writes outputs.
* **Backpressure:** If bus signals `SLOW` or PDV rate limit is hit, Node MUST buffer/slowdown.
* **Shutdown:** Flush pending writes; checkpoint cursor.

---

## 6) Subscriptions & cursors

### 6.1 SubscriptionSpec (from Node → Event Bus/PDV)

```json
{
  "schema": "pcfx.subscription/0.1",
  "node_id": "pcfx.atomizer/0.2.1",
  "streams": [
    {
      "topic": "pcfx.exposures.audio",
      "filters": {
        "surface": ["audio","browser"],
        "time_start": "2025-10-20T00:00:00Z",
        "pii_flags_exclude": ["financial","health"]
      },
      "cursor": "opaque-cursor-token"
    }
  ],
  "max_batch": 128,
  "max_lag_ms": 2000
}
```

### 6.2 Delivery semantics

* **At-least-once** delivery from PDV/bus.
* Nodes MUST be **idempotent**:

  * Use `event.id` as idempotency key for processing.
  * Use deterministic output IDs where possible: `uuid5(namespace, event.id + stage)` or `content hash`.

---

## 7) PDV APIs (Node-facing)

**Events**

* `GET /events?topic=pcfx.exposures.audio&cursor=X&limit=N`

  * Returns `[ExposureEvent]`, `next_cursor`
* `GET /events/{id}`

**Blobs**

* `GET /blobs/{hash}` → byte stream

  * Denied if consent/PII policy restricts.

**Write**

* `POST /atoms` → `{id, ts, …}`
* `POST /relations`
* `POST /metrics`

**Quotas**

* PDV can return `429` + `Retry-After`; Node must respect.

---

## 8) Required fields (egress)

### 8.1 KnowledgeAtom (minimum)

```json
{
  "schema": "pcfx.knowledge_atom/0.1",
  "id": "uuid",
  "ts": "2025-10-20T14:32:20Z",
  "provenance": {
    "event_id": "uuid",
    "adapter_id": "com.acme.android.accessibility/1.0.3",
    "analysis_node_id": "pcfx.atomizer/0.2.1"
  },
  "text": "Brand X increases status.",
  "entities": [{"id":"BrandX","type":"org"}],
  "tone": {"aspiration":0.84},
  "vector_ref": "qdrant://atoms/id:uuid",
  "confidence": {"extraction":0.92},
  "blob_refs": ["sha256:..."],
  "signature": "eddsa-p256:..."
}
```

### 8.2 Relation

```json
{
  "schema": "pcfx.relation/0.1",
  "id": "uuid",
  "ts": "2025-10-20T14:33:10Z",
  "src": "atom:uuid-a",
  "dst": "atom:uuid-b",
  "type": "same_template",
  "weight": 0.73,
  "analysis_node_id": "pcfx.templater/0.1.0",
  "signature": "eddsa-p256:..."
}
```

### 8.3 Metric

```json
{
  "schema": "pcfx.metric/0.1",
  "id": "uuid",
  "ts": "2025-10-20",
  "scope": "actor:BrandX",
  "name": "exposure_velocity",
  "value": 2.31,
  "window": "P1D",
  "explain": {"contributors": ["atom:…","atom:…"]},
  "signature": "eddsa-p256:..."
}
```

---

## 9) Privacy, consent & redaction

* Node MUST check PDV policy for each event/blobs:

  * **Consent check** on `privacy.consent_id`.
  * Respect `retention_days` (do not read expired blobs).
  * Respect **PII flags**: if `voice` is disallowed for the Node, reject audio blob access.
* **Redaction:** Nodes SHOULD redact/avoid copying PII into atoms. Prefer references (`blob_refs`) to duplication.
* **TTL:** Raw artifacts may be purged by PDV; Nodes must tolerate missing blobs and degrade gracefully.

---

## 10) Security & attestation

* All Node output MUST be **signed** with Node key (issued at install/registration).
* PDV stores signature and checks on read/write.
* Node binary/WASM SHOULD be measured (hash) and recorded in Component Registry; mismatch → deny run (unless dev mode).

---

## 11) Performance & resource guidance

* **Batching:** up to 128 events per pull recommended; keep payloads < 2MB each.
* **Latency targets:** atomizer < 2s/event (audio excluded); heavy ASR runs on charging/idle windows.
* **Memory:** WASM memory cap (configurable, default 512MB).
* **CPU:** Nodes should yield (cooperative scheduling) and honor PDV backpressure.

---

## 12) Error handling

* **Retryable (5xx/429):** exponential backoff with jitter (base 500ms, max 60s).
* **Non-retryable (4xx):** log + skip; include reason in Node telemetry.
* **Partial writes:** treat as failed; retry with idempotency.
* **Corrupt blob:** emit `Metric(name="ingest_error",scope="node",value=1)` and continue.

---

## 13) Versioning & compatibility

* Each Node declares supported input/output schemas (e.g., `pcfx.exposure_event/0.1`).
* Minor schema upgrades MUST be handled without break (ignore unknown fields).
* Breaking changes bump **major** and require Node upgrade.
* PDV exposes **schema registry** with deprecation timelines.

---

## 14) Telemetry (local only)

Nodes MAY publish **local** operational metrics (no PII):

* `node.events_processed`
* `node.backpressure_ms`
* `node.errors_total`
* `node.cpu_pct`, `node.mem_mb`

Exposed via PDV `GET /telemetry?node_id=…` for local dashboards.

---

## 15) Reference Node classes (profiles)

* **Atomizer Node**
  Inputs: `ExposureEvent`, Blobs → Outputs: `KnowledgeAtom`
  MUST implement: NER, tone, (optional) ASR, embeddings.

* **Coordination Node**
  Inputs: `KnowledgeAtom` → Outputs: `Relation(same_template|near_duplicate|co_timed)`
  MUST implement: MinHash/LSH + time-window grouping.

* **Metrics Node**
  Inputs: Atoms/Relations → Outputs: `Metric(exposure_velocity, burstiness, influence_index)`
  MUST implement: sliding windows, EMA, z-scores.

---

## 16) Example processing sequences

### 16.1 Atomizer (text) — ASCII sequence

```
Adapter → PDV.events     : POST ExposureEvent(id=E1,text="Buy Brand X")
Node(Atomizer) → PDV     : GET /events?cursor=C
PDV → Node               : [E1]
Node → PDV.blobs         : (none)
Node(Atomizer)           : NER,tone,embed → Atom A1
Node → PDV.atoms         : POST KnowledgeAtom(id=A1, provenance.event_id=E1)
Client(Dashboard) → PDV  : GET /atoms?date=2025-10-20
```

### 16.2 Coordination (templates) — ASCII sequence

```
Node(Coord) → PDV        : GET /atoms?since=T0
Node(Coord)              : MinHash buckets → pairs (A1,A7),(A1,A9)
Node → PDV.relations     : POST Relation(type="same_template",src=A1,dst=A7)
Client → PDV             : GET /relations?type=same_template
```

---

## 17) Compliance checklist (for implementers)

* [ ] Manifest signed; capabilities minimal; `requires_net` justified.
* [ ] Subscriptions declare surfaces and PII excludes.
* [ ] Idempotent processing; deterministic IDs or idempotency keys.
* [ ] Outputs signed; provenance set (`event_id`, `adapter_id`, `analysis_node_id`).
* [ ] Redaction policy applied; no raw PII duplicated.
* [ ] Backpressure honored; retries exponential.
* [ ] Schema versions declared; unknown fields ignored.

---

## 18) Minimal pseudo-code (Atomizer)

```python
while True:
    events, next_cursor = pdv.get_events(topic="pcfx.exposures.*", cursor=cursor, limit=64)
    for e in dedup(events):
        try:
            text = e["content"].get("text") or asr_if_audio(e)
            ents = ner(text); tone = tone_cls(text); vec = embed(text)
            atom = build_atom(e, text, ents, tone, vec)
            pdv.post_atom(atom)  # signed
        except ConsentError:
            log.warn("consent denied", e["id"])
        except RateLimit:
            sleep(backoff())
    cursor = next_cursor
```

---

### How Adapters & Clients benefit (why this spec matters)

* **Adapters** only need to emit **one** artifact (`ExposureEvent`) with clear privacy fields; all downstream processing is standardized.
* **Clients** can assume **consistent outputs** (`KnowledgeAtom`, `Relation`, `Metric`) across vendors—making dashboards and alerts portable.

---
