FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace/app

COPY pom.xml .
COPY src src

RUN apk add --no-cache maven
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"] 