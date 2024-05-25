FROM openjdk:17-oracle

EXPOSE 8081

ADD target/BlogWebApi-0.0.1-SNAPSHOT.jar BlogWebApi-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "BlogWebApi-0.0.1-SNAPSHOT.jar"]
