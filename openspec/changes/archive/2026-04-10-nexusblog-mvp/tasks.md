## 1. Project Initialization

- [x] 1.1 Create monorepo directory structure (backend/, frontend/, docker/, .github/)
- [x] 1.2 Initialize Git repository and create .gitignore
- [x] 1.3 Create README.md with project overview and setup instructions

## 2. Backend Setup

- [x] 2.1 Create Spring Boot 3.2.x project with Java 21
- [x] 2.2 Add dependencies (Spring Data JPA, H2, Spring Security, Validation, Lombok)
- [x] 2.3 Configure application.yml (server port, database, CORS)
- [x] 2.4 Create standard package structure (config, controller, service, repository, entity, dto, exception)
- [x] 2.5 Configure H2 file database with MySQL compatibility mode

## 3. Data Model Implementation

- [x] 3.1 Create User entity (id, username, password, createdAt)
- [x] 3.2 Create Category entity (id, name)
- [x] 3.3 Create Tag entity (id, name)
- [x] 3.4 Create Article entity (id, title, content, category, tags, createdAt, updatedAt)
- [x] 3.5 Create JPA repositories for all entities
- [x] 3.6 Create DTOs for API requests and responses (ArticleDTO, CategoryDTO, TagDTO, UserDTO)
- [x] 3.7 Create Mapper utilities to convert entities to DTOs

## 4. Security and Authentication

- [x] 4.1 Configure Spring Security (session management, CSRF, CORS)
- [x] 4.2 Implement BCrypt password encryption
- [x] 4.3 Create UserDetailService for authentication
- [x] 4.4 Create AuthController with login endpoint
- [x] 4.5 Implement authentication DTOs (LoginRequest, LoginResponse)
- [x] 4.6 Configure security filter chain (public routes, protected routes)

## 5. Backend API - Article Management

- [x] 5.1 Create ArticleController with RESTful endpoints
- [x] 5.2 Implement ArticleService with CRUD operations
- [x] 5.3 Add validation for article creation and updates
- [x] 5.4 Implement GET /api/v1/articles (list with sorting)
- [x] 5.5 Implement GET /api/v1/articles/{id} (single article)
- [x] 5.6 Implement POST /api/v1/articles (create)
- [x] 5.7 Implement PUT /api/v1/articles/{id} (update)
- [x] 5.8 Implement DELETE /api/v1/articles/{id} (delete)
- [x] 5.9 Add exception handling for 404 and validation errors

## 6. Backend API - Category and Tag Management

- [x] 6.1 Create CategoryController
- [x] 6.2 Implement GET /api/v1/categories endpoint
- [x] 6.3 Create TagController
- [x] 6.4 Implement GET /api/v1/tags endpoint

## 7. Backend API - Health Check

- [x] 7.1 Create HealthController
- [x] 7.2 Implement GET /api/v1/health endpoint
- [x] 7.3 Return system status and database connection status

## 8. Frontend Setup

- [x] 8.1 Create Vue 3 + Vite project
- [x] 8.2 Install dependencies (Vue Router, Pinia, Element Plus, Axios, and markdown-it, highlight.js)
- [x] 8.3 Configure Vite (proxy, build options)
- [x] 8.4 Create frontend directory structure (api, components, views, router, stores, utils, styles)
- [x] 8.5 Configure Element Plus
- [x] 8.6 Configure Axios instance with base URL and interceptors

## 9. Frontend - Router and State Management

- [x] 9.1 Configure Vue Router with all routes
- [x] 9.2 Create Pinia stores for user authentication
- [x] 9.3 Create Pinia stores for article management
- [x] 9.4 Implement route guards for admin routes

## 10. Frontend - API Client

- [x] 10.1 Create API client for authentication
- [x] 10.2 Create API client for articles
- [x] 10.3 Create API client for categories and tags
- [x] 10.4 Implement error handling and toast notifications

## 11. Frontend - Public Pages

- [x] 11.1 Create homepage layout
- [x] 11.2 Create ArticleList component (public)
- [x] 11.3 Implement article card component with category and tags
- [x] 11.4 Create ArticleDetail component with Markdown rendering
- [x] 11.5 Implement Markdown rendering with syntax highlighting
- [x] 11.6 Create 404 Not Found page

## 12. Frontend - Admin Authentication

- [x] 12.1 Create admin login page layout
- [x] 12.2 Implement login form with validation
- [x] 12.3 Handle login success and session management
- [x] 12.4 Implement logout functionality

## 13. Frontend - Admin Layout

- [x] 13.1 Create admin dashboard layout with sidebar
- [x] 13.2 Implement navigation menu
- [x] 13.3 Create admin homepage with basic statistics

## 14. Frontend - Admin Article Management

- [x] 14.1 Create ArticleAdminList component
- [x] 14.2 Implement article table with actions (edit, delete)
- [x] 14.3 Create ArticleForm component for creating/editing
- [x] 14.4 Implement Markdown editor with preview
- [x] 14.5 Implement category and tag selection
- [x] 14.6 Add form validation and error handling
- [x] 14.7 Implement article deletion with confirmation

## 15. Frontend - Styling and Polish

- [x] 15.1 Apply consistent styling across all pages
- [x] 15.2 Create responsive design for mobile and desktop
- [x] 15.3 Add loading states and skeletons
- [x] 15.4 Implement toast notification system
- [x] 15.5 Add error pages and proper error messaging

## 16. Testing and Integration

- [x] 16.1 Test backend API endpoints manually (curl or Postman)
- [x] 16.2 Test user authentication flow
- [x] 16.3 Test article CRUD operations end-to-end
- [x] 16.4 Test frontend-backend integration
- [x] 16.5 Test Markdown rendering and syntax highlighting
- [x] 16.6 Test data persistence across application restarts
- [x] 16.7 Verify CORS configuration

## 17. Documentation

- [x] 17.1 Update README with setup and deployment instructions
- [x] 17.2 Document API endpoints
- [x] 17.3 Create CONTRIBUTING.md
- [x] 17.4 Add code comments for complex logic

## 18. Docker and Deployment Preparation

- [x] 18.1 Create Dockerfile for backend
- [x] 18.2 Create Dockerfile for frontend
- [x] 18.3 Create docker-compose.yml
- [x] 18.4 Create .dockerignore files
- [x] 18.5 Document deployment process (for future GitHub Actions setup)
