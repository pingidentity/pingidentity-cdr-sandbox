FROM openjdk:11-jre-slim
VOLUME /tmp
VOLUME /opt/dataright/localdb
EXPOSE 8080
ARG JAR_FILE
ADD target/${JAR_FILE} /opt/dataright/api-auth.jar
ENTRYPOINT ["java","-jar","/opt/dataright/api-auth.jar"]
