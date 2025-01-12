
# Use an official Maven image to build the project
FROM maven:3.8.4-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and project files to the working directory
COPY pom.xml .
COPY src ./src

# Build the Maven project
RUN mvn clean package

# Use a smaller base image to run the application
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container for the runtime
WORKDIR /app

# Copy the jar file from the build image to the runtime image
COPY --from=build /app/target/library.app-0.0.1-SNAPSHOT.jar /app/my-app.jar

# Command to run the jar file
CMD ["java", "-jar", "/app/my-app.jar"]