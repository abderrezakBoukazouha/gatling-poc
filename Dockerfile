FROM maven:3-amazoncorretto-21-alpine AS build
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package


FROM amazoncorretto:21.0.5-alpine
COPY --from=build /app/target/*.jar /usr/local/lib/presentation-app.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/presentation-app.jar"]

