FROM maven:3.6.3-jdk-8-slim as build

ENV APP_HOME=/app

WORKDIR $APP_HOME

COPY . $APP_HOME

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:8-jdk-alpine

ENV TZ=Asia/Shanghai

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

VOLUME /tmp

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dspring-boot.run.profiles=dev -jar /app.jar"]
