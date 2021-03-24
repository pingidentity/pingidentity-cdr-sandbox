FROM pingidentity/pingaccess:6.2.1-edge 

ARG APKURL

USER root

RUN curl $APKURL --output apk.zip && tar -zxvf apk.zip 
RUN ./sbin/apk.static -X http://dl-cdn.alpinelinux.org/alpine/latest-stable/main -U --allow-untrusted --initdb add apk-tools-static
RUN apk.static update
RUN apk.static -X http://dl-cdn.alpinelinux.org/alpine/latest-stable/main -U --allow-untrusted add apk-tools
RUN apk update
RUN apk add openssl

RUN rm -f /sbin/apk

USER ping 
