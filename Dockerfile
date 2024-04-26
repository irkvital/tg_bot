FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
COPY pom.xml /app/pom.xml
WORKDIR /app
RUN mvn dependency:go-offline
COPY ./src ./src
RUN mvn package

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*spring-boot.jar /app.jar
COPY ./sql /sql
COPY .env .

ENTRYPOINT ["java", "-jar", "/app.jar"]