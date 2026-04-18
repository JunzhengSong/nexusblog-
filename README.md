# NexusBlog

An AI-native minimalist personal blog system.

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.2.x
- MyBatis-Plus 3.5.7
- MySQL 8.0+
- Spring Security

### Frontend
- Vue 3
- Vite
- Element Plus
- Vue Router
- Pinia
- Axios

## Architecture

Monorepo structure with frontend-backend separation:

```
nexusblog/
├── backend/           # Spring Boot API server
├── frontend/          # Vue 3 SPA
├── docker/            # Docker configuration
├── openspec/          # OpenSpec change management
└── .github/           # CI/CD configuration
```

## Quick Start

### Prerequisites
- JDK 21
- Node.js 18+
- Maven (or use backend/mvnw)
- MySQL 8.0+

### Backend Setup
```bash
cd backend
./mvnw spring-boot:run
```

**配置环境变量：**
在运行前，需要配置MySQL数据库连接信息。请参考 [ENV_VARIABLES.md](ENV_VARIABLES.md) 文档。

**快速本地配置示例：**
在 `backend` 目录下创建 `.env` 文件：
```bash
DB_URL=jdbc:mysql://localhost:3306/nexusblog?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

Backend runs on `http://localhost:8080`

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:5173`

## Default Credentials

**Username:** `admin`
**Password:** `admin123`

*Note: Please change the default password after first login.*

## Features

- User authentication (simple username/password)
- Article CRUD with Markdown support
- Categories and tags
- Public article listing and detail view
- Admin dashboard
- Data persistence (MySQL database with MyBatis-Plus)
- Environment variable configuration

## API Documentation

### Authentication
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/logout` - User logout

### Articles
- `GET /api/v1/articles` - List articles (public)
- `GET /api/v1/articles/{id}` - Get article details (public)
- `POST /api/v1/articles` - Create article (auth required)
- `PUT /api/v1/articles/{id}` - Update article (auth required)
- `DELETE /api/v1/articles/{id}` - Delete article (auth required)

### Categories & Tags
- `GET /api/v1/categories` - List categories (public)
- `GET /api/v1/tags` - List tags (public)

### Health
- `GET /api/v1/health` - System health check (public)

## Development

### Adding Dependencies

**Backend (Maven):**
Edit `backend/pom.xml`

**Frontend (npm):**
```bash
cd frontend
npm install <package>
```

## License

MIT
