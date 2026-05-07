# CommonGrant Portal — Complete Solution

Nonprofit grant management portal. One application → multiple funders. Reduces overhead by 80%.

---

## Project Structure

```
commongrant/
├── backend/                         ← Spring Boot (Java 17)
│   ├── pom.xml
│   └── src/main/java/com/commongrant/
│       ├── CommonGrantApplication.java
│       ├── model/Models.java            ← JPA entities
│       ├── dto/Dtos.java                ← Request/response objects
│       ├── repository/Repositories.java ← Spring Data JPA
│       ├── service/
│       │   ├── AuthService.java
│       │   ├── ApplicationService.java
│       │   └── OtherServices.java       ← Funder, Vault, Compliance
│       ├── controller/Controllers.java  ← REST endpoints
│       ├── security/Security.java       ← JWT + Spring Security
│       └── config/Config.java           ← CORS + exception handling
│   └── src/main/resources/
│       ├── application.yml              ← H2 (dev) / PostgreSQL (prod)
│       └── data.sql                     ← Seed data (demo org + funders)
│
└── frontend/
    └── index.html                       ← Complete SPA, zero build step
```

---

## Run the Backend

### Prerequisites
- Java 17+ installed (`java -version`)
- Maven 3.9+ installed (`mvn -version`)

### Steps

```bash
cd commongrant/backend

# 1. Build
mvn clean install -DskipTests

# 2. Run (uses H2 in-memory DB — zero setup needed)
mvn spring-boot:run
```

Backend starts at **http://localhost:8080**

> H2 Console: http://localhost:8080/h2-console  
> JDBC URL: `jdbc:h2:mem:commongrant` · User: `sa` · Password: *(blank)*

---

## Run the Frontend

```bash
# Option A — Just open the file
open commongrant/frontend/index.html

# Option B — Serve locally (needed for file uploads to work in all browsers)
cd commongrant/frontend
python3 -m http.server 3000
# Open: http://localhost:3000
```

---


Pre-loaded with:
- 6 funders (Patagonia, Gates, RWJ, Kellogg, Bloomberg, Ford)
- Demo organization: GreenPath Foundation

---

## API Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/login` | Login → JWT token |
| POST | `/api/v1/auth/register` | Register new org |
| GET | `/api/v1/applications` | List my applications |
| POST | `/api/v1/applications` | Create draft |
| PUT | `/api/v1/applications/{id}` | Update draft |
| POST | `/api/v1/applications/{id}/submit` | Submit to funders |
| DELETE | `/api/v1/applications/{id}` | Delete draft |
| GET | `/api/v1/applications/dashboard` | Dashboard stats |
| GET | `/api/v1/funders` | List all funders |
| GET | `/api/v1/funders?focus=Education` | Filter funders |
| GET | `/api/v1/documents` | List vault docs |
| POST | `/api/v1/documents/upload` | Upload document |
| DELETE | `/api/v1/documents/{id}` | Delete document |
| GET | `/api/v1/compliance/overview` | Compliance scores |
| POST | `/api/v1/compliance/reports` | Submit report |

---

## Switch to PostgreSQL

Change `application.yml`:

```yaml
spring:
  profiles:
    active: prod   # was: dev
```


---

## Frontend Features (fully wired to backend)

| Feature | Status |
|---------|--------|
| Login / Register with JWT | ✅ Live API |
| Dashboard stats | ✅ Live API |
| Create application (5-step form) | ✅ Live API |
| Select multiple funders | ✅ Live API |
| Submit to funders | ✅ Live API |
| List/filter/delete applications | ✅ Live API |
| Document vault (upload/delete) | ✅ Live API |
| Funder directory + filter | ✅ Live API |
| Compliance overview | ✅ Live API |
| Post-grant report submission | ✅ Live API |

