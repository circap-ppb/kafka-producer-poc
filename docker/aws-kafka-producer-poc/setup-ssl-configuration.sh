#!/usr/bin/env bash
set -e

# constants
KAFKA_CONSUMER_DOMAIN_NAME=$1
SSL_CERT_DIR=$2
APPLICATION=$3
THIS_REGION=$REGION

PRIVATE_KEY_FILE="private.key"
EXPORTED_CERTIFICATE_INFO_FILE="export-cert.json"
CERT="certificate.pem"
CA_CERT_FILE="certificateChain.pem"
KEYSTORE_FILE="keystore.jks"
KEYSTORE_P12_FILE="keystore.p12.jks"
TRUSTSTORE_FILE="truststore.jks"

if [[ -z $1 || -z $2 || -z $3 ]] ; then
    echo 'KAFKA_CONSUMER_DOMAIN_NAME, SSL_CERT_DIR, APPLICATION need to be defined'
    exit 1
fi
function create_and_change_to_ssl_cert_dir(){
  echo "- Creating ssl cert directory"
  mkdir -p "${SSL_CERT_DIR}" && cd "${SSL_CERT_DIR}"
}

function get_certificate_arn(){
  echo "- Retrieving Certificate ARN "
  # Get Certificate ARN for kafka consumer
  CERTIFICATE_ARN=$(aws --no-verify-ssl --region "${THIS_REGION}" acm list-certificates --output text | grep "${KAFKA_CONSUMER_DOMAIN_NAME}" | awk '{ print $2 }')
  echo "- ${CERTIFICATE_ARN}"
}

function get_private_key_name(){
  echo "- get private key name for /keys/${KAFKA_CONSUMER_DOMAIN_NAME}"
  PRIVATE_KEY_NAME=$(aws --no-verify-ssl  ssm describe-parameters --region "${THIS_REGION}" --filters Key=Name,Values=/keys/"${KAFKA_CONSUMER_DOMAIN_NAME}" --output json|jq -r ".Parameters[0].Name")
  echo "- ${PRIVATE_KEY_NAME}"
}

function get_app_private_key(){
  echo "- get Private Key for Service"
  aws --no-verify-ssl  --region "${THIS_REGION}" ssm get-parameter --name "${PRIVATE_KEY_NAME}" --with-decryption --query 'Parameter.Value' --output text > "${PRIVATE_KEY_FILE}"
}

function export_app_certificate(){
  echo "- Creating Certificate files"
  ## Export Certificate From AWS Certificate Manager and format into files
  aws acm get-certificate \
    --no-verify-ssl \
    --certificate-arn "${CERTIFICATE_ARN}" \
    --region "${THIS_REGION}" > "${EXPORTED_CERTIFICATE_INFO_FILE}"
  jq -r '.["Certificate"]' "${EXPORTED_CERTIFICATE_INFO_FILE}" > "${CERT}"
  jq -r '.["CertificateChain"]' "${EXPORTED_CERTIFICATE_INFO_FILE}" > "${CA_CERT_FILE}"
  echo "- Created ${CERT}"
  echo "- Created ${CA_CERT_FILE}"
}

function create_pkcs12_keystore(){
  # Create PKCS12 keystore from private key and public certificate.
  openssl pkcs12 -export \
      -name "${APPLICATION}" \
      -in "${CERT}" \
      -inkey "${PRIVATE_KEY_FILE}" \
      -passout pass:"${KAFKA_JKS_PASSWORD}" \
      -out "${KEYSTORE_P12_FILE}"
}

function convert_pkcs12_to_jks_keystore(){
# Convert PKCS12 keystore into a JKS keystore
  keytool -importkeystore \
      -destkeystore "${KEYSTORE_FILE}" \
      -srckeystore "${KEYSTORE_P12_FILE}" \
      -deststoretype pkcs12 \
      -srcstoretype pkcs12 \
      -storepass "${KAFKA_JKS_PASSWORD}" \
      -srcstorepass "${KAFKA_JKS_PASSWORD}" \
      -alias "${APPLICATION}" \
      -noprompt
}

function import_root_certificate_into_keystore(){
  # Import the CA into Keystore
  keytool -keystore "${KEYSTORE_FILE}" \
    -alias caroot \
    -import -file "${CA_CERT_FILE}" \
    -storepass "${KAFKA_JKS_PASSWORD}" \
    -noprompt
}

function import_root_certificate_into_truststore(){
  # add the CA cert file to trust store
  keytool -keystore "${TRUSTSTORE_FILE}" \
    -alias caroot \
    -import -file "${CA_CERT_FILE}" \
    -storepass "${KAFKA_JKS_PASSWORD}" \
    -noprompt
}

function delete_certificates(){
   rm "${CA_CERT_FILE}" "${KEYSTORE_P12_FILE}" "${PRIVATE_KEY_FILE}" "${CERT}" "${EXPORTED_CERTIFICATE_INFO_FILE}"
}

create_and_change_to_ssl_cert_dir
get_certificate_arn
get_private_key_name
get_app_private_key
export_app_certificate
create_pkcs12_keystore
convert_pkcs12_to_jks_keystore
import_root_certificate_into_keystore
import_root_certificate_into_truststore
delete_certificates