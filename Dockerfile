FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw -ntp verify -DskipTests -Pprod package

FROM eclipse-temurin:17-jre-focal

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""

WORKDIR /app

EXPOSE 8080

# Add the application to the container
COPY --from=build /app/target/*.jar /app/app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"] 