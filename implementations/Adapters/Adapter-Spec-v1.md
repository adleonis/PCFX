# PCF-X Adapter Specification (v0.1)

## 0) Scope & intent

An **Adapter** is a sandboxed capture component that:

* **observes** a human-facing surface (apps, browser pages, audio, TV captions, wearables, OS notifications, etc.),
* **normalizes** observations into **`ExposureEvent`** records,
* **optionally persists** raw artifacts (audio/image/video/text) into the PDV blob store,
* **publishes** to the local Event Bus and/or writes to the **Personal Data Vault (PDV)**.

Adapters **DO NOT** analyze or infer narratives; they **only observe and attest**. Analysis is the job of Nodes.

---

## 1) Roles & trust model

* **Adapters**: lowest-privilege observers; write `ExposureEvent`s and blobs.
* **Nodes**: transform events into `KnowledgeAtom`, `Relation`, `Metric`.
* **Clients**: render insights (read-only).
* **PDV**: system-of-record; enforces consent & retention; verifies signatures.

**Zero-trust**: Adapters cannot read user data by default; they only write what they capture with explicit user consent.

---

## 2) Required capabilities (Adapter)

Adapters MUST declare capabilities in an **Adapter Manifest**, e.g.:

* Capture:

  * `screen.focus.read` (foreground app/window titles)
  * `browser.urls.read` / `browser.page.meta.read`
  * `notifications.read`
  * `microphone.capture`
  * `captions.read` (TV/streaming subtitles)
  * `wearable.stream.read`
* Storage:

  * `pdv.write.events`
  * `pdv.write.blobs`
* Bus:

  * `bus.pub.exposures.*`
* Networking:

  * `net.out` (**DENIED by default**; only for update checks, never to exfiltrate captured content)

Each capability must be covered by a **ConsentManifest** (user-signed, time-limited).

---

## 3) Core artifacts & contracts

### 3.1 ExposureEvent (egress; REQUIRED)

* **Purpose**: atomic record of “the user was exposed to X at time T on surface S”.
* **Schema id**: `pcfx.exposure_event/0.1`
* **Immutability**: append-only. Any correction is a new event with `prior_id`.

**Minimum fields (normative):**

```json
{
  "schema": "pcfx.exposure_event/0.1",
  "id": "uuid",
  "ts": "2025-10-20T14:32:11.123Z",
  "device": "android:pixel8",
  "adapter_id": "com.acme.android.accessibility/1.0.3",
  "capabilities_used": ["screen.focus.read","notifications.read"],
  "source": {
    "surface": "app|browser|audio|tv|wearable|system",
    "app": "com.instagram.android",
    "url": "https://example.com/post/123",
    "frame": "main|iframe|adslot|caption"
  },
  "content": {
    "kind": "text|audio|image|video|ad|system",
    "text": "Buy Brand X now...",      // optional if blob_ref provided
    "lang": "en",
    "blob_ref": "sha256:... (optional)"
  },
  "privacy": {
    "consent_id": "consent-uuid",
    "pii_flags": ["voice","face"],     // from controlled vocab
    "retention_days": 14
  },
  "signature": "eddsa-p256:..."
}
```

**Notes**

* If `content.text` contains PII or large payloads, prefer storing the raw artifact in PDV **blob store** and set `blob_ref`.
* **NEVER** include secrets/credentials (tokens, cookies, emails) in `content.text`.

### 3.2 Blob (optional; egress)

* Raw audio/image/video/text tied to an `ExposureEvent`.
* **Key**: `sha256:<content-hash>`; **Write**: `POST /blobs` → returns hash.
* **Linkage**: the `ExposureEvent.content.blob_ref` MUST reference this hash.
* **Retention**: governed by the event’s `privacy.retention_days`.

---

## 4) Adapter Manifest & identity

Every Adapter ships a signed **Adapter Manifest** (registered with PDV):

```json
{
  "schema": "pcfx.adapter_manifest/0.1",
  "adapter_id": "com.acme.android.accessibility/1.0.3",
  "name": "Acme Android Accessibility Adapter",
  "maintainer": "Acme Labs",
  "repo": "https://example.org/pcfx/adapters/android",
  "hash": "sha256:…binary-or-wasm…",
  "capabilities": ["screen.focus.read","notifications.read","pdv.write.events"],
  "surfaces": ["app","system"],
  "requires_net": false,
  "signature": "eddsa-p256:…"
}
```

PDV refuses unsigned or mutated binaries unless in explicitly enabled **dev mode**.

---

## 5) Consent & user control

### 5.1 ConsentManifest (normative)

* Specifies **who** may capture **what**, **why**, and **for how long**.
* Includes optional **do-not-listen zones** (geofences, BT devices) and **auto-pause** conditions.

```json
{
  "schema": "pcfx.consent/0.1",
  "consent_id": "uuid",
  "subject": "user:self",
  "adapter_id": "com.acme.android.accessibility/1.0.3",
  "grants": [
    {"cap": "screen.focus.read", "purpose":"exposure_audit","retention_days":30},
    {"cap": "microphone.capture", "purpose":"asr_transcription","retention_days":7,
     "auto_pause_zones": ["office_wifi","car_bt"]}
  ],
  "created_at": "2025-10-20T13:55:10Z",
  "expires_at": "2026-10-20T00:00:00Z",
  "signature": "..."
}
```

### 5.2 Live UX requirements (informative but recommended)

* Persistent indicator when audio/video capture is active.
* One-tap pause/resume.
* Per-surface toggles and retention sliders.

---

## 6) Runtime, lifecycle & scheduling

* **Runtime**: WASM+WASI (preferred) or native (Android/iOS/browser extensions).
* **Startup**: register manifest → request consent scopes → obtain PDV write tokens.
* **Steady state**: capture → normalize → (optional) blob write → publish event (bus) → persist to PDV.
* **Backpressure**: if PDV/bus throttles, the Adapter MUST **buffer** (bounded) and **shed** oldest buffered events if still backpressured, logging telemetry.

---

## 7) PDV & Bus APIs (Adapter-facing)

**Events**

* `POST /events` → `{id}`

  * Body: `ExposureEvent` (signed by adapter key)
* Optional: `WS pub` to `pcfx.exposures.<surface>`

**Blobs**

* `POST /blobs` (binary) → `{hash}`

  * Enforced by consent; size caps configurable (e.g., 25MB).

**Auth/Keys**

* `POST /auth/register` → returns adapter public key registration / ephemeral token.

**Quotas**

* `429` + `Retry-After` on pressure; adapters MUST backoff with jitter.

---

## 8) Normalization rules (REQUIRED)

1. **Time**: ISO-8601 UTC with ms precision (`ts`).
2. **Device**: `"platform:model"` or vendor string (`android:pixel8`, `macos:mbp14`).
3. **Surface**: controlled vocab: `app`, `browser`, `audio`, `tv`, `wearable`, `system`.
4. **Language**: BCP-47 tags (`en`, `pt-BR`).
5. **URL**: include only normalized, privacy-safe URLs (strip query params with PII).
6. **App names**: package IDs where possible (`com.instagram.android`).
7. **PII flags**: from taxonomy: `voice`, `face`, `contact`, `location`, `biometric`, `financial`, `health`.
8. **Retention**: set `privacy.retention_days` based on consent grant (NOT hardcoded).

---

## 9) Privacy, redaction & retention

* **Redaction before write**: remove contact names, emails, phone numbers from `content.text` unless the consent explicitly allows; prefer blobs with TTLs.
* **TTL enforcement**: set `retention_days`; PDV will purge blobs on expiry.
* **Zone auto-pause**: adapters must poll PDV policy; if in a restricted zone, **do not capture**.
* **No duplication**: do not write raw payloads into both `content.text` and blob; choose one.

---

## 10) Security & attestation

* Event signing: `signature` is computed over the canonical JSON (e.g., JCS) with adapter’s private key; PDV verifies with registered public key.
* Manifest hashing: PDV stores the binary/WASM hash; on connect, mismatch → reject.
* Least privilege: adapters SHOULD request the smallest capability set.

---

## 11) Performance & resource guidance

* **Batching**: POST events in batches up to 64 where platform allows.
* **Memory**: audio ring buffer with VAD; avoid retaining more than N minutes.
* **CPU**: adapters DO NOT run heavy ASR/ML; they capture only. (ASR is Node’s job unless the platform mandates on-device transcription APIs.)
* **Battery** (mobile): schedule bursts when charging/idle; minimise wakeups; honor Doze.

---

## 12) Error handling

* **Retryable**: on 5xx/429, exponential backoff (base 500ms, max 60s, jitter).
* **Non-retryable**: on 4xx (consent denied, schema invalid), drop event, log telemetry.
* **Blob too large**: split (for text) or reject (media) with reason.

---

## 13) Health & telemetry (local only)

Adapters MAY publish local, non-PII health stats:

```
adapter.events_emitted_total
adapter.events_dropped_total
adapter.backpressure_ms
adapter.battery_guard_trips
adapter.zone_autopause_trips
```

Exposed via PDV `GET /telemetry?adapter_id=…`.

---

## 14) Reference Adapter profiles

* **Android Accessibility Adapter**

  * Caps: `screen.focus.read`, `notifications.read`, `pdv.write.events`
  * Emits: `ExposureEvent` with `surface="app"` / `system"`
  * Optional: `microphone.capture` + `pdv.write.blobs` (if consented)

* **Browser Extension Adapter**

  * Caps: `browser.urls.read`, `browser.page.meta.read`, `pdv.write.events`
  * Emits: `ExposureEvent` with `surface="browser"`, `content.text` (title/og:description), optional `frame="adslot"`

* **Audio / TV Caption Adapter**

  * Caps: `captions.read`, `pdv.write.events`, `pdv.write.blobs`
  * Emits: `ExposureEvent` with `surface="tv"` or `"audio"`, plus `blob_ref` to caption/audio segment

---

## 15) Compliance checklist (for adapter implementers)

* [ ] Manifest signed; capabilities minimal; `requires_net` false unless justified
* [ ] Consent flow implemented; retention and zones respected
* [ ] `ExposureEvent` conforms to schema; timestamps normalized
* [ ] PII redaction applied; blobs used for raw payloads with TTL
* [ ] Backpressure & retries implemented; bounded buffers
* [ ] No analysis logic embedded; no outbound data exfiltration
* [ ] Signature over canonical JSON; public key registered with PDV

---

## 16) Pseudocode examples

### 16.1 Android (foreground app + notification titles)

```kotlin
onAppChanged(pkg, title, ts) {
  val evt = ExposureEvent(
    id = uuid(),
    ts = ts.toISOString(),
    device = deviceId(),
    adapter_id = "com.pcfx.android/0.1.0",
    capabilities_used = listOf("screen.focus.read"),
    source = Source(surface="app", app=pkg, frame="main"),
    content = Content(kind="text", text=title, lang=detectLang(title)),
    privacy = Privacy(consent_id=currentConsent(),
                      pii_flags=listOf(),
                      retention_days=30)
  )
  pdv.post("/events", sign(evt))
}
```

### 16.2 Browser Extension (URL + meta)

```javascript
function onNavigate(url, title, description) {
  const evt = {
    schema: "pcfx.exposure_event/0.1",
    id: uuidv4(),
    ts: new Date().toISOString(),
    device: `browser:${navigator.userAgentData?.brand||'unknown'}`,
    adapter_id: "org.pcfx.browser/0.1.0",
    capabilities_used: ["browser.urls.read","browser.page.meta.read"],
    source: { surface: "browser", url, frame: "main" },
    content: { kind: "text", text: `${title} ${description||""}`.trim(), lang: "en" },
    privacy: { consent_id: currentConsent(), pii_flags: [], retention_days: 30 }
  };
  pdvPost("/events", sign(evt));
}
```

### 16.3 Audio fragment (blob + event)

```python
buf = mic.read(seconds=5)
if vad(buf):
    h = pdv.post_blob(buf)            # returns sha256:...
    evt = {
      "schema": "pcfx.exposure_event/0.1",
      "id": uuid4(),
      "ts": nowZ(),
      "device": device_id(),
      "adapter_id": "org.pcfx.audio/0.1.0",
      "capabilities_used": ["microphone.capture","pdv.write.blobs"],
      "source": {"surface":"audio"},
      "content": {"kind":"audio","blob_ref": h},
      "privacy": {"consent_id": consent(), "pii_flags":["voice"], "retention_days": 7}
    }
    pdv.post("/events", sign(evt))
```

---

## 17) Schemas (to add alongside)

* `pcfx.adapter_manifest/0.1`
* `pcfx.consent/0.1` (already sketched in earlier docs)
* (Existing) `pcfx.exposure_event/0.1`
* (Optional) `pcfx.telemetry/0.1` (for health counters)

---

## 18) Why this spec matters (for Nodes & Clients)

* **Nodes** get a uniform stream of `ExposureEvent`s with stable `source.surface`, `pii_flags`, and retention semantics — enabling deterministic pipelines.
* **Clients** can trust that any PDV populated by a compliant Adapter is interpretable and auditable, regardless of OS/device vendor.

---