# NexusBlog 环境变量配置文档

本文档说明了 NexusBlog 后端应用所需的环境变量。

## 必需环境变量

### MySQL 数据库配置

| 环境变量 | 说明 | 默认值 | 示例 |
|-----------|------|---------|-------|
| `DB_URL` | MySQL 数据库连接 URL | `jdbc:mysql://localhost:3306/nexusblog?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true` | `jdbc:mysql://db.example.com:3306/nexusblog?useUnicode=true&characterEncoding=utf8mb4&useSSL=true&serverTimezone=Asia/Shanghai` |
| `DB_USERNAME` | MySQL 数据库用户名 | `root` | `nexusblog_user` |
| `DB_PASSWORD` | MySQL 数据库密码 | (空) | `your_secure_password` |

## 环境变量设置方式

### 方式 1: 使用 .env 文件（开发环境）

在项目根目录创建 `.env` 文件：

```bash
DB_URL=jdbc:mysql://localhost:3306/nexusblog?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=your_password
```

**注意**: `.env` 文件不应提交到版本控制，请确保它已添加到 `.gitignore`。

### 方式 2: 在 IDE 中配置

**IntelliJ IDEA:**
1. 打开 Run/Debug Configurations
2. 选择 NexusBlogApplication
3. 在 Environment variables 字段中添加上述环境变量

**Eclipse/STS:**
1. 右键点击项目 → Run As → Run Configurations
2. 在 Environment 标签页中添加上述环境变量

### 方式 3: 使用命令行参数

```bash
java -jar nexusblog-backend-1.0.0.jar \
  --DB_URL="jdbc:mysql://localhost:3306/nexusblog?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true" \
  --DB_USERNAME=root \
  --DB_PASSWORD=your_password
```

### 方式 4: 使用容器化部署

**Docker:**
```yaml
# docker-compose.yml
version: '3.8'
services:
  nexusblog:
    image: nexusblog:latest
    environment:
      - DB_URL=jdbc:mysql://mysql:3306/nexusblog?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
      - DB_USERNAME=nexusblog
      - DB_PASSWORD=secure_password
    depends_on:
      - mysql

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=root_password
      - MYSQL_DATABASE=nexusblog
      - MYSQL_USER=nexusblog
      - MYSQL_PASSWORD=secure_password
```

**Kubernetes:**
```yaml
# deployment.yaml
apiVersion: v1
kind: Deployment
metadata:
  name: nexusblog
spec:
  template:
    spec:
      containers:
      - name: nexusblog
        image: nexusblog:latest
        env:
        - name: DB_URL
          value: "jdbc:mysql://mysql-service:3306/nexusblog?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true"
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: password
```

## 数据库要求

### MySQL 版本
- **推荐版本**: MySQL 8.0 或更高版本
- **最低版本**: MySQL 5.7+

### 数据库配置

创建数据库：

```sql
CREATE DATABASE nexusblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

创建用户（可选，推荐）：

```sql
CREATE USER 'nexusblog'@'%' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON nexusblog.* TO 'nexusblog'@'%';
FLUSH PRIVILEGES;
```

### 连接参数说明

- `useUnicode=true`: 启用 Unicode 支持
- `characterEncoding=utf8mb4`: 使用 UTF8MB4 字符集，支持 emoji 等特殊字符
- `useSSL=false`: 生产环境使用 `useSSL=true`
- `serverTimezone=Asia/Shanghai`: 设置服务器时区
- `allowPublicKeyRetrieval=true`: 允许公钥检索（用于某些加密场景）

## 常见问题

### 1. 连接超时

如果遇到连接超时错误，检查：
- MySQL 服务是否正在运行
- 防火墙是否允许连接
- 数据库 URL 中的主机名和端口是否正确

### 2. 字符编码问题

确保：
- 数据库使用 UTF8MB4 字符集
- 连接 URL 中包含 `characterEncoding=utf8mb4`
- MySQL 配置文件中的 `character-set-server=utf8mb4`

### 3. SSL 连接错误

开发环境可以使用 `useSSL=false` 绕过。
生产环境需要：
- 配置 SSL 证书
- 使用 `useSSL=true` 和相应的 SSL 参数

### 4. 时区问题

确保连接 URL 中包含正确的时区参数，如 `serverTimezone=Asia/Shanghai`。

## 安全建议

1. **不要在代码中硬编码数据库凭证**
2. **使用强密码**
3. **定期更换密码**
4. **限制数据库用户权限**（遵循最小权限原则）
5. **使用密钥管理服务**（如 AWS Secrets Manager、HashiCorp Vault）
6. **不要将 .env 文件提交到版本控制**
