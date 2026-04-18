# CLAUDE.md

此文件为 Claude Code (claude.ai/code) 在此仓库中工作时提供指导。

## 项目结构

前后端分离的 monorepo：

- `backend/` - Spring Boot 3.2.x API 服务器 (Java 21)
- `frontend/` - Vue 3 SPA（使用 Vite）
- `.env` - 环境变量（需创建此文件配置数据库）

## 开发命令

### 后端 (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run          # 启动后端，端口 8080
./mvnw test                     # 运行测试
./mvnw clean install            # 构建
```

**数据库配置**：在项目根目录创建 `.env` 文件：

```bash
DB_URL=jdbc:mysql://localhost:3306/nexusblog?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=your_password
```

### 前端 (Vue 3)

```bash
cd frontend
npm install                    # 安装依赖
npm run dev                    # 启动开发服务器，端口 5173
npm run build                  # 生产环境构建
npm run preview                 # 预览生产构建
```

## 架构概览

### 后端 (Spring Boot)

- **包结构**：`com.nexusblog.{controller,service,mapper,entity,dto,config,util}`
- **ORM**：MyBatis-Plus，自动配置（通过 `@MapperScan` 扫描 mapper）
- **安全**：Spring Security，使用自定义 `CustomUserDetailsService`
- **数据库**：MySQL，Flyway 迁移脚本位于 `src/main/resources/db/migration/`
- **DTO 模式**：请求/响应使用独立的 DTO，通过 `DtoMapper` 转换
- **核心功能**：文章 CRUD、分类、标签、GitHub Markdown 同步

### 前端 (Vue 3)

- **路由**：Vue Router，配置文件 `src/router/index.js`
- **状态管理**：Pinia stores，位于 `src/stores/`
- **UI 框架**：Element Plus 组件库
- **API 代理**：Vite 将 `/api` 代理到 `http://localhost:8080`
- **核心功能**：公开博客、管理后台、GitHub 同步界面

## 数据库迁移

- 位置：`backend/src/main/resources/db/migration/`
- 命名：`V{版本}__描述.sql`
- Flyway 默认禁用（`flyway.enabled: false`）
- 手动迁移：在 `application.yml` 中设置 `flyway.enabled: true`

## API 端点

- `POST /api/v1/auth/login` - 认证
- `GET/POST/PUT/DELETE /api/v1/articles` - 文章 CRUD
- `GET /api/v1/categories`、`/api/v1/tags` - 分类和标签
- `GET /api/v1/health` - 健康检查

## 默认凭据

- 用户名：`admin`
- 密码：`admin123`

## 关键配置文件

- `backend/src/main/resources/application.yml` - 后端配置
- `frontend/vite.config.js` - 前端构建配置
- `.env` - 环境变量（不提交到版本控制）
