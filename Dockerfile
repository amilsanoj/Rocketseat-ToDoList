# Multi-stage build otimizado
FROM maven:3.8.6-openjdk-17 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -q

FROM openjdk:17-jdk-alpine

# Criar usuário não-root para segurança
RUN addgroup -g 1001 -S appuser && \
    adduser -S appuser -u 1001 -G appuser

WORKDIR /app
COPY --from=build --chown=appuser:appuser /app/target/todolist-1.0.0.jar app.jar

USER appuser
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]