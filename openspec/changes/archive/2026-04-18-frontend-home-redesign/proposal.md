## Why

当前首页视觉层次单一，仅包含标题和管理后台入口，缺乏博客应有的阅读体验和内容预览。重构为极简主义风格，突出文章标题和摘要，提供分类筛选能力。

## What Changes

- 极简主义首页布局，中心化内容展示
- 顶部 slogan 和分类 Tab 筛选栏
- 文章卡片显示标题、前 120 字符摘要、日期、标签
- 响应式布局（桌面端优先，能自适应但不单独优化移动端）
- 移除 ArticleCard 组件，改用内联卡片设计

## Capabilities

### New Capabilities

- `public-home-page`: 面向游客的公开首页，展示文章列表和分类筛选

### Modified Capabilities

- (无)

## Impact

- `frontend/src/views/Home.vue` - 重构首页组件
- `frontend/src/components/ArticleCard.vue` - 移除或保留备用
- (无后端影响)
- (无 API 变更)
