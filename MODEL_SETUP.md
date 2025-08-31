# 模型文件设置指南

## 📁 模型文件结构

本项目需要Live2D模型文件才能正常运行。模型文件结构如下：

```
model/
├── model_list.json                    # 模型列表配置
├── bilibili-live/                     # 模型分组
│   ├── 22/                           # 具体模型
│   │   ├── index.json                # 模型配置
│   │   ├── model.moc                 # Live2D模型文件
│   │   ├── motions/                  # 动作文件
│   │   │   ├── idle_01.mtn
│   │   │   └── ...
│   │   └── textures/                 # 纹理文件
│   │       ├── default.png
│   │       └── ...
│   └── 33/
└── Potion-Maker/
    ├── Pio/
    └── Tia/
```

## 🎯 必需的文件类型

### 1. 模型文件 (.moc)
- **作用：** Live2D的核心模型文件
- **位置：** 每个模型目录的根目录
- **示例：** `model/Potion-Maker/Pio/model.moc`

### 2. 动作文件 (.mtn)
- **作用：** 定义角色的动作和动画
- **位置：** `motions/` 目录
- **示例：** `model/Potion-Maker/Pio/motions/Breath1.mtn`

### 3. 纹理文件 (.png/.jpg)
- **作用：** 角色的外观和服装
- **位置：** `textures/` 目录
- **示例：** `model/Potion-Maker/Pio/textures/default-costume.png`

### 4. 配置文件 (.json)
- **作用：** 定义模型的各种参数
- **位置：** 每个模型目录的根目录
- **示例：** `model/Potion-Maker/Pio/index.json`

## 📋 获取模型文件的方法

### 方法1：从原PHP项目复制
```bash
# 如果本地有原PHP项目
cp -r live2d_api/model/* live2d-api-java/model/
```

### 方法2：从GitHub下载
```bash
# 克隆原PHP项目
git clone https://github.com/fghrsh/live2d_api.git
cp -r live2d_api/model/* live2d-api-java/model/
```

### 方法3：手动添加模型
1. 创建模型目录结构
2. 放入相应的文件
3. 更新 `model_list.json` 配置

## ⚠️ 重要说明

### Git仓库限制
由于模型文件通常很大（几十MB到几百MB），这些文件**不会**被提交到Git仓库中：

```gitignore
# Live2D Model files (too large for git)
model/*/*.moc
model/*/motions/*.mtn
model/*/textures/*.png
model/*/textures/*.jpg
model/*/textures/*.jpeg
```

### 部署时的注意事项
1. **开发环境：** 需要手动添加模型文件
2. **生产环境：** 通过Docker volume挂载模型文件
3. **CI/CD：** 从外部存储下载模型文件

## 🚀 快速开始

### 1. 获取模型文件
```bash
# 从原项目复制（推荐）
cp -r ../live2d_api/model/* model/

# 或从GitHub下载
git clone https://github.com/fghrsh/live2d_api.git temp
cp -r temp/model/* model/
rm -rf temp
```

### 2. 验证文件结构
```bash
# 检查模型文件是否存在
ls -la model/Potion-Maker/Pio/
# 应该看到：index.json, model.moc, motions/, textures/
```

### 3. 启动应用
```bash
mvn clean package
java -jar target/live2d-api-1.0.0.jar
```

### 4. 测试API
```bash
# 测试获取模型配置
curl "http://localhost:8080/api/get?id=1"

# 测试随机切换
curl "http://localhost:8080/api/rand?id=1"
```

## 🔧 Docker部署

### 使用Docker Compose
```yaml
version: '3.8'
services:
  live2d-api:
    build: .
    ports:
      - "8080:8080"
    volumes:
      - ./model:/app/model  # 挂载模型文件
    environment:
      - SPRING_PROFILES_ACTIVE=prod
```

### 手动Docker运行
```bash
# 构建镜像
docker build -t live2d-api .

# 运行容器（挂载模型文件）
docker run -d -p 8080:8080 \
  -v $(pwd)/model:/app/model \
  live2d-api
```

## 📊 模型文件大小参考

| 模型类型 | 文件大小 | 说明 |
|----------|----------|------|
| 简单模型 | 10-50MB | 基础角色，少量动作 |
| 标准模型 | 50-200MB | 完整角色，多种动作 |
| 复杂模型 | 200MB+ | 多套服装，丰富动作 |

## 🛠️ 故障排除

### 问题1：模型文件未找到
**症状：** API返回 "Model config not found"
**解决：** 检查模型目录结构和文件是否存在

### 问题2：皮肤缓存问题
**症状：** 皮肤切换不生效
**解决：** 调用 `/api/add` 接口更新缓存

### 问题3：文件权限问题
**症状：** 无法读取模型文件
**解决：** 检查文件权限，确保应用有读取权限

## 📝 模型文件来源

本项目中的模型文件来自：
- [fghrsh/live2d_api](https://github.com/fghrsh/live2d_api) - 原PHP版本
- 各种Live2D模型制作者
- 仅供学习研究使用

**版权声明：** 所有模型文件版权归原作者所有，请勿用于商业用途。