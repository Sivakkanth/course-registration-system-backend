FROM maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /target/Couse-Registration-and-System-0.0.1-SNAPSHOT.jar Couse-Registration-and-System.jar
EXPOSE 7777
ENTRYPOINT ["java","-jar", "Couse-Registration-and-System.jar"]