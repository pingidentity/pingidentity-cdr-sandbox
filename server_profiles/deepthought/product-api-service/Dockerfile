#FROM openjdk:11-jre-slim
FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-alpine-slim
RUN apk upgrade musl
VOLUME /tmp
#VOLUME /opt/deepthought/localdb
EXPOSE 8081
ARG JAR_FILE=deepthought-product-api-service-1.1.0-SNAPSHOT.jar
ADD target/${JAR_FILE} /opt/deepthought/deepthought-product-api-service.jar
COPY cert/ /opt/
RUN cd /opt/  && sh ./add_crt.sh
ENTRYPOINT ["java","-jar","/opt/deepthought/deepthought-product-api-service.jar"]
