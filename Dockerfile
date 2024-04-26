FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
COPY . /app
WORKDIR /app
RUN mvn package

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*spring-boot.jar /app.jar
COPY --from=build /app/sql /sql

ENTRYPOINT ["java", "-jar", "/app.jar"]