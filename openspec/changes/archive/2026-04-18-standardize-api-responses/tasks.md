## 1. 后端统一响应类

- [x] 1.1 创建 `common/IErrorCode.java` 错误码接口
- [x] 1.2 创建 `common/ApiResult.java` 统一响应包装类
- [x] 1.3 创建 `common/BusinessException.java` 业务异常类

## 2. 后端全局异常处理

- [x] 2.1 修改 `GlobalExceptionHandler` 返回 ApiResult 格式
- [x] 2.2 添加参数校验异常处理
- [ ] 2.3 测试异常响应格式

## 3. 后端 Controller 修改

- [x] 3.1 修改 `ArticleController` 使用 ApiResult.ok()
- [x] 3.2 修改 `CategoryController` 使用 ApiResult.ok()
- [x] 3.3 修改 `TagController` 使用 ApiResult.ok()
- [x] 3.4 修改 `AuthController` 使用 ApiResult.ok()
- [x] 3.5 修改 `GithubRepoConfigController` 使用 ApiResult.ok()
- [x] 3.6 修改 `GithubSyncController` 使用 ApiResult.ok()
- [x] 3.7 修改 `SyncArticleController` 使用 ApiResult.ok()
- [x] 3.8 修改 `HealthController` 使用 ApiResult.ok()

## 4. 前端 API 层修改

- [x] 4.1 修改 `api/index.js` axios 拦截器解包 ApiResult
- [x] 4.2 修改 `api/github.js` 移除重复的响应处理逻辑

## 5. 前端页面适配

- [x] 5.1 适配 `admin/GithubRepos.vue` 从 res.data 直接获取数据
- [x] 5.2 适配 `admin/GithubSyncHistory.vue` 从 res.data 直接获取数据
- [x] 5.3 适配 `admin/GithubSyncArticles.vue` 从 res.data 直接获取数据

## 6. 验证测试

- [x] 6.1 启动后端服务，测试各 API 响应格式
- [x] 6.2 启动前端服务，验证页面功能正常
- [x] 6.3 提交代码并推送
