# public-home-page Specification

## Purpose
TBD - created by archiving change frontend-home-redesign. Update Purpose after archive.
## Requirements
### Requirement: 首页展示文章列表

首页 SHALL 显示所有已发布文章的列表，按创建时间倒序排列。每篇文章显示：标题、120字符摘要（纯文本）、发布日期、标签（最多3个）。

#### Scenario: 显示文章列表
- **WHEN** 用户访问首页
- **THEN** 系统按创建时间倒序显示所有文章，每篇显示标题、120字符摘要、日期、标签（最多3个）

### Requirement: 分类筛选

用户 SHALL 能够通过点击分类 Tab 筛选文章。默认显示"全部"，点击分类后仅显示该分类下的文章。分类列表从后端获取。

#### Scenario: 默认显示全部文章
- **WHEN** 用户访问首页
- **THEN** 显示所有文章，按时间倒序

#### Scenario: 按分类筛选
- **WHEN** 用户点击某个分类 Tab
- **THEN** 仅显示该分类下的文章

#### Scenario: 返回全部
- **WHEN** 用户点击"全部" Tab
- **THEN** 恢复显示所有文章

### Requirement: 文章卡片交互

用户点击文章卡片 SHALL 导航到文章详情页 `/article/:id`。

#### Scenario: 点击文章卡片
- **WHEN** 用户点击文章卡片
- **THEN** 跳转到 `/article/{articleId}`

### Requirement: 响应式布局

页面布局 SHALL 能够自适应 768px 以上的屏幕宽度。内容区域最大宽度 900px 居中显示。

#### Scenario: 大屏幕显示
- **WHEN** 屏幕宽度 > 1200px
- **THEN** 内容宽度 900px 居中

#### Scenario: 中等屏幕显示
- **WHEN** 屏幕宽度在 768px - 1200px 之间
- **THEN** 内容宽度 90%

### Requirement: 空状态处理

当没有文章时，首页 SHALL 显示友好的空状态提示。

#### Scenario: 无文章时显示空状态
- **WHEN** 文章列表为空且非加载中
- **THEN** 显示"暂无文章"提示

