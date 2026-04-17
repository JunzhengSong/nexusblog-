## Why

为了提升NexusBlog的内容管理效率，实现与GitHub仓库的文档自动同步，减少手动复制粘贴文章的工作量，支持批量管理和同步Markdown格式的博客内容，让用户可以通过GitHub工作流来管理博客文章，实现内容的版本控制和协作编辑。

## What Changes

- ✅ 新增GitHub仓库配置管理页面，支持配置仓库地址、分支、文档根目录、访问令牌
- ✅ 新增Markdown文档同步功能，支持手动触发一键拉取仓库所有.md文件
- ✅ 新增Markdown元数据解析能力，自动提取文件名作为标题、内容作为正文，支持YAML Front Matter解析分类和标签
- ✅ 新增同步去重机制，通过文件路径+哈希值判断，不重复导入相同文档
- ✅ 新增同步记录管理页面，支持查看同步历史、删除同步文档
- ✅ 同步后的文章与原有文章统一存储，前台无需修改即可正常展示
- ❌ 无破坏性变更，不影响原有功能的正常使用

## Capabilities

### New Capabilities
- `github-repo-config`: GitHub仓库配置管理，支持增删改查仓库配置信息
- `markdown-sync-engine`: Markdown文档同步引擎，负责拉取GitHub仓库文件、解析内容、去重、入库
- `sync-record-management`: 同步记录管理，支持查看同步历史、管理同步的文档
- `markdown-metadata-parser`: Markdown元数据解析器，支持YAML Front Matter解析，提取标题、分类、标签等信息

### Modified Capabilities
- 无，所有变更均为新增功能，不修改原有功能的需求规范

## Impact

- 后端新增4个API接口组，新增3个数据模型
- 前端新增3个页面：GitHub配置页、同步记录页、同步详情页
- 新增依赖：GitHub API客户端、Markdown Front Matter解析库
- 对原有文章展示逻辑无影响，同步后的文章与原有文章使用相同的数据结构
