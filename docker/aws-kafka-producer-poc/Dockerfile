FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.5_10

ENV JAR_FILE "kafka-producer-poc-0.0.1-SNAPSHOT.jar"
ENV JAR_LOCATION "target/kafka-producer-poc-0.0.1-SNAPSHOT.jar"
ENV APP_DIR "/opt/sportsbet/smf/kafka-producer-poc"
ENV APP_JAR "${APP_DIR}/${JAR_FILE}"
ENV JAVA_OPTS="-Xmx1024m"
ENV APP_TIME_ZONE="Australia/Darwin"

RUN apk update && apk add bash && \
    apk add openssl && apk add jq && \
    apk add --update --no-cache python3

RUN pip3 --no-cache-dir install pip awscli --upgrade

COPY ${JAR_LOCATION} ${APP_JAR}
COPY docker/aws-kafka-producer-poc/startup.sh /startup.sh
COPY docker/aws-kafka-producer-poc/setup-ssl-configuration.sh /setup-ssl-configuration.sh
RUN chmod +x /setup-ssl-configuration.sh

EXPOSE 8080:8080

ENTRYPOINT ["/bin/bash", "/startup.sh"]