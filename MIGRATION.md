# 从PHP版本迁移到Java版本指南

本文档将帮助您从PHP版本的Live2D API迁移到Java版本。

## 迁移概述

Java版本完全兼容PHP版本的API接口和配置文件格式，迁移过程相对简单。

## 迁移步骤

### 1. 环境准备

**PHP版本要求：**
- PHP 5.2+
- JSON扩展

**Java版本要求：**
- Java 8+
- Maven 3.6+

### 2. 文件迁移

#### 配置文件迁移

**PHP版本：**
```
live2d_api/
├── model_list.json
├── model/
└── tools/
```

**Java版本：**
```
live2d-api-java/
├── model/
│   └── model_list.json
└── src/main/java/
```

**迁移操作：**
1. 复制 `model_list.json` 到 `model/` 目录
2. 复制整个 `model/` 目录到Java项目根目录
3. 确保模型文件结构保持不变

#### 模型文件迁移

模型文件结构完全兼容，直接复制即可：

```bash
# 复制模型文件
cp -r live2d_api/model/* live2d-api-java/model/
```

### 3. API接口对比

| 功能 | PHP版本 | Java版本 | 兼容性 |
|------|---------|----------|--------|
| 获取模型配置 | `/get/?id=1-23` | `/api/get?id=1-23` | ✅ 完全兼容 |
| 随机切换模型 | `/rand/?id=1` | `/api/rand?id=1` | ✅ 完全兼容 |
| 顺序切换模型 | `/switch/?id=1` | `/api/switch?id=1` | ✅ 完全兼容 |
| 随机切换皮肤 | `/rand_textures/?id=1-23` | `/api/rand_textures?id=1-23` | ✅ 完全兼容 |
| 顺序切换皮肤 | `/switch_textures/?id=1-23` | `/api/switch_textures?id=1-23` | ✅ 完全兼容 |
| 更新缓存 | `/add/` | `/api/add` | ✅ 完全兼容 |

### 4. 前端代码修改

如果您的网站使用了Live2D API，只需要修改API的基础URL：

**PHP版本：**
```javascript
const API_BASE = 'http://your-domain.com/live2d_api/';
```

**Java版本：**
```javascript
const API_BASE = 'http://your-domain.com:8080/api/';
```

### 5. 部署方式对比

#### PHP版本部署

```bash
# 上传文件到Web服务器
# 配置虚拟主机
# 确保PHP环境正常
```

#### Java版本部署

**方式1：直接运行**
```bash
# 编译项目
mvn clean package

# 运行应用
java -jar target/live2d-api-1.0.0.jar
```

**方式2：Docker部署**
```bash
# 构建镜像
docker build -t live2d-api .

# 运行容器
docker run -d -p 8080:8080 live2d-api
```

**方式3：Docker Compose**
```bash
docker-compose up -d
```

### 6. 配置迁移

#### PHP版本配置

PHP版本通过文件系统直接访问配置文件。

#### Java版本配置

Java版本使用YAML配置文件：

```yaml
# application.yml
server:
  port: 8080

live2d:
  model:
    path: model  # 对应PHP版本的model目录
```

### 7. 性能对比

| 指标 | PHP版本 | Java版本 | 提升 |
|------|---------|----------|------|
| 启动时间 | 即时 | ~10秒 | - |
| 内存使用 | 低 | 中等 | - |
| 并发处理 | 有限 | 优秀 | ✅ |
| 缓存效率 | 文件缓存 | 内存+文件 | ✅ |
| 扩展性 | 有限 | 优秀 | ✅ |

### 8. 常见问题

#### Q: 模型文件路径问题
**A:** 确保模型文件放在 `model/` 目录下，与 `model_list.json` 在同一级。

#### Q: 皮肤缓存不更新
**A:** 调用 `/api/add` 接口或删除 `textures.cache` 文件重新生成。

#### Q: 端口冲突
**A:** 修改 `application.yml` 中的端口配置。

#### Q: 跨域问题
**A:** Java版本已配置CORS支持，允许跨域访问。

### 9. 回滚方案

如果需要回滚到PHP版本：

1. 停止Java应用
2. 恢复PHP版本文件
3. 修改前端API地址
4. 重启Web服务器

### 10. 迁移检查清单

- [ ] 模型文件已复制到Java项目
- [ ] `model_list.json` 配置正确
- [ ] 前端API地址已更新
- [ ] 端口配置正确
- [ ] 防火墙规则已更新
- [ ] 域名解析已更新（如需要）
- [ ] 测试所有API接口
- [ ] 验证模型切换功能
- [ ] 检查皮肤缓存机制

## 技术支持

如果在迁移过程中遇到问题，请：

1. 查看应用日志：`logs/live2d-api.log`
2. 检查配置文件格式
3. 验证模型文件完整性
4. 确认网络连接正常

## 总结

Java版本提供了更好的性能、可扩展性和维护性，同时保持了与PHP版本的完全兼容性。迁移过程简单，风险较低，建议在生产环境迁移前先在测试环境验证。