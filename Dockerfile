FROM maven:latest as build
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]