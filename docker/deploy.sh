#!/bin/bash

# ===========================================
# Qing 微服务 Docker Compose 部署脚本
# ===========================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 Docker 和 Docker Compose
check_requirements() {
    print_info "检查系统要求..."
    
    if ! command -v docker &> /dev/null; then
        print_error "Docker 未安装，请先安装 Docker"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
        print_error "Docker Compose 未安装，请先安装 Docker Compose"
        exit 1
    fi
    
    print_success "系统要求检查通过"
}

# 创建必要的目录
create_directories() {
    print_info "创建必要的目录..."
    
    mkdir -p data/postgres
    mkdir -p data/redis
    mkdir -p data/consul
    mkdir -p data/minio
    mkdir -p data/rabbitmq
    mkdir -p logs
    
    print_success "目录创建完成"
}

# 检查环境变量文件
check_env_file() {
    if [ ! -f ".env" ]; then
        print_warning ".env 文件不存在，从 .env.example 复制..."
        cp .env.example .env
        print_info "请编辑 .env 文件配置您的环境变量"
        print_info "配置完成后重新运行此脚本"
        exit 0
    fi
}

# 构建镜像
build_images() {
    print_info "构建 Docker 镜像..."
    
    # 构建配置中心镜像
    print_info "构建配置中心镜像..."
    docker build -t qing/qing-config-server:latest -f qing-config-server/Dockerfile qing-config-server/
    
    # 构建网关镜像
    print_info "构建网关镜像..."
    docker build -t qing/qing-service-cloud-gateway:latest -f qing-service-cloud-gateway/Dockerfile qing-service-cloud-gateway/
    
    # 构建认证服务镜像
    print_info "构建认证服务镜像..."
    docker build -t qing/qing-service-auth:latest -f qing-services/qing-service-auth/Dockerfile qing-services/qing-service-auth/
    
    # 构建动漫服务镜像
    print_info "构建动漫服务镜像..."
    docker build -t qing/qing-service-anime:latest -f qing-services/qing-service-anime/Dockerfile qing-services/qing-service-anime/
    
    print_success "镜像构建完成"
}

# 启动基础设施服务
start_infrastructure() {
    print_info "启动基础设施服务..."
    
    docker-compose up -d postgres redis consul minio rabbitmq
    
    print_info "等待基础设施服务启动..."
    sleep 30
    
    print_success "基础设施服务启动完成"
}

# 启动应用服务
start_applications() {
    print_info "启动应用服务..."
    
    # 先启动配置中心
    docker-compose up -d qing-config-server
    print_info "等待配置中心启动..."
    sleep 30
    
    # 启动其他应用服务
    docker-compose up -d qing-gateway qing-auth-service qing-anime-service
    
    print_success "应用服务启动完成"
}

# 显示服务状态
show_status() {
    print_info "服务状态:"
    docker-compose ps
    
    print_info "\n服务访问地址:"
    echo "  - API 网关: http://localhost:9527"
    echo "  - 认证服务: http://localhost:8088"
    echo "  - 动漫服务: http://localhost:8080"
    echo "  - 配置中心: http://localhost:8888"
    echo "  - Consul: http://localhost:8500"
    echo "  - MinIO 控制台: http://localhost:9001"
    echo "  - RabbitMQ 管理界面: http://localhost:15672"
}

# 停止所有服务
stop_services() {
    print_info "停止所有服务..."
    docker-compose down
    print_success "所有服务已停止"
}

# 清理数据
clean_data() {
    print_warning "这将删除所有数据，包括数据库数据！"
    read -p "确定要继续吗？(y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_info "清理数据..."
        docker-compose down -v
        docker system prune -f
        rm -rf data/
        print_success "数据清理完成"
    else
        print_info "取消清理操作"
    fi
}

# 显示日志
show_logs() {
    if [ -z "$2" ]; then
        docker-compose logs -f
    else
        docker-compose logs -f "$2"
    fi
}

# 显示帮助信息
show_help() {
    echo "Qing 微服务 Docker Compose 部署脚本"
    echo ""
    echo "用法: $0 [命令]"
    echo ""
    echo "命令:"
    echo "  start       启动所有服务"
    echo "  stop        停止所有服务"
    echo "  restart     重启所有服务"
    echo "  status      显示服务状态"
    echo "  logs [服务] 显示日志"
    echo "  build       构建镜像"
    echo "  clean       清理数据"
    echo "  help        显示帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 start          # 启动所有服务"
    echo "  $0 logs gateway   # 显示网关服务日志"
    echo "  $0 clean          # 清理所有数据"
}

# 主函数
main() {
    case "$1" in
        start)
            check_requirements
            create_directories
            check_env_file
            start_infrastructure
            start_applications
            show_status
            ;;
        stop)
            stop_services
            ;;
        restart)
            stop_services
            sleep 5
            main start
            ;;
        status)
            show_status
            ;;
        logs)
            show_logs "$@"
            ;;
        build)
            check_requirements
            build_images
            ;;
        clean)
            clean_data
            ;;
        help|--help|-h)
            show_help
            ;;
        "")
            show_help
            ;;
        *)
            print_error "未知命令: $1"
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"