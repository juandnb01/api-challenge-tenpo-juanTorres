# Primera etapa: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar archivos del proyecto y compilar
COPY . .
RUN mvn clean package -DskipTests

# Segunda etapa: Ejecución
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copiar solo el JAR generado en la primera etapa
COPY --from=builder /app/target/api-challengeTenpo-0.0.1-SNAPSHOT.jar .

ENV PORT 8007
EXPOSE $PORT

ENTRYPOINT ["java", "-jar", "api-challengeTenpo-0.0.1-SNAPSHOT.jar"]
