FROM openjdk:11
WORKDIR usr/src/app
COPY target/web-app-1.0-SNAPSHOT.jar app.jar
COPY wait-for-it.sh wait-for-it.sh
CMD ["java","-jar","app.jar"]
