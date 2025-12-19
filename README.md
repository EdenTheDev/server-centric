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
```
---

## **__Running the Application__**

### **Requirements**
* NetBeans 18 or higher
* JDK 17
* Apache Tomcat 9

### **Steps**
1. Open the project in **NetBeans**.
2. Ensure **Tomcat 9** is configured as the application server.
3. **Clean and Build** the project.
4. Run the application on Tomcat.
5. Access services via the context root: `http://localhost:8080/CycleNestOrchestrator`

---

## **__REST Endpoints__**

### **Part A – Item Service**
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/items` | Retrieve all rental items |
| **GET** | `/items/{id}` | Retrieve a specific item by ID |
| **POST** | `/items` | Create a new cycle item |

### **Part A – Rental Request Service**
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/requests` | Retrieve all rental requests |
| **POST** | `/requests` | Submit a new rental request |

### **Part B – Distance Service**
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/distance` | Calculate metrics between two coordinates |

* **Query Parameters**: `lat1`, `lon1`, `lat2`, `lon2`
* **Example**: `/distance?lat1=52.9548&lon1=-1.1581&lat2=52.9225&lon2=-1.4746`

---

## **__Concurrency & Quality of Service (QoS)__**

To support high-quality service delivery and concurrent requests, the following measures were implemented to meet the **Evidence of Excellence** criteria:

* **Thread-Safety**: Use of `ConcurrentHashMap` and `AtomicInteger` ensures data integrity during simultaneous requests.
* **Resource Management**: Efficient reuse of the `HttpClient` for connection pooling to minimize latency.
* **Error Resilience**: A custom exception hierarchy (`OsrmException`) maps internal failures to precise HTTP status codes, such as **400 (Bad Request)** and **502 (Bad Gateway)**.
### **Objective**
To critically evaluate the scalability of the Orchestrator under "Big Data" conditions[cite: 52]. The testing focuses on the integration between the internal Item Service (10,000 items) and the external OSRM Distance Service.

### **Hypothesis & Expected Bottlenecks**
The current architecture processes distance requests using a linear **O(N)** approach. For every user request:
1.  The Orchestrator retrieves all 10,000 items.
2.  It iterates through the list and calls the external OSRM API for **each item** to calculate travel duration.

**Anticipated Issues:**
* **High Latency:** Making 10,000 synchronous HTTP calls to an external service for a single user request is expected to result in response times exceeding acceptable limits (> 10s).
* **API Throttling:** The external OSRM provider is expected to block or throttle requests (HTTP 429) due to the high volume of traffic.
* **Scalability Failure:** Under concurrent load (simulated via JMeter), the thread pool is expected to exhaust, leading to service unavailability.

### **Test Environment**
* **Tool:** Apache JMeter[cite: 52].
* [cite_start]**Dataset:** `cycle_nest_items_with_coordinates.json` (10,000 entries)[cite: 41].
* **Metric Goals:** Latency < 500ms, Error Rate < 1%.


## **__Testing and Version Control__**
* **Testing**: All endpoints have been verified using Postman. Evidence includes successful GET/POST cycles and correct JSON schema validation.
* **Version Control**: A rigorous Git commit history is maintained in the Olympus repository, documenting the incremental development process and architectural decisions as required by the specification.
