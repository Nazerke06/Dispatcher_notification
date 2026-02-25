FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/dispatcher-notification-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java",
    "-XX:MaxRAMPercentage=75.0",
    "-XX:+UseG1GC",
    "-Xlog:gc*:stdout:time,level,tags",
    "-jar", "app.jar"]