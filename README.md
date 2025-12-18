# CycleNestOrchestrator – Service-Centric REST Application

---

## **__Overview__**

**CycleNestOrchestrator** is a Java-based RESTful web application developed as part of the **Service-Centric & Cloud Computing** coursework at Nottingham Trent University. The project demonstrates the application of **Service-Oriented Architecture (SOA)** principles to integrate multiple services, including an internal item rental service and an external routing service, with a focus on **Quality of Service (QoS)** and robust error handling.

The system is designed as a modular REST application deployed on **Apache Tomcat**, using **JAX-RS (Jersey)** for service implementation.

---

## **__Architecture Summary__**

The application is structured around clear service boundaries to ensure modularity and scalability:

### **Part A – Internal Services**
* **Cycle Rental Item Service**: Manages the inventory and details of available cycles.
* **Rental Request Workflow Service**: Handles the logic and state for booking requests.

### **Part B – External Service Integration**
* **OSRM Integration**: Real-world distance and duration calculation using the Open Source Routing Machine public API.

> **Technical Note**: The system uses thread-safe, in-memory repositories to manage application state, fulfilling the core requirements of the coursework brief.

---

## **__Technologies Used__**
* **Language**: Java 17
* **Server**: Apache Tomcat 9
* **Framework**: JAX-RS (Jersey)
* **Data Handling**: Jackson (JSON serialization/deserialization)
* **Tools**: Netbeans IDE, Postman, Git Bash

---

## **__Project Structure__**
```text
CycleNestOrchestrator/
├── src/java/
│   ├── cyclenest/
│   │   ├── model/         # Domain models (Item, RentalRequest)
│   │   ├── repository/    # Thread-safe repositories (ConcurrentHashMap)
│   │   └── resource/      # REST resources for Part A
│   └── osrm/
│       ├── OsrmClient.java     # External API consumer logic
│       ├── OsrmResource.java   # REST endpoints for Part B
│       └── model/              # OSRM response and result DTOs
├── web/
│   └── WEB-INF/
│       └── web.xml        # Deployment descriptor
└── README.md
