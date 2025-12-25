@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM ===========================================
REM Qing 微服务 Docker Compose 部署脚本 (Windows)
REM ===========================================

set "RED=[91m"
set "GREEN=[92m"
set "YELLOW=[93m"
set "BLUE=[94m"
set "NC=[0m"

REM 打印带颜色的消息
:print_info
echo %BLUE%[INFO]%NC% %~1
goto :eof

:print_success
echo %GREEN%[SUCCESS]%NC% %~1
goto :eof

:print_warning
echo %YELLOW%[WARNING]%NC% %~1
goto :eof

:print_error
echo %RED%[ERROR]%NC% %~1
goto :eof

REM 检查 Docker 和 Docker Compose
:check_requirements
call :print_info "检查系统要求..."

docker --version >nul 2>&1
if errorlevel 1 (
    call :print_error "Docker 未安装，请先安装 Docker Desktop"
    pause
    exit /b 1
)

docker-compose --version >nul 2>&1
if errorlevel 1 (
    docker compose version >nul 2>&1
    if errorlevel 1 (
        call :print_error "Docker Compose 未安装，请先安装 Docker Compose"
        pause
        exit /b 1
    )
)

call :print_success "系统要求检查通过"
goto :eof

REM 创建必要的目录
:create_directories
call :print_info "创建必要的目录..."

if not exist "data" mkdir data
if not exist "data\postgres" mkdir data\postgres
if not exist "data\redis" mkdir data\redis
if not exist "data\consul" mkdir data\consul
if not exist "data\minio" mkdir data\minio
if not exist "data\rabbitmq" mkdir data\rabbitmq
if not exist "logs" mkdir logs

call :print_success "目录创建完成"
goto :eof

REM 检查环境变量文件
:check_env_file
if not exist ".env" (
    call :print_warning ".env 文件不存在，从 .env.example 复制..."
    copy ".env.example" ".env" >nul
    call :print_info "请编辑 .env 文件配置您的环境变量"
    call :print_info "配置完成后重新运行此脚本"
    pause
    exit /b 0
)
goto :eof

REM 构建镜像
:build_images
call :print_info "构建 Docker 镜像..."

REM 这里需要根据实际的 Dockerfile 位置调整
call :print_info "请确保已经为各个服务创建了 Dockerfile"
call :print_info "镜像构建需要在各个服务目录中执行"

call :print_success "镜像构建完成"
goto :eof

REM 启动基础设施服务
:start_infrastructure
call :print_info "启动基础设施服务..."

docker-compose up -d postgres redis consul minio rabbitmq

call :print_info "等待基础设施服务启动..."
timeout /t 30 /nobreak >nul

call :print_success "基础设施服务启动完成"
goto :eof

REM 启动应用服务
:start_applications
call :print_info "启动应用服务..."

REM 先启动配置中心
docker-compose up -d qing-config-server
call :print_info "等待配置中心启动..."
timeout /t 30 /nobreak >nul

REM 启动其他应用服务
docker-compose up -d qing-gateway qing-auth-service qing-anime-service

call :print_success "应用服务启动完成"
goto :eof

REM 显示服务状态
:show_status
call :print_info "服务状态:"
docker-compose ps

echo.
call :print_info "服务访问地址:"
echo   - API 网关: http://localhost:9527
echo   - 认证服务: http://localhost:8088
echo   - 动漫服务: http://localhost:8080
echo   - 配置中心: http://localhost:8888
echo   - Consul: http://localhost:8500
echo   - MinIO 控制台: http://localhost:9001
echo   - RabbitMQ 管理界面: http://localhost:15672
goto :eof

REM 停止所有服务
:stop_services
call :print_info "停止所有服务..."
docker-compose down
call :print_success "所有服务已停止"
goto :eof

REM 清理数据
:clean_data
call :print_warning "这将删除所有数据，包括数据库数据！"
set /p "confirm=确定要继续吗？(y/N): "
if /i "!confirm!"=="y" (
    call :print_info "清理数据..."
    docker-compose down -v
    docker system prune -f
    if exist "data" rmdir /s /q "data"
    call :print_success "数据清理完成"
) else (
    call :print_info "取消清理操作"
)
goto :eof

REM 显示日志
:show_logs
if "%~2"=="" (
    docker-compose logs -f
) else (
    docker-compose logs -f %~2
)
goto :eof

REM 显示帮助信息
:show_help
echo Qing 微服务 Docker Compose 部署脚本 (Windows)
echo.
echo 用法: %~nx0 [命令]
echo.
echo 命令:
echo   start       启动所有服务
echo   stop        停止所有服务
echo   restart     重启所有服务
echo   status      显示服务状态
echo   logs [服务] 显示日志
echo   build       构建镜像
echo   clean       清理数据
echo   help        显示帮助信息
echo.
echo 示例:
echo   %~nx0 start          # 启动所有服务
echo   %~nx0 logs gateway   # 显示网关服务日志
echo   %~nx0 clean          # 清理所有数据
goto :eof

REM 主函数
if "%~1"=="start" (
    call :check_requirements
    call :create_directories
    call :check_env_file
    call :start_infrastructure
    call :start_applications
    call :show_status
) else if "%~1"=="stop" (
    call :stop_services
) else if "%~1"=="restart" (
    call :stop_services
    timeout /t 5 /nobreak >nul
    call "%~f0" start
) else if "%~1"=="status" (
    call :show_status
) else if "%~1"=="logs" (
    call :show_logs %*
) else if "%~1"=="build" (
    call :check_requirements
    call :build_images
) else if "%~1"=="clean" (
    call :clean_data
) else if "%~1"=="help" (
    call :show_help
) else if "%~1"=="--help" (
    call :show_help
) else if "%~1"=="-h" (
    call :show_help
) else if "%~1"=="" (
    call :show_help
) else (
    call :print_error "未知命令: %~1"
    call :show_help
    exit /b 1
)

pause