FROM openjdk:11-jre-slim
VOLUME /tmp
#VOLUME /opt/dataright/localdb
#EXPOSE 8080
ARG JAR_FILE=deepthought-banking-api-service-1.1.0-SNAPSHOT.jar
ADD target/${JAR_FILE} /opt/dataright/api-auth.jar
USER root
COPY cert/ /opt/ 
RUN \
    cd /opt/ \
    && sh ./add_crt.sh
ENTRYPOINT ["java","-jar","/opt/dataright/api-auth.jar"]
