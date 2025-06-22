#  Modular In-Memory Address Book Service


---

##  Features

-  In-memory data storage (no external DB)
-  Modular design (Controller, Service, DAO, Index)
-  Fast substring search using n-gram inverted index
-  Scalable for large datasets
-  RESTful API with JSON support
-  Runs on port `5000`

---



## Tech Stack

- Java 17+
- Spring Boot 3.x
- JUnit 5
- Mockito (for unit testing)
- No external DB or dependencies (fully self-contained)

---

## Setup & Run

### 🔧 Prerequisites
- Java 17+
- Maven 3+

### Details about Quick fetch
I have used In-Memory Indexing also known as N-Gram Based
This is done to make the seach fast for a big database
For every contact name, we break the name into smaller parts of 3 letters. For example, if the name is Vansh, we create:

van, ans, nsh
These small parts (called 3-grams) are saved in a map. The map keeps track of which contacts have which parts in their name.

So when someone search with part of a name like "ans", the service checks the map and quickly finds all matching contact IDs without going through all contacts one by one.

This makes search very fast and good for performance, even when millions of contacts are saved.

If user search with very small word (like 1 or 2 letters), we try to scan more or fallback to full check.

This indexing is done completely in memory using simple Java structures — no database or external search engine is used.



### ▶️ Run the project

```bash

mvn spring-boot:run




