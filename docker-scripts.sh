#!/bin/bash

# Docker Management Scripts cho dự án trangdangnhap
# Sử dụng: ./docker-scripts.sh [command]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Functions
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

# Commands
start_db() {
    print_info "Starting PostgreSQL container..."
    docker-compose up -d postgres
    print_success "PostgreSQL started successfully!"
    print_info "Waiting for PostgreSQL to be ready..."
    sleep 5
    print_info "You can now connect to:"
    print_info "  Server: localhost:5432"
    print_info "  Database: DBUser"
    print_info "  Username: postgres"
    print_info "  Password: postgres123"
}

start_all() {
    print_info "Starting all services..."
    docker-compose up -d
    print_success "All services started successfully!"
    print_info "Web application: http://localhost:8080"
    print_info "SQL Server: localhost:1433"
}

stop_all() {
    print_info "Stopping all services..."
    docker-compose down
    print_success "All services stopped!"
}

restart_all() {
    print_info "Restarting all services..."
    docker-compose down
    docker-compose up -d
    print_success "All services restarted!"
}

reset_db() {
    print_warning "This will delete all data in the database!"
    read -p "Are you sure? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_info "Resetting database..."
        docker-compose down -v
        docker-compose up -d sqlserver
        print_success "Database reset completed!"
    else
        print_info "Database reset cancelled."
    fi
}

logs() {
    if [ -z "$2" ]; then
        docker-compose logs -f
    else
        docker-compose logs -f "$2"
    fi
}

status() {
    print_info "Container status:"
    docker-compose ps
    echo
    print_info "Docker system info:"
    docker system df
}

build() {
    print_info "Building Java application..."
    mvn clean package
    print_success "Build completed!"
}

backup_db() {
    BACKUP_DIR="./backup"
    mkdir -p "$BACKUP_DIR"
    
    BACKUP_FILE="DBUser_$(date +%Y%m%d_%H%M%S).sql"
    
    print_info "Creating database backup..."
    docker exec trangdangnhap-postgres pg_dump -U postgres -d DBUser > "$BACKUP_DIR/$BACKUP_FILE"
    
    print_success "Backup created: $BACKUP_DIR/$BACKUP_FILE"
}

restore_db() {
    if [ -z "$2" ]; then
        print_error "Please specify backup file: ./docker-scripts.sh restore backup/DBUser_20240101_120000.sql"
        exit 1
    fi
    
    BACKUP_FILE="$2"
    if [ ! -f "$BACKUP_FILE" ]; then
        print_error "Backup file not found: $BACKUP_FILE"
        exit 1
    fi
    
    print_warning "This will replace all data in the database!"
    read -p "Are you sure? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_info "Restoring database from $BACKUP_FILE..."
        
        # Restore database
        docker exec -i trangdangnhap-postgres psql -U postgres -d DBUser < "$BACKUP_FILE"
        
        print_success "Database restored successfully!"
    else
        print_info "Database restore cancelled."
    fi
}

help() {
    echo "Docker Management Scripts for trangdangnhap project"
    echo
    echo "Usage: $0 [command]"
    echo
    echo "Commands:"
    echo "  start-db     Start only PostgreSQL container"
    echo "  start        Start all services (PostgreSQL + Web App)"
    echo "  stop         Stop all services"
    echo "  restart      Restart all services"
    echo "  reset-db     Reset database (delete all data)"
    echo "  logs [service] Show logs (optionally for specific service)"
    echo "  status       Show container status and system info"
    echo "  build        Build Java application"
    echo "  backup       Create database backup"
    echo "  restore <file> Restore database from backup file"
    echo "  help         Show this help message"
    echo
    echo "Examples:"
    echo "  $0 start-db"
    echo "  $0 logs postgres"
    echo "  $0 backup"
    echo "  $0 restore backup/DBUser_20240101_120000.sql"
}

# Main script logic
case "${1:-help}" in
    start-db)
        start_db
        ;;
    start)
        start_all
        ;;
    stop)
        stop_all
        ;;
    restart)
        restart_all
        ;;
    reset-db)
        reset_db
        ;;
    logs)
        logs "$@"
        ;;
    status)
        status
        ;;
    build)
        build
        ;;
    backup)
        backup_db
        ;;
    restore)
        restore_db "$@"
        ;;
    help|--help|-h)
        help
        ;;
    *)
        print_error "Unknown command: $1"
        echo
        help
        exit 1
        ;;
esac
