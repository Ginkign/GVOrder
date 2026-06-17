#  微服务示例 — 构建与运行说明（详细）

简要：本目录包含 3 个最小 Spring Boot 服务示例：`user-service`（8081）、`product-service`（8082）、`order-service`（8083）。示例编排文件位于 `docker-compose.yml`。

**服务与端口**
- `user-service` — 8081
- `product-service` — 8082
- `order-service` — 8083
- `mysql`（docker-compose）— 3306

**先决条件**
- Java 11+
- Maven (`mvn`) — 构建 jar（需要能访问 Maven 仓库）
- Docker & docker-compose（推荐）

一、在可联网环境构建并运行（开发）

1) 构建每个服务：

```bash
cd /www/dspt/user-service && mvn clean package -DskipTests
cd /www/dspt/product-service && mvn clean package -DskipTests
cd /www/dspt/order-service && mvn clean package -DskipTests
```

2) 直接运行某个服务（调试）：

```bash
java -jar /www/dspt/user-service/target/user-service-0.0.1-SNAPSHOT.jar --server.port=8081
```

二、使用 Docker Compose（推荐在有网络或已准备好 jars 的环境）

```bash
# 在 /www/dspt 下运行
docker-compose up --build
```

说明：每个服务的 `Dockerfile` 预期复制 `target/*.jar`。如果你没有在目标环境构建 jar，可在能联网的机器构建并将 `target/*.jar` 复制到对应目录后再执行 `docker-compose up --build`。

三、数据库初始化（MySQL）

在 MySQL 中创建数据库：

```sql
CREATE DATABASE IF NOT EXISTS user_db DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS product_db DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS order_db DEFAULT CHARACTER SET utf8mb4;
```

或修改各服务 `src/main/resources/application.yml` 使用同一数据库（仅作演示）或在 `docker-compose.yml` 中使用 `MYSQL_DATABASE` 初始化。

四、环境变量（Docker / 运行时）样例

- `SPRING_DATASOURCE_URL`：覆盖数据源 URL，例如 `jdbc:mysql://mysql:3306/user_db`
- `SERVICE_PRODUCT_URL`：订单服务中指向商品服务的地址（例如 `http://product-service:8082`）

五、完整 curl 示例

- 注册用户：

```bash
curl -s -X POST http://localhost:8081/api/users/register \
	-H "Content-Type: application/json" \
	-d '{"username":"u1","password":"p","email":"a@b.com"}'
```

- 登录用户：

```bash
curl -s -X POST http://localhost:8081/api/users/login \
	-H "Content-Type: application/json" \
	-d '{"username":"u1","password":"p"}'
```

- 添加商品（管理员场景）：

```bash
curl -s -X POST http://localhost:8082/api/products \
	-H "Content-Type: application/json" \
	-d '{"name":"sku1","stock":100,"price":9.90}'
```

- 查询商品：

```bash
curl -s http://localhost:8082/api/products
```

- 创建订单（假设 userId=1, productId=1）：

```bash
curl -s -X POST http://localhost:8083/api/orders \
	-H "Content-Type: application/json" \
	-d '{"userId":1,"items":[{"productId":1,"quantity":2}]}'
```

- 支付订单：

```bash
curl -s -X POST http://localhost:8083/api/orders/1/pay
```

六、受限网络或离线部署建议

- 在一台可联网机器上执行 Maven 构建并将 `target/*.jar` 复制到目标主机。
- 或在 CI 中构建并把镜像推到私有 Registry，然后在目标环境拉取镜像部署。

七、故障排查要点

- 若 Maven 构建失败并提示无法下载插件或依赖，请检查网络或在 `~/.m2/settings.xml` 中配置内部镜像（Nexus/Artifactory）。
- 若 `docker-compose up` 失败，先确认 `target/*.jar` 存在于服务目录，或在 `docker-compose.yml` 中改用已发布镜像。

文件：更多细节见 [dspt/README.md](dspt/README.md)


包含三个最小 Spring Boot 服务示例：`user-service`、`product-service`、`order-service`，以及示例 `docker-compose.yml`。

快速开始（本地开发）：

1. 各服务分别进入对应目录，使用 Maven 构建：

```bash
cd user-service
mvn clean package -DskipTests
java -jar target/user-service-0.0.1-SNAPSHOT.jar --server.port=8081
```

2. 或使用 Docker Compose（需先构建镜像或在服务目录中包含 Dockerfile）：

```bash
docker-compose up --build
```

配置和数据库请参考各服务目录下的 `application.yml`。
