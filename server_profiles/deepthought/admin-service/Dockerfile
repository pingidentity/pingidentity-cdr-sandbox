#FROM openjdk:11-jre-slim
FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-alpine-slim
RUN apk upgrade musl
VOLUME /tmp
VOLUME /opt/deepthought/localdb
EXPOSE 8080
ARG JAR_FILE=deepthought-admin-service-1.1.0-SNAPSHOT.jar
ADD target/${JAR_FILE} /opt/deepthought/deepthought-admin-service.jar
COPY cert/ /opt/
RUN cd /opt/  && sh ./add_crt.sh
#ENTRYPOINT ["java","-Ddebug=true","-jar","/opt/deepthought/deepthought-admin-service.jar"]
ENTRYPOINT ["java","-jar","/opt/deepthought/deepthought-admin-service.jar"]
