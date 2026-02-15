# --- СТАДИЯ 1: СБОРКА ---
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
# Кэшируем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline
# Собираем код
COPY src ./src
RUN mvn clean package -DskipTests

# --- СТАДИЯ 2: РАНТАЙМ ---
# Используем максимально легкий образ
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Копируем только результат (JAR) из первой стадии
COPY --from=builder /app/target/*.jar app.jar

# В Distroless нет пользователя root по умолчанию в некоторых версиях,
# либо мы можем запускать под non-root (65532 - стандарт для distroless)
USER 65532
ENTRYPOINT ["java", "-jar", "app.jar"]