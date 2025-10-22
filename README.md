# Wopfun - Kindergarten Documentation Automation

A Spring Boot application that automates the generation of official WOPFU and IPET documents for kindergarten children based on physiotherapist observations and AI-generated content.

## Overview

Wopfun streamlines the tedious process of creating standardized educational documents (WOPFU/IPET) for children in kindergarten. Physiotherapists can:
- Enter child data and free-form observation notes
- Generate formal pedagogical content using AI (Claude API)
- Produce ready-to-print .docx documents from templates

### Key Workflow

1. Add child information (name, birth date, diagnosis, group)
2. Add observation notes (free-form text entries over time)
3. Click "Generate IPET" to create AI-generated formal content
4. Review and edit the generated content
5. Download the filled .docx document

## Tech Stack

- **Java 17+**
- **Spring Boot** (MVC architecture)
- **Spring Data JPA** - Database access layer
- **PostgreSQL** - Data persistence
- **Thymeleaf** - Server-side HTML rendering
- **poi-tl** - .docx template filling (future phase)
- **Claude API** (Anthropic) - AI content generation (future phase)

## Database Schema

### Core Tables

**child** - Basic child information
- `id`, `first_name`, `last_name`, `birth_date`
- `group_name` - kindergarten group
- `diagnosis` - optional medical/developmental diagnosis
- `created_at`

**child_note** - Observation notes
- `id`, `child_id` (FK)
- `note_date`, `content`
- `category` - optional (e.g., "motoryka", "zachowanie")
- `created_at`

**generated_document** - AI-generated documents
- `id`, `child_id` (FK), `doc_type` ("WOPFU" or "IPET")
- `generated_at`, `status` ("DRAFT" or "APPROVED")
- `ai_strengths`, `ai_recommendations`, `ai_goals`
- `file_path` - path to generated .docx

## Project Structure

```
com.dovskyy.wopfun/
├── controller/          # Spring MVC Controllers
├── service/             # Business logic
│   ├── ChildService
│   ├── ChildNoteService
│   ├── DocumentGenerationService (future)
│   ├── AIService (future)
│   └── DocxService (future)
├── repository/          # Spring Data JPA repositories
├── model/               # JPA Entity classes
├── dto/                 # Data Transfer Objects
└── config/              # Configuration classes
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL 15+ (or Docker)

### Database Setup

Using Docker:
```bash
docker run --name wopfun-db -e POSTGRES_PASSWORD=password -e POSTGRES_DB=wopfun -p 5432:5432 -d postgres:15
```

Configure in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/wopfun
spring.datasource.username=postgres
spring.datasource.password=password
```

### Running the Application

```bash
# Start application (port 8080)
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

### Testing

```bash
# Run all tests
./mvnw test

# Run specific test
./mvnw test -Dtest=WopfunApplicationTests
```

### Building

```bash
# Package as JAR
./mvnw clean package

# Skip tests
./mvnw clean package -DskipTests
```

## Development Roadmap

**Phase 1: Core Application** (in progress)
- ✓ Project setup
- ⧖ Database schema & entities
- ⧖ Basic CRUD for children and notes
- ⧖ Thymeleaf forms and views

**Phase 2: AI Integration** (future)
- Claude API service integration
- Polish pedagogical content generation
- Draft/review/edit workflow

**Phase 3: Document Generation** (future)
- poi-tl integration for .docx templates
- Template placeholder mapping
- File download functionality

**Phase 4: Deployment** (future)
- Deploy to local NUC server
- Production database setup
- Possible React frontend migration

## Architecture Philosophy

### User-Friendly Design
The application is designed for non-technical users (physiotherapists):
- Simple HTML forms for data entry
- Natural language observations (no rigid structure)
- One-click document generation

### AI Integration
- Uses **Claude API** for superior Polish language support
- Generates formal pedagogical language from casual notes
- All AI output is editable before finalization
- Expected format: strengths paragraph + recommendation/goal lists

### Document Generation
- poi-tl library for .docx template filling
- Templates in `/tools/templates/`
- Generated files in `/generated-docs/` (gitignored)

## Polish Terminology

- **IPET** - Indywidualny Program Edukacyjno-Terapeutyczny
- **WOPFU** - Official kindergarten assessment document
- **mocne strony** - strengths
- **zalecenia** - recommendations
- **cele szczegółowe** - specific goals
- **diagnoza** - diagnosis
- **obserwacje** - observations

## License

[Add your license here]

## Contributing

[Add contributing guidelines here]
