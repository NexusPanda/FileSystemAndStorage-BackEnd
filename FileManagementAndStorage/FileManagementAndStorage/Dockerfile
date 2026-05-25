FROM openjdk:21
COPY target/filesystem-storage.jar filesystem-storage.jar
EXPOSE 5656
ENTRYPOINT ["java", "-jar", "filesystem-storage.jar"]