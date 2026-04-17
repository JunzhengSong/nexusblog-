## Why

当前系统使用H2作为开发数据库，无法满足生产环境的需求。H2是内存数据库，数据持久化能力有限，且无法支持高并发场景。为了使NexusBlog能够部署到生产环境并支持真正的业务需求，需要迁移到MySQL数据库。同时，数据库凭证等敏感信息需要通过环境变量注入，避免直接硬编码在代码中并推送到GitHub，这是企业级应用的安全最佳实践。

## What Changes

- **添加MySQL数据库依赖**：在Spring Boot项目中添加MySQL JDBC驱动、MyBatis-Plus相关依赖
- **移除现有ORM框架**：移除Spring Data JPA相关依赖和配置
- **配置MyBatis-Plus**：使用MyBatis-Plus作为ORM框架，配置数据源和MyBatis-Plus相关设置
- **环境变量配置**：使用环境变量配置数据库连接信息（URL、用户名、密码）
- **创建MySQL配置文件**：新增application-mysql.yml配置文件，包含MySQL特定的配置
- **转换JPA实体到MyBatis-Plus**：将现有JPA实体转换为MyBatis-Plus的BaseEntity模式
- **转换Repository**：将JPA Repository接口转换为MyBatis-Plus的Mapper接口
- **转换Service层**：调整Service层以适配MyBatis-Plus的API
- **添加环境变量文档**：创建README或文档说明所需的环境变量
- **测试验证**：编写并执行测试验证MySQL连接和CRUD操作

## Capabilities

### New Capabilities
- `mysql-database`: MySQL数据库集成能力，包括连接配置、MyBatis-Plus映射和基本CRUD操作
- `mybatis-plus-persistence`: MyBatis-Plus持久化框架，提供单表CRUD、分页、条件构造等功能

### Modified Capabilities
- 无（仅更换数据库和ORM实现，不改变业务逻辑和API行为）

## Impact

- **后端依赖**：pom.xml需要添加MySQL和MyBatis-Plus依赖，移除JPA相关依赖
- **配置文件**：application.yml需要支持环境变量替换，新增application-mysql.yml
- **实体类**：现有JPA实体需要转换为MyBatis-Plus兼容的实体模式
- **数据访问层**：Repository接口需要转换为MyBatis-Plus Mapper接口
- **Service层**：需要适配MyBatis-Plus的API调用方式
- **测试**：需要创建集成测试验证MySQL连接和数据操作
- **部署**：部署时需要配置MySQL相关环境变量
