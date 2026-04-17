## ADDED Requirements

### Requirement: MyBatis-Plus基础配置
系统SHALL正确配置MyBatis-Plus框架，包括Mapper扫描、类型别名等基础配置。

#### Scenario: Mapper接口扫描配置
- **GIVEN** 应用启动且MyBatis-Plus配置正确
- **WHEN** 系统扫描Mapper接口
- **THEN** 所有MyBatis-Plus Mapper接口被正确扫描和注册

#### Scenario: 实体类类型别名配置
- **GIVEN** MyBatis-Plus已配置
- **WHEN** 使用实体类
- **THEN** 可以通过类名简单引用实体类无需全限定名

### Requirement: 实体类BaseEntity支持
系统SHALL提供BaseEntity基类，包含通用的审计字段（id、createTime、updateTime等）。

#### Scenario: 实体类继承BaseEntity
- **GIVEN** 创建新的实体类
- **WHEN** 实体类继承BaseEntity
- **THEN** 实体类自动拥有id、createTime、updateTime等字段
- **AND** 这些字段在数据库操作时自动填充

#### Scenario: 审计字段自动填充
- **GIVEN** 实体继承BaseEntity
- **WHEN** 插入新记录
- **THEN** createTime字段自动填充为当前时间
- **WHEN** 更新记录
- **THEN** updateTime字段自动填充为当前时间

### Requirement: 单表CRUD操作
系统SHALL通过MyBatis-Plus提供完整的单表CRUD操作能力，包括插入、更新、删除、查询。

#### Scenario: 插入单条记录
- **GIVEN** Mapper继承BaseMapper
- **WHEN** 调用insert方法插入实体
- **THEN** 数据成功插入到数据库
- **AND** 返回影响行数为1

#### Scenario: 根据ID查询
- **GIVEN** 数据库中存在id为1的记录
- **WHEN** 调用selectById方法查询
- **THEN** 返回正确的实体对象
- **AND** 对象的id字段值为1

#### Scenario: 条件查询
- **GIVEN** 数据库中存在多条记录
- **WHEN** 使用QueryWrapper构建条件查询
- **THEN** 返回符合查询条件的记录列表
- **AND** SQL条件正确应用到查询中

#### Scenario: 更新记录
- **GIVEN** 数据库中存在id为1的记录
- **WHEN** 调用updateById方法更新实体
- **THEN** 记录更新成功
- **AND** updateTime字段自动更新

#### Scenario: 删除记录
- **GIVEN** 数据库中存在id为1的记录
- **WHEN** 调用deleteById方法删除
- **THEN** 记录从数据库中删除
- **AND** 返回影响行数为1

### Requirement: 分页查询支持
系统SHALL通过MyBatis-Plus提供分页查询能力。

#### Scenario: 分页查询列表
- **GIVEN** 数据库中存在多条记录
- **WHEN** 使用IPage对象查询第1页，每页10条
- **THEN** 返回包含记录、总数、总页数的分页信息
- **AND** 当前页记录数不超过10条

### Requirement: 复杂条件查询
系统SHALL支持通过QueryWrapper和LambdaQueryWrapper构建复杂的数据库查询条件。

#### Scenario: 多条件组合查询
- **GIVEN** 需要查询status=1且createTime>某个日期的记录
- **WHEN** 使用QueryWrapper构建多个条件
- **THEN** SQL正确生成WHERE子句
- **AND** 返回符合所有条件的记录

#### Scenario: 模糊查询
- **GIVEN** 需要查询name包含"test"的记录
- **WHEN** 使用QueryWrapper的like方法
- **THEN** SQL使用LIKE模糊匹配
- **AND** 返回包含"test"的记录

### Requirement: 代码生成器配置（可选）
系统SHALL配置MyBatis-Plus代码生成器，用于快速生成实体、Mapper、Service等代码。

#### Scenario: 代码生成器可用
- **GIVEN** 配置了代码生成器
- **WHEN** 运行代码生成器
- **THEN** 自动生成实体、Mapper、Service等代码文件
- **AND** 生成的代码符合项目规范
