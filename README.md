# NexusBlog

An AI-native minimalist personal blog system.

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.2.x
- MyBatis-Plus 3.5.5
- MySQL 8.0+
- Flyway (database migration)
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
└── .github/           # CI/CD configuration
```

## Quick Start

### Prerequisites
- JDK 21
- Node.js 18+
- Maven (or use backend/mvnw)
- MySQL 8.0+ (or MariaDB 10.5+)

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

- ✅ User authentication (simple username/password)
- ✅ Article CRUD with Markdown support
- ✅ Categories and tags
- ✅ Public article listing and detail view
- ✅ Admin dashboard
- ✅ Data persistence (MySQL database with MyBatis-Plus)
- ✅ Database migrations (Flyway)
- ✅ Environment variable configuration

## API Documentation

### Authentication
- `POST /api/v1/auth/login` - User login

### Articles
- `GET /api/v1/articles` - List articles
- `GET /api/v1/articles/{id}` - Get article details
- `POST /api/v1/articles` - Create article
- `PUT /api/v1/articles/{id}` - Update article
- `DELETE /api/v1/articles/{id}` - Delete article

### Categories & Tags
- `GET /api/v1/categories` - List categories
- `GET /api/v1/tags` - List tags

### Health
- `GET /api/v1/health` - System health check

## Development

### Adding Dependencies

**Backend (Maven):**
Edit `backend/pom.xml`

**Frontend (npm):**
```bash
cd frontend
npm install <package>
```

### Database

The H2 database file is stored in the `data/` directory. Database console is available at:
```
http://localhost:8080/h2-console
```

Connection settings:
- JDBC URL: `jdbc:h2:file:./data/nexusblog;MODE=MySQL`
- User: `sa`
- Password: (empty)

## License

MIT
