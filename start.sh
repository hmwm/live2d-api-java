#!/bin/bash

# Live2D API Java版本启动脚本

echo "Starting Live2D API Java Version..."

# 检查Java版本
java -version

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven first."
    exit 1
fi

# 编译项目
echo "Building project..."
mvn clean package -DskipTests

# 检查编译是否成功
if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

# 启动应用
echo "Starting application..."
java -jar target/live2d-api-1.0.0.jar --spring.profiles.active=dev

echo "Live2D API started successfully!"
echo "API endpoints available at: http://localhost:8080/api/"