## Context

当前NexusBlog系统使用H2内存数据库和Spring Data JPA作为ORM框架。H2数据库适合开发环境，但无法满足生产环境的需求，包括数据持久化、高并发性能、数据备份恢复等。同时，Spring Data JPA虽然功能强大，但相对厚重，对于CRUD为主的业务场景，MyBatis-Plus提供更轻量级且高效的解决方案。

系统需要迁移到MySQL数据库，并采用MyBatis-Plus作为ORM框架。MySQL作为成熟的关系型数据库，能够满足生产环境的需求。MyBatis-Plus在MyBatis基础上增强了CRUD能力，提供了类似Spring Data JPA的便捷API，同时保留了SQL的灵活性。

## Goals / Non-Goals

**Goals:**
- 将数据存储从H2迁移到MySQL，支持生产环境部署
- 使用MyBatis-Plus替换Spring Data JPA，提供高效的ORM操作
- 通过环境变量注入数据库凭证，确保安全性
- 保持现有业务功能不变，实现平滑迁移
- 配置数据库连接池和性能优化参数
- 提供完整的测试覆盖，确保迁移的正确性

**Non-Goals:**
- 不改变现有API接口和业务逻辑
- 不引入复杂的分布式事务处理
- 不进行数据库读写分离配置
- 不实现复杂的SQL优化（仅在必要时）

## Decisions

### 1. 使用MyBatis-Plus而非原生MyBatis或JPA

**选择理由：**
- MyBatis-Plus提供了内置的CRUD操作，减少重复代码
- 提供丰富的条件构造器，支持链式调用
- 支持代码生成器，可快速生成实体、Mapper、Service
- 保留了MyBatis的SQL灵活性，复杂查询仍可编写SQL
- 相比JPA，SQL更可控，避免N+1查询等问题

**替代方案考虑：**
- Spring Data JPA：功能强大但学习曲线陡峭，SQL不够透明
- 原生MyBatis：需要手写大量CRUD SQL，开发效率低
- JOOQ：类型安全但配置复杂，学习成本高

### 2. 使用环境变量配置数据库连接

**选择理由：**
- 避免敏感信息硬编码在代码或配置文件中
- 符合12-Factor App的最佳实践
- 便于不同环境（dev、test、prod）的配置管理
- 与云原生和容器化部署方式兼容

**实现方式：**
- 使用Spring Boot的@Value注解或Environment接口
- 配置文件中使用${ENV_VAR:default_value}语法
- 提供环境变量文档，指导运维配置

### 3. 使用HikariCP作为连接池

**选择理由：**
- Spring Boot 2.x默认连接池，性能优秀
- 轻量级、快速、稳定
- 配置简单，监控功能完善

**替代方案考虑：**
- Druid：功能丰富但相对厚重
- DBCP2：性能略逊于HikariCP

### 4. 使用Flyway管理数据库迁移

**选择理由：**
- 提供数据库版本控制
- 支持团队协作，避免数据库结构冲突
- 回滚和审计功能完善
- 与Spring Boot集成良好

**替代方案考虑：**
- Liquibase：功能类似但XML配置相对复杂
- 手动管理：容易出错，无法版本控制

### 5. 实体类设计采用BaseEntity继承模式

**选择理由：**
- 统一管理id、createTime、updateTime等审计字段
- 配合MyBatis-Plus的自动填充功能
- 减少重复代码，保持代码整洁

**字段设计：**
- id: 主键（使用Snowflake或UUID，根据业务需求）
- createTime: 创建时间，自动填充
- updateTime: 更新时间，自动填充
- deleted: 逻辑删除标记，可选

## Risks / Trade-offs

### 风险及缓解措施

**风险1: 现有JPA实体转换为MyBatis-Plus实体可能出现字段映射问题**
**缓解措施:**
- 仔细分析现有实体结构，确保字段类型和名称正确映射
- 使用MyBatis-Plus的@TableField注解明确字段映射
- 编写详细的测试验证字段映射的正确性

**风险2: Service层代码需要大量修改，可能引入bug**
**缓解措施:**
- 逐步迁移，先迁移简单实体，再迁移复杂实体
- 保持Service层接口不变，修改实现类
- 编写全面的测试覆盖，确保功能等价

**风险3: 生产环境部署时环境变量配置错误导致连接失败**
**缓解措施:**
- 提供详细的环境变量配置文档
- 实现启动时的连接检查和友好的错误提示
- 在CI/CD流程中添加环境变量验证

**风险4: 性能可能不如预期**
**缓解措施:**
- 配置合理的连接池参数
- 对关键SQL进行性能测试和优化
- 考虑添加适当的数据库索引

### 权衡考虑

**权衡1: 开发效率 vs 性能**
- 使用MyBatis-Plus提高开发效率，可能放弃一些底层SQL
- 缓解措施：对性能关键场景仍可使用自定义SQL

**权衡2: 功能完整性 vs 复杂度**
- 保持现有功能，不引入新特性
- 避免过度设计，聚焦迁移本身

## Migration Plan

### 迁移步骤

1. **准备阶段**
   - 备份现有H2数据库数据
   - 准备MySQL数据库环境
   - 配置开发、测试环境的环境变量

2. **依赖替换阶段**
   - 在pom.xml中移除Spring Data JPA依赖
   - 添加MySQL驱动、MyBatis-Plus、HikariCP、Flyway依赖
   - 配置MyBatis-Plus和Flyway插件

3. **配置文件改造**
   - 创建application-mysql.yml配置文件
   - 配置数据源、MyBatis-Plus、Flyway相关设置
   - 使用环境变量占位符

4. **代码迁移阶段**
   - 创建BaseEntity基类
   - 创建MyBatis-Plus配置类（配置自动填充等）
   - 逐个迁移JPA实体为MyBatis-Plus实体
   - 逐个将Repository接口转换为Mapper接口
   - 修改Service层代码适配MyBatis-Plus API

5. **数据库迁移脚本**
   - 使用Flyway创建V1__init.sql脚本
   - 根据现有实体定义创建表结构
   - 包含必要的索引和约束

6. **测试阶段**
   - 编写单元测试验证CRUD操作
   - 编写集成测试验证端到端功能
   - 运行现有测试套件确保功能不变
   - 性能测试对比迁移前后

7. **文档更新**
   - 更新README说明数据库配置
   - 创建环境变量配置文档
   - 更新部署文档

8. **部署阶段**
   - 在测试环境部署验证
   - 准备生产环境部署
   - 制定回滚方案

### 回滚策略

如果迁移过程中出现严重问题：

1. **开发环境回滚**
   - 使用Git回滚到迁移前的代码版本
   - 恢复原来的H2数据库配置

2. **生产环境回滚**
   - 停止新版本应用
   - 回滚数据库schema（使用Flyway的回滚功能）
   - 重新部署上一版本应用

## Open Questions

1. **主键生成策略**
   - 问题：使用自增ID还是Snowflake算法？
   - 决策：暂定使用自增ID，简化实现
   - 后续：如需要分布式ID再考虑Snowflake

2. **逻辑删除**
   - 问题：是否需要在BaseEntity中添加deleted字段支持逻辑删除？
   - 决策：暂不添加，保持简单
   - 后续：根据业务需求再考虑

3. **时间字段类型**
   - 问题：使用LocalDateTime还是Long时间戳？
   - 决策：使用LocalDateTime，便于阅读和数据库存储
   - 后续：根据性能表现评估

4. **Flyway脚本位置**
   - 问题：脚本放在resources/db/migration还是其他位置？
   - 决策：按Flyway约定放在resources/db/migration
   - 后续：根据项目结构调整

## Testing Strategy

### 单元测试
- 测试MyBatis-Plus基础CRUD操作
- 测试条件构造器
- 测试自动填充功能
- 测试分页查询

### 集成测试
- 测试数据库连接
- 测试完整的业务流程
- 测试事务处理
- 测试并发场景

### 性能测试
- 对比迁移前后CRUD性能
- 测试连接池性能
- 测试复杂查询性能

### 测试覆盖要求
- 核心业务测试覆盖率 > 80%
- 所有新增功能必须有测试覆盖
- 修复的bug必须有回归测试
