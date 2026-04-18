## Context

当前后端 8 个 Controller 返回格式混乱：
- `ArticleController` → `ResponseEntity<List<ArticleDTO>>`
- `AuthController` → `ResponseEntity<LoginResponse>` 和 `ResponseEntity<String>`
- `GithubSyncController` → `ResponseEntity<Map<String, Object>>` 和 `ResponseEntity<List<>>`
- `GlobalExceptionHandler` → `ErrorResponse` 格式（timestamp, status, error, message）

前端 axios 直接透传 response，各页面自行从 `res.data` 提取数据。

## Goals / Non-Goals

**Goals:**
- 后端所有成功响应统一为 `{ code: 200, data: <payload>, message: "success" }`
- 后端所有错误响应统一为 `{ code: <errCode>, data: null, message: <errMsg> }`
- 前端统一拦截处理，所有 API 调用直接获取 `res.data.data` 为业务数据
- 错误码标准化（200=成功, 400=参数错误, 401=未授权, 403=禁止, 404=不存在, 500=服务器错误）

**Non-Goals:**
- 不改变 HTTP 状态码语义（仍使用 200, 201, 204, 400, 401, 403, 404, 500）
- 不实现复杂的错误码体系，只定义基础错误码
- 不修改前端路由或页面逻辑，只修改 API 调用层

## Decisions

### 1. ApiResult<T> 泛型包装类

```java
// backend/src/main/java/com/nexusblog/common/ApiResult.java
@Data
public class ApiResult<T> {
    private int code;
    private T data;
    private String message;

    public static <T> ApiResult<T> ok(T data) { ... }
    public static <T> ApiResult<T> ok(T data, String msg) { ... }
    public static <T> ApiResult<T> error(IErrorCode errorCode) { ... }
    public static <T> ApiResult<T> error(int code, String msg) { ... }
}
```

**Why**: 泛型保证类型安全，静态工厂方法简化调用

### 2. IErrorCode 错误码接口

```java
public interface IErrorCode {
    int getCode();
    String getMessage();
}
```

**Why**: 定义标准错误码常量，便于维护和扩展

### 3. 前端统一响应处理

```javascript
// api/index.js
api.interceptors.response.use(
  response => {
    const { code, data, message } = response.data
    if (code !== 200) {
      ElMessage.error(message)
      return Promise.reject(new Error(message))
    }
    return data  // 直接返回 data 字段，前端直接用
  },
  error => { /* 错误处理 */ }
)
```

**Why**: 前端每个调用者直接获取业务数据，无需手动解包

## Risks / Trade-offs

| 风险 | 影响 |  Mitigation |
|------|------|------------|
| 前端大量 Vue 组件需适配 | BREAKING CHANGE | 统计所有使用处，一次性修改 |
| 已上线 API 格式变更 | 线上前端需同步更新 | 确保 PR 一起合并 |

## Open Questions

无
