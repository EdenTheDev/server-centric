# CycleNest Coursework Agent Guidelines

## General Expectations
- Treat this repository as the working area for the COMP30231 CycleNest coursework. Keep all notes, scenarios, and requirements versioned here so they can be referenced later.
- Preserve the coursework brief verbatim when you capture it. If you summarise or reorganise content, retain the original meaning and cite the source section headings for clarity.
- Document every material change with an explicit Git commit that explains **what** changed and **why** (e.g., "Document OSRM Route API decision for proximity lookups").

## Editing This AGENTS.md in VS Code on Ubuntu
1. Open VS Code and use `File → Open Folder…` to load the repository path (e.g., `/workspace/server-centric`).
2. Locate `AGENTS.md` in the Explorer. If it is not visible, ensure "Show Hidden Files" is enabled.
3. Create a new branch before editing (`git switch -c <feature-name>` in the integrated terminal) to keep changes isolated.
4. Follow a predictable structure when updating this file:
   - Keep the top-level heading (# CycleNest Coursework Agent Guidelines).
   - Add or amend sections using second-level headings (##).
   - Use ordered or unordered lists for step-by-step instructions.
   - When inserting coursework extracts, wrap them in fenced code blocks or block quotes to preserve formatting.
5. After editing, save with `Ctrl+S`, run `git status` to review updates, and stage/commit with a descriptive message.
6. Run `git diff` before committing to check for accidental whitespace or formatting issues.
7. Push the branch only after verifying that the remote (`git remote -v`) points to the correct Olympus or GitHub repository.

## Coursework Workflow Priorities
- **Start with Part B (External API Integration)**: choose and document the OSRM endpoint (Nearest, Route, Table, etc.), describe why it fits the proximity requirement, and outline error handling for timeouts/unavailable services.
- Maintain notes on dataset usage (e.g., `cycle_nest_items06-city.json`, `cycle_nest_items_with_coordinates.json`) and any schema adjustments.
- Capture tool choices and versions in commits (recommended stack: NetBeans 18, JDK 17, Tomcat 9; preferred NoSQL storage such as MongoDB or Azure Cosmos DB).
- Log QoS test plans and outcomes (JMeter scenarios, payload sizes, observed bottlenecks) to support Part C decisions before implementing optimisations.
- When you reach Part D, record which option you selected (Security, Containerisation, or Cloud Deployment) and summarise the rationale in both this file and commit messages.

## Development Environment
- **IDE:** Apache NetBeans 18  
- **JDK:** 17 (Temurin or Oracle distribution)  
- **Server:** Apache Tomcat 9.0.x  
- **Database:** Cloud-based NoSQL (MongoDB Atlas or Azure Cosmos DB recommended)  
- **Testing Tools:** Postman for REST validation, Apache JMeter for QoS testing  
- **Version Control:** GitHub private repository (linked to Olympus submission)


## Git & Documentation Reminders
- Keep a chronological Git log that narrates the development story, including QoS findings, API decisions, and security or deployment considerations.
- When referencing external research or cost implications (e.g., managed MongoDB tiers), cite them either in commit messages or in a dedicated documentation file.
- For live demonstration prep, document how to run the orchestrator, invoke endpoints, and validate OSRM responses so you can rehearse within the 13-minute slot.

## Asset Management
- Store screenshots, JMeter `.jmx` plans, and any supporting evidence under a dedicated directory (e.g., `docs/evidence/`).
- Include deployment or container artefacts (`Dockerfile`, VM setup scripts) alongside clear README instructions when tackling Part D options.
- Keep sensitive credentials out of the repository; use environment variables or example templates (`.env.example`).

Following these guidelines will help maintain a clean, traceable history while aligning with the coursework assessment criteria.