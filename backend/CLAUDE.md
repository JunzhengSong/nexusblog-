## 技术栈
- Java 21
- Spring Boot 3.2.1
- MyBatis-Plus 3.5.7
- MySQL 

## 命令

### 重启后端
```bash
# 1. 找到占用端口的进程
netstat -ano | grep 8080

# 2. 停止该进程（替换 <PID> 为实际进程ID）
taskkill //F //PID <PID>

# 3. Maven 编译
mvn clean compile -DskipTests

# 4. Maven 启动
mvn spring-boot:run -DskipTests

# 5. 校验端口是否正确监听
netstat -ano | grep 8080 | grep LISTENING
```

## 规则
- 