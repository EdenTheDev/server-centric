
# QoS Analysis: OSRM API Bottleneck

## 1. Identified Issue
During JMeter stress testing of the CycleNestOrchestrator, a 98% error rate was observed under concurrent load. The system returned HTTP 502/504 errors.

## 2. Root Cause
The public OSRM API (router.project-osrm.org) is a significant bottleneck. It enforces strict rate limits (HTTP 429), 
causing the Orchestrator to hang while waiting for responses, eventually timing out.

## 3. Technical Solution (Part C)
To improve Quality of Service (QoS):
- **KNN Filtering:** I am implementing a local Haversine formula to filter the 10,000 items down to the 5 closest bikes before calling the API.
- **Robust Error Handling:** Added 2s/3s timeouts to the Java HttpClient to prevent thread exhaustion.

## 4. Proposed Migration (Part D)
To achieve 100% availability and bypass third-party rate limits, I aim to implement **D.2 Containerisation**. Moving the OSRM backend to a 
local Docker container will ensure the service is "Cloud-ready" and scalable.