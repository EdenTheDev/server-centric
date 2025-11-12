# CycleNest Coursework Brief

## Assessment Details
- **Module code:** COMP30231
- **Module title:** Service-Centric and Cloud Computing
- **Module leader:** Ismahane Cheheb
- **Module team:** Brad Patrick, Daniyal Haider, Taha Osman
- **Coursework title:** Software Application Integration using Web Services
- **Contribution to module:** 60%
- **Dates:**
  - Set: 10/10/2025
  - Formative submission: 13/11/2025 at 14:30
  - Summative submission: 19/01/2026 at 14:30
  - Feedback: 20/02/2026 at 14:30
  - Demos: Weeks commencing 19/01/2026 (two weeks)
- **Submission method:** Upload zipped development code folder plus signed AI usage declaration to Dropbox, including Git log and testing evidence.
- **Required declarations:** Signed code authorship declaration and AI usage declaration.
- **Repository hosting:** Private Olympus repository with collaborators `cmp3chehei`, `cmp3haided`, `cmp3osmantm`, `cmp3patrib`.

### Module Learning Outcomes
1. Demonstrate appreciation of advanced SOA capabilities for large-scale distributed applications.
2. Critically evaluate service security and QoS in enterprise and cloud contexts.
3. Investigate cloud computing/storage exploitation via emerging Web/Business Intelligence.
4. Conduct service-oriented analysis to deliver reusable, interoperable distributed components (MLO6).
5. Compose new/legacy services to deliver added value (MLO7).
6. Evaluate QoS and security for migration to private/public/hybrid clouds (MLO8).

### Late Submission & Extensions
- Up to five working days late: capped at Low Third.
- More than five working days late: zero unless extension granted for valid personal circumstances.
- Re-sit submissions: zero if late beyond original deadline.

## Assessment Scenario Overview
Develop **CycleNest**, a RESTful service enabling short-term item rentals to promote sustainability. The orchestrator service must be built in Java, deployed on Tomcat (local or remote), and communicate via JSON. Clients should support:
- Searching available items with filtering (e.g., price, location).
- Requesting an item (creating a `pending` request).
- Cancelling a request (setting status to `cancelled`).

Supporting guidance is available via the provided activities on REST services, persistence, JSON serialization/deserialization, and troubleshooting.

### Data Requirements
- Persist users, items, and rentals in a cloud-hosted database (SQL or NoSQL; JSON-compatible NoSQL recommended).
- Each item should track: ID, owner ID, name, category, location (postcode/coordinates), daily rate, availability, condition, description.
- Provided sample data: `cycle_nest_items06-city.json` (6 items) and `cycle_nest_items_with_coordinates.json` (10,000 items).

### External Service Integration
- Integrate one JSON-based external API (e.g., OSRM) to compute proximity between item and user locations.
- Implement robust error handling for unavailability, timeouts, and invalid responses.
- Document API choice and error-handling strategies in Git commits.

### Quality of Service (QoS) Testing & Improvement
1. Stress test using JMeter (or similar) to simulate high concurrency and large payloads.
2. Submit evidence (.jmx, screenshots) with code.
3. Identify bottlenecks, propose, and implement one technical solution justified by test results and supported by recent research.
4. Document findings, solutions, and potential cloud costs in Git history.

### Optional Advanced Task (choose one)
- **Security:** Identify and mitigate at least one significant vulnerability, with justification in Git log. Additional mitigations earn higher credit.
- **Containerisation:** Provide functional Dockerfile (or equivalent) exposing REST endpoints, with documented dependencies and build steps.
- **Cloud Deployment:** Deploy orchestrator on IaaS, provide public access, and document configuration, security, and startup instructions (e.g., README).

## Submission & Demonstration Requirements
- Provide Git log with descriptive commit messages covering functionality, testing, QoS analysis, security/container/cloud decisions, and any associated costs.
- Include testing evidence in submission archive.
- Live demo on campus (13 minutes: 8-minute presentation + 5-minute Q&A). Work not demonstrated will not be marked.
- Presentation should cover functionality and integration details; be prepared to answer code questions.

## Assessment Criteria Summary
- **HTTP & RESTful Design (30%)**: Correct use of HTTP methods, REST conventions, and integrated cloud storage.
- **API Integration & Error Handling (20%)**: Functional external API consumption with comprehensive error handling.
- **JSON Communication & Handling (15%)**: Strict JSON communication with proper serialization/deserialization.
- **QoS Testing & Improvement (20%)**: Rigorous testing, analysis, and justified improvements referencing research.
- **Advanced Feature (15%)**: Security mitigation, containerisation, or cloud deployment (only credited if core work completed).

## Feedback Opportunities
- **Formative:** Verbal feedback during labs; formative submission on 13/11/2025.
- **Summative:** Written feedback via Dropbox with final grade post submission.

## Supporting Modules
- Distributed Network Architectures and Operating Systems (BSc Cyber Security)
- Systems Software (all other courses)

## Additional Notes
- Demonstrations occur during the weeks commencing 19/01/2026.
- Ensure orchestrator communicates exclusively in JSON and uses custom classes for serialization/deserialization.
- Testing tools (curl, Postman, or text-based clients) may be used for verification.
- Maintain both pre- and post-QoS-improvement code versions in Git for tutor review.
- Keep your Olympus repository synced by verifying the current branch with `git status -sb` or `git branch` before pushing.
- If `git push -u origin work` reports `src refspec work does not match any`, create or switch to the branch locally using `git checkout -b work` (or `git switch -c work`) and commit before pushing.
- Confirm the remote is configured with `git remote -v`; add it via `git remote add origin <repository-url>` if missing.
