## Why

前后端 API 交互缺乏统一的响应格式标准，导致：
1. 后端各 Controller 返回格式不一致（有的返回 List，有的返回 Map，有的返回分页对象）
2. 前端需要针对每个接口做不同的数据提取逻辑
3. 错误处理和成功响应的结构不统一

统一响应格式可以消除这些不一致，提升代码可维护性和前端开发效率。

## What Changes

### 后端改动
- 新增 `ApiResult<T>` 统一响应包装类，包含 code、data、message 字段
- 新增 `IErrorCode` 错误码接口，定义标准错误码
- 修改 `GlobalExceptionHandler`，返回统一 `ApiResult` 格式
- 修改全部 8 个 Controller，使用 `ApiResult.ok()` 和 `ApiResult.error()` 包装响应

### 前端改动
- 修改 `api/index.js` axios 拦截器，统一处理 `ApiResult` 格式
- 新增统一错误处理和成功提示逻辑
- **BREAKING**: 前端所有使用 `res.data.xxx` 的地方需适配为 `res.data.data.xxx`

## Capabilities

### New Capabilities
- `unified-api-response`: 定义前后端统一的 API 响应格式标准

## Impact

**后端受影响文件：**
- `backend/src/main/java/com/nexusblog/common/ApiResult.java` (新建)
- `backend/src/main/java/com/nexusblog/common/IErrorCode.java` (新建)
- `backend/src/main/java/com/nexusblog/exception/GlobalExceptionHandler.java` (修改)
- `backend/src/main/java/com/nexusblog/controller/*Controller.java` (8个文件修改)

**前端受影响文件：**
- `frontend/src/api/index.js` (修改)
- `frontend/src/api/github.js` (修改)
- `frontend/src/views/admin/*.vue` (使用 github API 的页面需适配)

**API 响应格式变更（BREAKING）：**
```
// 之前
GET /api/v1/articles → List<ArticleDTO>

// 之后
GET /api/v1/articles → { code: 200, data: List<ArticleDTO>, message: "success" }
```
