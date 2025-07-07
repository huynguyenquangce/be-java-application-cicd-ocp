

# --- Stage 1: Build the Application ---
# FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder  
FROM docker.io/library/maven:3.9.6-eclipse-temurin-17-alpine
WORKDIR /app
COPY pom.xml .  
RUN mvn dependency:go-offline 
COPY src ./src  
RUN mvn clean package -DskipTests  

# --- Stage 2: Run the Application ---
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8082
CMD ["java", "-jar", "app.jar"]
    