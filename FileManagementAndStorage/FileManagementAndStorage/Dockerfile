FROM openjdk:27-ea-oraclelinux9
ADD /target/filesystem-storage.jar filesystem-storage.jar
EXPOSE 5656
ENTRYPOINT ["java", "-jar", "filesystem-storage.jar"]
