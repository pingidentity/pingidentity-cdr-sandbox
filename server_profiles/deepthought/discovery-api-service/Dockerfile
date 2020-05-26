FROM openjdk:11-jre-slim
VOLUME /tmp
VOLUME /opt/deepthought/localdb
EXPOSE 8081
ARG JAR_FILE
ADD target/${JAR_FILE} /opt/deepthought/deepthought-product-api-service.jar
ENTRYPOINT ["java","-jar","/opt/deepthought/deepthought-product-api-service.jar"]
