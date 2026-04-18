#  NexusBlog 平台 Monorepo

## 结构
```
/
├── frontend/         # Vue3 前端应用（见 /frontend/CLAUDE.md）
├── backend/           # SpringBoot 后端应用（见 /backend/CLAUDE.md）
├── docker/            # Docker 配置
│   └── frontend/
├── openspec/          # OpenSpec 变更管理
│   ├── changes/       # 变更记录
│   │   └── archive/
│   └── specs/         # 规范文档
├── .github/           # GitHub 配置
└── CLAUDE.md          # 项目说明文档
```

## 全局规则（适用于所有应用）
- 所有 PR 必须通过 CI 才能合并。

## 命令
```bash

```

## 跨应用规则
- 前端应用仅通过 API 获取数据，禁止直接访问数据库。
- API 变更必须保持向后兼容。破坏性变更需要版本控制。

