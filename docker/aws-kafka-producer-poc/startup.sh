#!/usr/bin/env bash
set -e
export SSL_CERT_DIR="/opt/ssl/"
export REGION=ap-southeast-2

echo "Prepare client certificates for kafka. consumer/producer: ${KAFKA_CONSUMER_DOMAIN_NAME}, application:${APPLICATION}"
# generate random jks password
export KAFKA_JKS_PASSWORD=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)
/setup-ssl-configuration.sh ${KAFKA_CONSUMER_DOMAIN_NAME} ${SSL_CERT_DIR} ${APPLICATION}

echo "Starting kafka-producer-poc"
java ${JAVA_OPTS} -Duser.timezone=${APP_TIME_ZONE} -jar ${APP_JAR}