# Live2D API - Java版本

Live2D 看板娘插件后端 API 的 Java 实现版本，基于 Spring Boot 框架开发。

## 项目特性

- **Spring Boot 2.7.14** - 现代化的Java Web框架
- **RESTful API** - 标准的REST接口设计
- **JSON配置** - 灵活的模型和皮肤配置
- **自动缓存** - 智能的皮肤缓存机制
- **Docker支持** - 容器化部署
- **跨平台** - 支持Windows、Linux、macOS

## 环境要求

- **Java 8+** - JDK 1.8 或更高版本
- **Maven 3.6+** - 项目构建工具
- **内存** - 建议至少512MB可用内存

## 快速开始

### 1. 克隆项目

```bash
git clone <your-repo-url>
cd live2d-api-java
```

### 2. 编译项目

```bash
mvn clean package -DskipTests
```

### 3. 启动应用

```bash
# 使用启动脚本
./start.sh

# 或直接使用Java命令
java -jar target/live2d-api-1.0.0.jar
```

### 4. 访问API

应用启动后，API将在 `http://localhost:8080` 上运行。

## API接口

### 获取模型配置

```http
GET /api/get?id=1-23
```

**参数说明：**
- `id`: 模型ID-皮肤ID（如：1-23表示模型1的第23个皮肤）

**响应示例：**
```json
{
  "version": "1.0.0",
  "model": "../model/Potion-Maker/Pio/model.moc",
  "textures": [
    "../model/Potion-Maker/Pio/textures/default-costume.png"
  ],
  "layout": {
    "center_x": 0.0,
    "center_y": -0.05,
    "width": 2.0
  },
  "motions": {
    "idle": [
      {"file": "../model/Potion-Maker/Pio/motions/Breath1.mtn"}
    ]
  }
}
```

### 随机切换模型

```http
GET /api/rand?id=1
```

**参数说明：**
- `id`: 当前模型ID

**响应示例：**
```json
{
  "model": {
    "id": 3,
    "name": "bilibili-live/22",
    "message": "来自 Bilibili Live 的 22 哦 ~"
  }
}
```

### 顺序切换模型

```http
GET /api/switch?id=1
```

### 随机切换皮肤

```http
GET /api/rand_textures?id=1-23
```

### 顺序切换皮肤

```http
GET /api/switch_textures?id=1-23
```

### 更新皮肤缓存

```http
GET /api/add
```

## 项目结构

```
live2d-api-java/
├── src/main/java/com/live2d/api/
│   ├── Live2dApiApplication.java          # 启动类
│   ├── controller/
│   │   └── Live2dController.java          # REST控制器
│   ├── model/
│   │   ├── ModelList.java                 # 模型列表配置
│   │   └── ModelConfig.java               # 模型配置文件
│   └── service/
│       ├── ModelListService.java          # 模型列表服务
│       └── ModelTexturesService.java      # 皮肤管理服务
├── src/main/resources/
│   ├── application.yml                    # 主配置文件
│   ├── application-dev.yml               # 开发环境配置
│   └── application-prod.yml              # 生产环境配置
├── model/                                # 模型文件目录
│   ├── model_list.json                   # 模型列表配置
│   └── Potion-Maker/
│       └── Pio/
│           ├── index.json                # 模型配置
│           ├── model.moc                 # Live2D模型文件
│           ├── motions/                  # 动作文件
│           └── textures/                 # 纹理文件
├── pom.xml                               # Maven配置
├── Dockerfile                            # Docker配置
├── docker-compose.yml                    # Docker Compose配置
└── start.sh                              # 启动脚本
```

## 配置说明

### application.yml

```yaml
server:
  port: 8080                              # 服务端口

live2d:
  model:
    path: model                           # 模型文件路径

logging:
  level:
    com.live2d.api: DEBUG                 # 日志级别
```

### model_list.json

```json
{
  "models": [
    "Potion-Maker/Pio",                   # 单个模型
    [                                      # 模型组
      "ShizukuTalk/shizuku-48",
      "ShizukuTalk/shizuku-pajama"
    ]
  ],
  "messages": [
    "来自 Potion Maker 的 Pio 酱 ~",       # 对应的介绍消息
    "Shizuku Talk ！这里是 Shizuku ~"
  ]
}
```

## 皮肤系统

### 单模型单皮肤模式

```
model/ModelName/
├── index.json
├── model.moc
├── textures.cache                        # 自动生成
├── motions/
└── textures/
    ├── default-costume.png
    └── school-costume.png
```

### 多组皮肤组合模式

```
model/ModelName/
├── index.json
├── model.moc
├── textures.cache                        # 自动生成
├── textures_order.json                   # 组合规则
├── motions/
├── texture_00/
│   └── 00.png
├── texture_01/
│   ├── 00.png
│   ├── 01.png
│   └── 02.png
└── texture_02/
    ├── 00.png
    └── 01.png
```

**textures_order.json 示例：**
```json
[
  ["texture_00"],
  ["texture_01", "texture_02"],
  ["texture_03"]
]
```

## Docker部署

### 构建镜像

```bash
docker build -t live2d-api .
```

### 运行容器

```bash
docker run -d -p 8080:8080 \
  -v $(pwd)/model:/app/model \
  -v $(pwd)/logs:/app/logs \
  live2d-api
```

### 使用Docker Compose

```bash
docker-compose up -d
```

## 开发指南

### 添加新模型

1. 将模型文件放入 `model/` 目录
2. 更新 `model/model_list.json` 配置
3. 重启应用或调用 `/api/add` 更新缓存

### 自定义配置

修改 `src/main/resources/application.yml` 文件：

```yaml
live2d:
  model:
    path: /path/to/your/models  # 自定义模型路径
```

### 日志配置

```yaml
logging:
  level:
    com.live2d.api: DEBUG       # 调试级别
  file:
    name: logs/live2d-api.log   # 日志文件路径
```

## 性能优化

### JVM参数调优

```bash
java -Xms512m -Xmx1024m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar target/live2d-api-1.0.0.jar
```

### 缓存优化

- 皮肤缓存文件 `textures.cache` 会自动生成和更新
- 建议定期调用 `/api/add` 接口更新缓存
- 生产环境建议使用Redis等外部缓存

## 故障排除

### 常见问题

1. **模型文件未找到**
   - 检查 `model_list.json` 配置是否正确
   - 确认模型文件路径存在

2. **皮肤缓存问题**
   - 删除 `textures.cache` 文件重新生成
   - 调用 `/api/add` 接口更新缓存

3. **端口冲突**
   - 修改 `application.yml` 中的端口配置
   - 检查防火墙设置

### 日志查看

```bash
# 查看应用日志
tail -f logs/live2d-api.log

# 查看Docker容器日志
docker logs -f <container-id>
```

## 与PHP版本的差异

| 特性 | PHP版本 | Java版本 |
|------|---------|----------|
| 框架 | 原生PHP | Spring Boot |
| 性能 | 中等 | 高 |
| 部署 | 简单 | 容器化 |
| 扩展性 | 有限 | 强 |
| 维护性 | 一般 | 优秀 |

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 致谢

- 原PHP版本作者 [fghrsh](https://github.com/fghrsh)
- Live2D 官方团队
- Spring Boot 社区

---

**注意：** API 内所有模型版权均属于原作者，仅供研究学习，不得用于商业用途。