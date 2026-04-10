## Context

开发一个AI原生极简个人博客系统NexusBlog，采用前后端分离架构。项目从零开始，没有现有代码基础。

**技术栈约束：**
- 后端：Java 21 + Spring Boot 3.2.x + Spring Data JPA
- 数据库：H2 (文件模式)
- 前端：Vue 3 + Vite + Element Plus
- 架构：前后端分离两个单体应用（不使用微服务）
- 部署：后期通过GitHub Actions + Docker Compose部署到云服务器

**利益相关者：**
- 项目所有者（用户）：负责需求确认和验收
- AI开发者（我）：全程主导开发

## Goals / Non-Goals

**Goals:**
- 实现简洁高效的个人博客核心功能
- 提供易于AI辅助开发和维护的代码结构
- 实现MVP版本的核心功能（用户认证、文章CRUD、前台展示、数据持久化）
- 使用轻量级技术栈，便于部署和维护

**Non-Goals:**
- 复杂的权限管理系统（仅简单用户名密码登录）
- 评论系统、点赞、分享等社交功能
- 多用户/多作者支持
- 复杂的搜索和筛选功能
- 高并发优化（针对个人博客场景）

## Decisions

### 1. 项目结构

**决定：** 采用monorepo结构，后端和前端在同一仓库中

**理由：**
- 便于统一版本管理和CI/CD配置
- 个人博客项目规模较小，monorepo管理简单
- 便于AI理解整体项目结构

**目录结构：**
```
nexusblog/
├── backend/           # Spring Boot后端应用
├── frontend/          # Vue 3前端应用
├── docker/            # Docker相关文件
└── .github/           # GitHub Actions配置
```

### 2. 后端架构

**决定：** 使用Spring Boot标准分层架构

**分层结构：**
- Controller层：RESTful API接口
- Service层：业务逻辑处理
- Repository层：数据访问（Spring Data JPA）
- Entity层：数据模型（JPA实体）

**包结构：**
```
com.nexusblog/
├── config/           # 配置类（CORS、安全等）
├── controller/       # REST控制器
├── service/          # 业务服务
├── repository/       # 数据仓库
├── entity/           # JPA实体
├── dto/              # 数据传输对象
├── exception/        # 异常处理
└── NexusBlogApplication.java
```

### 3. 数据库配置

**决定：** 使用H2文件数据库，配置为MySQL兼容模式

**配置要点：**
- 连接URL：`jdbc:h2:file:./data/nexusblog;MODE=MySQL;DB_CLOSE_DELAY=-1`
- 启用SQL控制台（开发环境）
- 数据存储在项目目录下的data文件夹

### 4. 数据模型设计

**实体设计：**
- **User**：用户表（id, username, password, createdAt）
- **Article**：文章表（id, title, content, category, tags, createdAt, updatedAt）
- **Category**：分类表（id, name）
- **Tag**：标签表（id, name）

**关系设计：**
- Article ← Category：多对一
- Article ← Tag：多对多

### 5. 前端架构

**决定：** 使用Vue 3 Composition API + Vite

**技术选型：**
- 路由：Vue Router
- 状态管理：Pinia
- UI组件：Element Plus
- Markdown渲染：markdown-it + highlight.js
- HTTP客户端：Axios

**目录结构：**
```
frontend/
├── src/
│   ├── api/           # API调用
│   ├── assets/        # 静态资源
│   ├── components/    # 通用组件
│   ├── composables/   # 组合式函数
│   ├── layouts/       # 布局组件
│   ├── router/        # 路由配置
│   ├── stores/        # Pinia状态
│   ├── styles/        # 样式文件
│   ├── utils/         # 工具函数
│   ├── views/         # 页面组件
│   ├── App.vue
│   └── main.ts
```

### 6. API设计

**决定：** 采用RESTful风格，使用JSON数据格式

**基础路径：** `/api/v1`

**API端点：**
- `POST /api/v1/auth/login` - 用户登录
- `GET /api/v1/articles` - 获取文章列表
- `GET /api/v1/articles/{id}` - 获取文章详情
- `POST /api/v1/articles` - 创建文章
- `PUT /api/v1/articles/{id}` - 更新文章
- `DELETE /api/v1/articles/{id}` - 删除文章
- `GET /api/v1/categories` - 获取分类列表
- `GET /api/v1/tags` - 获取标签列表

### 7. 安全设计

**决定：** 使用基于Session的简单认证，无复杂权限

**实现方式：**
- Spring Security配置
- 用户名密码登录（密码BCrypt加密）
- Session管理（前端存储session cookie）

### 8. 前端路由设计

**决定：** 前后端路由分离，使用Vue Router

**路由配置：**
- `/` - 首页（文章列表）
- `/article/:id` - 文章详情
- `/admin/login` - 后台登录
- `/admin` - 后台管理首页
- `/admin/articles` - 文章管理
- `/admin/articles/create` - 创建文章
- `/admin/articles/edit/:id` - 编辑文章

## Risks / Trade-offs

**风险：**
1. H2数据库在大数据量下性能不足 → **缓解**：MVP阶段数据量小，后续可迁移到MySQL/PostgreSQL
2. Session认证在高并发下扩展性差 → **缓解**：个人博客场景并发量低，后续可升级为JWT
3. 无复杂权限可能存在安全隐患 → **缓解**：管理员账户信息妥善保管，后续可添加RBAC

**权衡：**
1. 简单性 vs 功能性：选择简洁性，MVP阶段专注核心功能
2. 性能 vs 开发效率：选择开发效率，个人博客场景性能要求不高
3. 前后端分离 vs 整体架构：选择前后端分离，便于独立开发和部署

## Migration Plan

**开发阶段：**
1. 后端项目初始化和基础配置
2. 数据模型设计和数据库初始化
3. 后端API开发
4. 前端项目初始化和基础配置
5. 前端页面和组件开发
6. 前后端联调测试

**部署阶段（后期）：**
1. 编写Dockerfile（后端和前端）
2. 编写Docker Compose配置
3. 配置GitHub Actions CI/CD
4. 部署到云服务器

**回滚策略：**
- 使用Git版本控制，可快速回退到稳定版本
- Docker Compose支持版本化部署，便于回滚
