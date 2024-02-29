FROM openjdk:17
ADD target/blog-web.jar blog-web.jar
ENTRYPOINT ["java", "-jar", "/blog-web.jar"]