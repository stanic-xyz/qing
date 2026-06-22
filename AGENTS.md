# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## Project Overview

Qing (青) is a domain-driven microservice practice platform. It consists of:
- **Backend**: Java 21 microservices using Spring Boot 3.x, JPA/Hibernate, and LangChain4j for AI
- **Frontend**: React 18 + TypeScript applications with Vite, Tailwind CSS, and Zustand
- **Infrastructure**: Common libraries in `qing-support/`, Maven BOM in `qing-bom/`

## Build Commands

### Backend (Java/Maven)
```bash
# Full build (from repo root)
mvn clean install

# Build specific service with dependencies
mvn clean package -pl qing-services/qing-service-llm -am

# Run a service
mvn spring-boot:run -pl qing-services/qing-service-llm

# Run tests
mvn test -pl qing-services/qing-service-llm

# Run a single test class
mvn test -pl qing-services/qing-service-llm -Dtest=ClassName

# Checkstyle
mvn checkstyle:check -pl qing-services/qing-service-llm
```

### Frontend (Node.js)
```bash
cd qing-frontend/finance-web

# Install dependencies
npm install

# Development server
npm run dev

# Production build
npm run build

# Lint
npm run lint
```

## Architecture

### Backend Structure (`qing-services/`)
- **qing-service-llm**: Core finance service - handles bill import, transaction matching, AI chat
- **qing-service-auth**: Authentication service (Spring Security + JWT)
- **qing-service-anime**: Anime-related service
- **qing-service-workflow**: Workflow/state machine service
- **qing-service-gateway**: API Gateway
- **qing-service-eureka**: Service discovery

### Support Modules (`qing-support/`)
- **qing-commons**: Shared utilities, exceptions, enums, API models (`cn.chenyunlong.common.*`)
- **qing-domain-common**: Domain-driven design base classes
- **qing-infrastructure**: Infrastructure concerns
- **qing-starters**: Spring Boot starters

### Frontend Structure (`qing-frontend/finance-web/src/`)
- `api/` - Axios-based API calls
- `pages/` - Route pages (Import, Accounts, Categories, Dashboard, Transactions, etc.)
- `components/` - Shared UI components
- `store/` - Zustand state management
- `hooks/` - Custom React hooks
- `types/` - TypeScript type definitions

### Key Backend Patterns
- Entity package: `entity/` with JPA annotations
- Repository layer: Spring Data JPA repositories
- Service layer: Business logic including `UploadService` (bill parsing), `MatcherService` (transaction matching)
- Controller layer: REST APIs

## Code Conventions

### Java
- Package naming: `cn.chenyunlong.qing.*`
- Checkstyle config: `checkstyle/checkstyle.xml` (checkstyle runs during validate phase but is configured to skip by default)
- Uses Lombok for boilerplate reduction
- MapStruct for DTO mapping

### Frontend
- TypeScript strict mode
- Tailwind CSS for styling
- Zustand for global state
- React Router v7 for routing

## CI/CD

- **Jenkins**: Primary CI/CD defined in `Jenkinsfile` - handles build, test, docker image creation, and K8s deployment
- **.cnb.yml**: Cloud Native Build pipeline for automated tagging and multi-service builds
- **Workflows**: GitHub Actions for CI on PR/push to main

## Important Notes

- The `qing-frontend/` has its own README with Vite/React guidance
- Each microservice has its own `application.yaml` for configuration
- LangChain4j integration exists in qing-service-llm for AI features (Assistant interface defined but implementation pending)
