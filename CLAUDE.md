# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

---

## Project Overview

**Wopfun** is a Spring Boot application for kindergarten documentation automation. It helps physiotherapists generate official WOPFU and IPET documents for children based on stored observations and AI-generated content.

### Purpose
The application automates the tedious manual process of filling out standardized educational documents (WOPFU/IPET) for children in kindergarten. A physiotherapist enters basic child data and free-form observation notes, then the system:
1. Stores data in PostgreSQL
2. Generates formal pedagogical content using AI (Claude API)
3. Fills a .docx template with the generated content
4. Produces a ready-to-print document

### Key User Workflow
1. Add child (name, birth date, diagnosis, group)
2. Add observation notes (free-form text entries over time)
3. Click "Generate IPET" → AI creates formal content
4. Review/edit generated content
5. Download filled .docx document

---

## Tech Stack

- **Java 17+**
- **Spring Boot** (MVC, not WebFlux - keeping it simple)
- **Spring Data JPA** for database access
- **PostgreSQL** for data storage
- **Thymeleaf** for server-side HTML rendering (simple forms, no React yet)
- **poi-tl** library for .docx template filling (future phase)
- **Claude API** (Anthropic) for AI content generation (future phase)

---

## Database Schema

### Core Tables

**`child`** - Basic child information
- `id` (SERIAL PRIMARY KEY)
- `first_name`, `last_name` (TEXT NOT NULL)
- `birth_date` (DATE)
- `group_name` (TEXT) - kindergarten group name
- `diagnosis` (TEXT) - optional medical/developmental diagnosis
- `created_at` (TIMESTAMPTZ)

**`child_note`** - Free-form observation notes
- `id` (SERIAL PRIMARY KEY)
- `child_id` (FK → child)
- `note_date` (DATE) - when observation was made
- `content` (TEXT) - free-form observation text
- `category` (TEXT) - optional (e.g., "motoryka", "zachowanie")
- `created_at` (TIMESTAMPTZ)

**`generated_document`** - AI-generated documents
- `id` (SERIAL PRIMARY KEY)
- `child_id` (FK → child)
- `doc_type` (TEXT) - "WOPFU" or "IPET"
- `generated_at` (TIMESTAMPTZ)
- `ai_strengths` (TEXT) - AI-generated "mocne strony"
- `ai_recommendations` (TEXT) - AI-generated "zalecenia" (JSON array as text)
- `ai_goals` (TEXT) - AI-generated "cele szczegółowe" (JSON array as text)
- `file_path` (TEXT) - path to generated .docx file
- `status` (TEXT) - "DRAFT" or "APPROVED"

---

## Project Structure

```
com.dovskyy.wopfun/
├── controller/          # Spring MVC Controllers (Thymeleaf views + REST endpoints)
├── service/             # Business logic layer
│   ├── ChildService
│   ├── ChildNoteService
│   ├── DocumentGenerationService (future: orchestrates AI + docx)
│   ├── AIService (future: Claude API integration)
│   └── DocxService (future: poi-tl integration)
├── repository/          # Spring Data JPA repositories
├── model/               # JPA Entity classes
├── dto/                 # Data Transfer Objects (form inputs, API responses)
└── config/              # Spring configuration classes
```

---

## Development Commands

### Running the Application

```bash
# Start application (default port 8080)
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

### Database Setup

**Using Docker:**
```bash
docker run --name wopfun-db -e POSTGRES_PASSWORD=password -e POSTGRES_DB=wopfun -p 5432:5432 -d postgres:15
```

**Connection settings** (in `application.properties`):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/wopfun
spring.datasource.username=postgres
spring.datasource.password=password
```

### Testing

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=WopfunApplicationTests
```

### Building

```bash
# Package as JAR
./mvnw clean package

# Skip tests during build
./mvnw clean package -DskipTests
```

---

## Important Architecture Notes

### Data Entry Philosophy
The application is designed for **non-technical users**. The physiotherapist should be able to:
- Enter data through simple HTML forms
- Write observations in plain, natural language (no structured fields)
- Generate documents with one click

### AI Integration (Future Phase)
When implementing AI generation:
- Use **Claude API** (Anthropic) - better Polish language support than GPT-4
- The prompt template is in Polish and generates formal pedagogical language
- AI output is **always editable** before final document generation
- Expected AI response format:
```json
{
  "mocne_strony": "Formal paragraph about child's strengths...",
  "zalecenia": ["Recommendation 1", "Recommendation 2", ...],
  "cele_szczegolowe": ["Goal 1", "Goal 2", ...]
}
```

### Document Generation (Future Phase)
- Use **poi-tl** library (https://deepoove.com/poi-tl/)
- Template files stored in `/tools/templates/`
- Placeholders in .docx: `{{imie}}`, `{{nazwisko}}`, `{{mocne_strony}}`, etc.
- Generated files stored in `/generated-docs/` (gitignored)

---

## Configuration Notes

### application.properties
- Database connection settings
- JPA: `spring.jpa.hibernate.ddl-auto=update` (auto-creates tables in development)
- Use `schema.sql` for production-ready schema initialization

### Dependencies (pom.xml)
Core dependencies:
- `spring-boot-starter-web` - Spring MVC
- `spring-boot-starter-data-jpa` - Database access
- `spring-boot-starter-thymeleaf` - Server-side templates
- `postgresql` - PostgreSQL JDBC driver
- `poi-tl` - .docx generation (to be added)

---

## Current Development Phase

**Phase 1: Core Application** (in progress)
- ✓ Project setup
- ⧖ Database schema & entities
- ⧖ Basic CRUD for children and notes
- ⧖ Thymeleaf forms and views

**Phase 2: AI Integration** (future)
- Claude API service
- Prompt engineering for Polish pedagogical content
- Draft/review/edit workflow

**Phase 3: Document Generation** (future)
- poi-tl integration
- Template placeholder mapping
- File download endpoints

**Phase 4: Deployment** (future)
- Deploy to local NUC server
- Production database setup
- Possible migration to React frontend

---

## Polish Language Notes

All user-facing content is in **Polish**. Key terminology:
- **IPET** - Indywidualny Program Edukacyjno-Terapeutyczny
- **WOPFU** - (official kindergarten assessment document)
- **mocne strony** - strengths
- **zalecenia** - recommendations
- **cele szczegółowe** - specific goals
- **diagnoza** - diagnosis
- **obserwacje** - observations
