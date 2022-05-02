FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR '/deploy/spring-azure-cloud-api/'
COPY target/spring-azure-cloud-0.0.1-SNAPSHOT.jar /deploy/spring-azure-cloud-api/spring-azure-cloud-0.0.1-SNAPSHOT.jar

RUN chmod 777 -R /deploy/spring-azure-cloud-api/
RUN chmod 777 -R /deploy/spring-azure-cloud-api/spring-azure-cloud-0.0.1-SNAPSHOT.jar

RUN apk add --no-cache bash \
    && apk add --no-cache curl \
    && rm -fR /var/cache/apk/* \
    && chgrp root /etc/passwd \
    && chmod ug+rw /etc/passwd

COPY Entrypoint.sh /deploy/spring-azure-cloud-api/Entrypoint.sh
RUN chmod 777 -R /deploy/spring-azure-cloud-api/Entrypoint.sh

ENV app_path /deploy/spring-azure-cloud-api/spring-azure-cloud-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["/deploy/spring-azure-cloud-api/Entrypoint.sh"]