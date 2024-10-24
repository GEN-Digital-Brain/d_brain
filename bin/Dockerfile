# Stage 1: Build the application
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app

# Copy project files to the image
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight image to run the application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR file generated in the previous step
COPY --from=build /app/target/*.jar app.jar

# Expose the port where the application will run
EXPOSE 8080

# Define the command to start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
