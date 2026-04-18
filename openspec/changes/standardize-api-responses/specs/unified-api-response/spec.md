## ADDED Requirements

### Requirement: 成功响应格式
后端所有成功的 API 响应 SHALL 使用统一的 ApiResult 格式，code=200。

#### Scenario: 单条实体响应
- **WHEN** 客户端请求单个资源（如 GET /api/v1/articles/1）
- **THEN** 响应体 SHALL 为 `{ "code": 200, "data": { entity }, "message": "success" }`

#### Scenario: 列表响应
- **WHEN** 客户端请求集合（如 GET /api/v1/articles）
- **THEN** 响应体 SHALL 为 `{ "code": 200, "data": [ entities ], "message": "success" }`

#### Scenario: 创建操作
- **WHEN** 客户端创建资源（如 POST /api/v1/articles）
- **THEN** 响应体 SHALL 为 `{ "code": 200, "data": { created entity }, "message": "success" }`

#### Scenario: 删除操作
- **WHEN** 客户端删除资源（如 DELETE /api/v1/articles/1）
- **THEN** 响应体 SHALL 为 `{ "code": 204, "data": null, "message": "删除成功" }`

### Requirement: 错误响应格式
后端所有错误响应 SHALL 使用统一的 ApiResult 格式，code 非 200。

#### Scenario: 参数验证错误
- **WHEN** 请求参数无效
- **THEN** 响应体 SHALL 为 `{ "code": 400, "data": null, "message": "参数错误信息" }`

#### Scenario: 资源不存在
- **WHEN** 请求的资源不存在
- **THEN** 响应体 SHALL 为 `{ "code": 404, "data": null, "message": "资源不存在" }`

#### Scenario: 未授权访问
- **WHEN** 客户端未认证
- **THEN** 响应体 SHALL 为 `{ "code": 401, "data": null, "message": "未授权，请重新登录" }`

#### Scenario: 服务器内部错误
- **WHEN** 服务器发生未预期错误
- **THEN** 响应体 SHALL 为 `{ "code": 500, "data": null, "message": "服务器内部错误" }`

### Requirement: 前端响应处理
前端 API 层 SHALL 自动解包 ApiResult 并返回业务数据给调用方。

#### Scenario: 请求成功
- **WHEN** API 返回 `{ "code": 200, "data": [items], "message": "success" }`
- **THEN** axios 拦截器 SHALL 直接返回 `[items]`
- **AND** 调用方 SHALL 直接获取数据，无需手动解包

#### Scenario: 请求失败
- **WHEN** API 返回 `{ "code": 400, "data": null, "message": "参数错误" }`
- **THEN** axios 拦截器 SHALL 通过 ElMessage 显示错误信息
- **AND** 返回被拒绝的 Promise 并携带错误信息
