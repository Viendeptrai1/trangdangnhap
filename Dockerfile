# Multi-stage build cho ứng dụng Java Web
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first để tận dụng Docker layer caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Runtime stage
FROM tomcat:10.1-jdk17-openjdk-slim

# Set working directory
WORKDIR /usr/local/tomcat

# Copy built WAR file
COPY --from=build /app/target/trangdangnhap-1.0-SNAPSHOT.war webapps/ROOT.war

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]
