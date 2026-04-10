## Why

创建一个AI原生极简个人博客系统NexusBlog，满足开发者对简洁、高效个人博客的需求。当前市面上博客系统过于复杂臃肿，需要轻量级、易部署、易于AI辅助开发和维护的解决方案。

## What Changes

- 创建全新的前后端分离博客系统，包含后端API服务和前端展示应用
- 实现基于H2文件数据库的持久化存储方案
- 实现用户认证和文章管理的核心功能
- 实现前台文章展示和Markdown渲染功能

## Capabilities

### New Capabilities

- `user-authentication`: 后台管理系统用户名密码登录功能，无复杂权限体系
- `article-management`: 文章CRUD功能，包括标题、Markdown内容、分类、标签、创建/更新时间
- `article-display`: 前台展示功能，包括首页文章列表和文章详情页Markdown渲染
- `data-persistence`: 基于H2文件数据库的数据持久化，确保重启后数据不丢失

### Modified Capabilities

- 无

## Impact

- 新增后端Spring Boot单体应用（Java 21 + Spring Boot 3.2.x + Spring Data JPA）
- 新增前端Vue 3单体应用（Vue 3 + Vite + Element Plus）
- 使用H2文件数据库存储数据
- 后期规划通过GitHub Actions + Docker Compose部署到云服务器
