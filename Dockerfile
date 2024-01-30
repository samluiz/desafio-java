FROM maven:3.8.3-openjdk-17 AS maven

COPY src /app/src
COPY pom.xml /app/pom.xml

WORKDIR app
RUN mvn package

FROM eclipse-temurin:17-jdk AS builder

ARG JAR_FILE=app/target/*.jar

COPY --from=maven ${JAR_FILE} app.jar

RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]