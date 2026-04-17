## ADDED Requirements

### Requirement: YAML Front Matter解析
系统 SHALL 支持解析Markdown文件开头的YAML Front Matter元数据，提取标题、日期、分类、标签、摘要等信息。

#### Scenario: 完整Front Matter解析
- **WHEN** Markdown文件包含完整的YAML Front Matter
  ```yaml
  ---
  title: 文章标题
  date: 2024-01-01
  category: 技术
  tags: [Java, Spring Boot]
  excerpt: 这是文章摘要
  ---
  ```
- **THEN** 系统提取所有元数据字段，作为文章的对应属性

#### Scenario: 部分Front Matter解析
- **WHEN** Markdown文件只包含部分Front Matter字段
- **THEN** 系统提取存在的字段，缺失的字段使用默认值

#### Scenario: 无Front Matter处理
- **WHEN** Markdown文件不包含YAML Front Matter
- **THEN** 系统使用文件名作为标题，正文前200字符作为摘要，使用仓库配置的默认分类和标签

### Requirement: 文件名解析
系统 SHALL 支持从Markdown文件名中提取标题信息，支持命名格式如"2024-01-01-文章标题.md"。

#### Scenario: 带日期前缀的文件名解析
- **WHEN** 文件名为"2024-01-01-我的第一篇文章.md"
- **THEN** 系统提取日期为2024-01-01，标题为"我的第一篇文章"

#### Scenario: 普通文件名解析
- **WHEN** 文件名为"我的第一篇文章.md"
- **THEN** 系统使用当前日期作为发布日期，标题为"我的第一篇文章"

#### Scenario: 文件名特殊字符处理
- **WHEN** 文件名包含连字符、下划线、空格等特殊字符
- **THEN** 系统正确处理，标题中特殊字符保留，连字符替换为空格

### Requirement: 内容提取
系统 SHALL 正确提取Markdown文件的正文内容，排除YAML Front Matter部分。

#### Scenario: 正文内容提取
- **WHEN** Markdown文件包含YAML Front Matter和正文
- **THEN** 系统只保留Front Matter之后的内容作为文章正文

#### Scenario: 无Front Matter内容提取
- **WHEN** Markdown文件不包含YAML Front Matter
- **THEN** 系统将整个文件内容作为文章正文
