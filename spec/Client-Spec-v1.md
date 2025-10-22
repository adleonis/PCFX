# PCF-X Client Specification (v0.1)

## 0) Scope & Intent

A **Client** is a **read-only**, user-facing application that:

* **retrieves** structured data (`KnowledgeAtom`, `Relation`, `Metric`, `ExposureEvent`) from the PDV;
* **renders** analytics, visualizations, or reports for the individual;
* **never modifies** raw or derived records, nor directly accesses PII-restricted blobs.

Clients are the **interface layer of cognitive transparency**, translating the analytical outputs of Nodes into understandable insights.

---

## 1) Roles & Trust Model

| Component    | Privilege   | Typical Actions                                   |
| ------------ | ----------- | ------------------------------------------------- |
| **Adapters** | write       | Capture exposures → POST `ExposureEvent`          |
| **Nodes**    | read+write  | Transform → `KnowledgeAtom`, `Relation`, `Metric` |
| **Clients**  | read-only   | Visualize → dashboards, alerts, reports           |
| **PDV**      | enforcement | ACL, consent, schema, signature verification      |

* Clients operate **in zero-trust** toward other components.
* The PDV enforces **read scopes** and **capabilities** at query time.

---

## 2) Required Capabilities (Client)

A Client Manifest MUST declare:

* `pdv.read.atoms`
* `pdv.read.relations`
* `pdv.read.metrics`
* `pdv.read.events` (optional; rarely granted)
* `pdv.read.telemetry` (optional; for local dashboards)
* `bus.sub.metrics.*` (optional; realtime streams)
* `ui.display` (implicit runtime capability)
* `net.out` (only for updates or federated analytics, never to export private data)

Each scope must appear in a user-signed **ConsentManifest**.

---

## 3) Core Artifacts (Readable)

| Artifact          | Purpose                                                     | Access Rules                                      |
| ----------------- | ----------------------------------------------------------- | ------------------------------------------------- |
| **KnowledgeAtom** | Semantic units (claims, tone, provenance)                   | Always accessible; PDV redacts PII                |
| **Relation**      | Links (support, contradiction, duplicates, co-timed)        | Read-only                                         |
| **Metric**        | Aggregates (exposure velocity, tone share, influence index) | Read-only                                         |
| **ExposureEvent** | Raw exposures                                               | Access **discouraged**; requires explicit consent |
| **Telemetry**     | Adapter/Node stats                                          | Safe for dashboards                               |

---

## 4) Client Manifest & Identity

```json
{
  "schema": "pcfx.client_manifest/0.1",
  "client_id": "org.pcfx.dashboard/1.0.0",
  "name": "PCF-X Dashboard Client",
  "maintainer": "PCF-X Foundation",
  "repo": "https://example.org/pcfx/clients/dashboard",
  "capabilities": ["pdv.read.atoms","pdv.read.relations","pdv.read.metrics"],
  "requires_net": false,
  "signature": "eddsa-p256:…"
}
```

The PDV registers each Client Manifest and denies unregistered binaries (unless dev mode).

---

## 5) Privacy & Consent

* **Read Scopes:** the PDV returns only records matching the client’s granted scopes.
* **Redaction:** sensitive text/entities marked by PII flags are automatically replaced with tokens (e.g., `[REDACTED:voice]`).
* **No Write Paths:** Clients cannot mutate, delete, or export PDV content unless the user explicitly exports via PDV UI.
* **Ephemeral Cache:** Clients may locally cache derived visuals but must purge on PDV TTL expiry.

---

## 6) Runtime & Interfaces

### 6.1 PDV Query API (Client-Facing)

| Method                                         | Example                | Description                           |
| ---------------------------------------------- | ---------------------- | ------------------------------------- |
| `GET /atoms?since=...&entity=BrandX`           | List atoms             | Supports filter by entity, tone, time |
| `GET /relations?type=same_template`            | List relations         | For narrative repetition maps         |
| `GET /metrics?name=influence_index&period=P1D` | Get metrics            | Returns latest analytic values        |
| `GET /events/{id}`                             | Retrieve raw event     | Requires special consent              |
| `GET /telemetry?scope=node`                    | Retrieve runtime stats | Optional, non-PII                     |

Responses are JSON arrays or streams (`text/event-stream`).

### 6.2 Event Bus Subscriptions (Optional)

* Clients may subscribe to `pcfx.metrics.*` or `pcfx.atoms.*` for live updates.
* Delivery = at-most-once (no replay); PDV stores persistent state.

### 6.3 Sandboxing

Preferred runtime: **WebAssembly** (for plugins) or browser PWA sandbox.
All outbound networking disabled unless capability `net.out` granted.

---

## 7) Health Check Registration

Every Client SHOULD register itself with the PDV by sending periodic health check heartbeats. While not strictly required (Clients are read-only), health checks enable the PDV to:

* Track which Clients are active and being used
* Collect usage statistics and metrics
* Maintain an accurate registry of installed user-facing applications
* Monitor user engagement with privacy-enhancing tools

### 7.1 Health Check Implementation

**Endpoint:** `GET /health`

**Required Headers:**

```
X-App-ID: <unique-uuid>           # Unique app identifier (stable per build)
X-App-Type: client                  # Must be "client"
X-App-Name: <display-name>          # e.g., "PCFx-Dashboard"
X-App-Version: <version-string>     # e.g., "1.0.0"
X-Platform-Info: <platform-details> # e.g., "Web/Chrome 120", "iOS 17"
```

**Implementation Notes:**

* The `X-App-ID` SHOULD be generated during the build phase (e.g., UUID) and persist as a constant in the compiled app or stored locally.
* The `X-App-ID` MAY be viewable to users in the app's settings or about page for transparency.
* Send health checks:
  * On app initialization (if network available)
  * Periodically (every 10-60 minutes) during normal operation
  * When connectivity is (re)established (if the Client was previously offline)
* Health checks are lightweight and non-blocking; failures should be logged but not disrupt the UI.
* Unlike Adapters and Nodes, Client health checks are optional and should degrade gracefully if PDV is unavailable.

**Pseudocode Example (JavaScript/React):**

```javascript
// Define unique app ID at build time
export const ClientBuildConfig = {
    APP_NAME: "PCFx-Dashboard",
    APP_TYPE: "client",
    APP_VERSION: "1.0.0",
    UNIQUE_APP_ID: localStorage.getItem('app_id') ||
                   (() => {
                       const id = crypto.randomUUID();
                       localStorage.setItem('app_id', id);
                       return id;
                   })()
};

// Send health check
async function sendHealthCheck(pdvUrl) {
    try {
        const platformInfo = `Web/${navigator.userAgent.split(' ').pop()}`;

        const response = await fetch(`${pdvUrl}/health`, {
            method: 'GET',
            headers: {
                'X-App-ID': ClientBuildConfig.UNIQUE_APP_ID,
                'X-App-Type': ClientBuildConfig.APP_TYPE,
                'X-App-Name': ClientBuildConfig.APP_NAME,
                'X-App-Version': ClientBuildConfig.APP_VERSION,
                'X-Platform-Info': platformInfo
            }
        });

        if (response.ok) {
            console.log('Health check sent');
        } else {
            console.warn(`Health check failed: ${response.status}`);
        }
    } catch (error) {
        console.debug('Health check unavailable (PDV may be offline)');
        // Non-fatal; Client continues to function
    }
}
```

### 7.2 PDV Health Check Registry

The PDV maintains a health check registry with one record per unique `X-App-ID`. Each record includes:

* `firstConnection`: Timestamp of first connection (set on initial health check)
* `lastConnected`: Timestamp of most recent health check (updated on each check)
* `connectionCount`: Total number of health checks received (incremented on each check)
* `appType`, `appName`, `appVersion`, `platformInfo`: Metadata about the component

This data is used by the PDV to:

* **Display Statistics:** Show total and active (24h) Client counts in dashboards
* **Monitor Adoption:** Track how many users are engaging with privacy tools
* **Usage Analytics:** Understand which Client versions are in use
* **Audit Compliance:** Maintain logs of which Clients have accessed the PDV

---

## 8) Standard Views and UX Patterns

| View                    | Inputs              | Description                                  |
| ----------------------- | ------------------- | -------------------------------------------- |
| **Influence Dashboard** | Metrics + top Atoms | Pie & time-series of exposure by actor/topic |
| **Narrative Map**       | Relations           | Force-directed graph of repeated templates   |
| **Tone Profile**        | Atoms               | Stacked bar of persuasion tone distribution  |
| **Timeline Explorer**   | Events + Atoms      | Scrollable feed of exposures                 |
| **Report Generator**    | Metrics + Atoms     | Daily/weekly PDF/HTML digest                 |

All views must include:

* timestamps and provenance,
* visibility of underlying evidence (link to Atom or Event ID),
* plain-language explanations (“Why is this shown?”).

---

## 9) Presentation Contracts (Recommended Schemas)

### 9.1 `pcfx.view_spec/0.1`

Declarative description of a dashboard or visualization.

```json
{
  "schema": "pcfx.view_spec/0.1",
  "view_id": "influence_dashboard",
  "title": "Top Actors by Exposure",
  "input": ["pcfx.metric/0.1"],
  "layout": "bar_chart",
  "filters": {"window":"P7D","metric":"exposure_velocity"},
  "explainable": true
}
```

### 9.2 `pcfx.alert/0.1`

Real-time notification structure.

```json
{
  "schema": "pcfx.alert/0.1",
  "id": "uuid",
  "ts": "2025-10-20T14:00:00Z",
  "level": "info|warning|critical",
  "message": "Exposure to aspirational tone rose 60% this week.",
  "related_metrics": ["metric:uuid"],
  "action": {"type": "open_view","target": "tone_profile"},
  "signature": "eddsa-p256:…"
}
```

---

## 10) Security Requirements

* **Read-Only Guarantee:** PDV exposes read endpoints with signed, non-mutating tokens.
* **Transport:** HTTPS / localhost only.
* **Session:** ephemeral access tokens (rotated ≤ 24 h).
* **Integrity:** PDV verifies Client signature before issuing token.
* **Export:** any external export (CSV, PDF, etc.) must go through PDV’s “user export” workflow.

---

## 11) Performance Guidelines

* Paginate large queries (default 100 atoms/page).
* Cache immutable responses (ETag or `If-None-Match`).
* UI latency target < 250 ms (local PDV).
* Offload heavy visualization (graphs > 10 k nodes) to WebWorker or WASM.
* Lazy-load evidence text/blobs on demand.

---

## 12) Error Handling

| Code  | Meaning                           | Client Action                 |
| ----- | --------------------------------- | ----------------------------- |
| `401` | Unauthorized / missing capability | Re-authenticate               |
| `403` | Consent revoked                   | Notify user                   |
| `404` | Artifact expired or purged        | Remove from view              |
| `429` | Rate limit                        | Backoff + retry               |
| `5xx` | PDV internal error                | Show “data unavailable” badge |

---

## 13) Telemetry & Feedback

Clients MAY emit non-PII telemetry to PDV for debugging:

```
client.views_rendered_total
client.query_latency_ms
client.cache_hits
client.errors_total
```

Telemetry is local-only and auto-purged.

---

## 14) Reference Client Profiles

| Profile                    | Description                        | Outputs                                        |
| -------------------------- | ---------------------------------- | ---------------------------------------------- |
| **Dashboard Client**       | Interactive React / PWA dashboard. | Live charts, filters, drill-down evidence.     |
| **Report Generator**       | Headless Python / Node script.     | Daily / weekly HTML + PDF digests.             |
| **Voice Assistant Client** | Audio interface (local TTS/STT).   | Spoken summaries (“Who influenced me today?”). |
| **AR Overlay Client**      | Heads-up display (glasses).        | Real-time on-screen narrative highlights.      |

All share identical PDV API usage.

---

## 15) Compliance Checklist (for Client Implementers)

* [ ] Manifest signed; scopes minimal; no write access.
* [ ] Consent confirmed; PDV redaction enabled.
* [ ] UI shows evidence/provenance for all claims.
* [ ] Network isolation enforced (`net.out` only for updates).
* [ ] Rate-limit/backoff implemented.
* [ ] Expired data auto-purged.
* [ ] User export route uses PDV’s API only.

---

## 16) Example Pseudocode

### 16.1 Query Atoms and Render Top Actors

```python
atoms = pdv.get("/atoms?since=2025-10-13")
actors = aggregate_by_entity(atoms)
chart.bar(actors, key="exposure_share")
```

### 16.2 Subscribe to Metrics for Realtime Alerts

```javascript
const ws = new WebSocket("ws://localhost:8080/pcfx.metrics");
ws.onmessage = evt => {
  const metric = JSON.parse(evt.data);
  if(metric.name === "influence_index" && metric.value > 2.0){
     notifyUser("High influence detected", metric.scope);
  }
};
```

### 15.3 Generate Weekly Report

```python
metrics = pdv.get("/metrics?window=P7D")
html = render_template("weekly_report.html", metrics=metrics)
save_pdf(html, "weekly_report.pdf")
```

---

## 16) Schemas (to add alongside)

* `pcfx.client_manifest/0.1`
* `pcfx.view_spec/0.1`
* `pcfx.alert/0.1`
* (Reuse) `pcfx.metric/0.1`, `pcfx.knowledge_atom/0.1`, `pcfx.relation/0.1`

---

## 17) Why this spec matters (for Adapters & Nodes)

* **Adapters** gain confidence that the data they emit will be visible and auditable through standardized UI conventions.
* **Nodes** can publish analytics once and know any compliant Client can visualize them without bespoke integrations.
* **Users** receive a consistent, transparent, evidence-based cognitive firewall experience—no black boxes, no vendor lock-in.

---
