#!/bin/bash

DOWNLOAD="$(pwd)/download"
OPENAPI_CODEGEN_VRSION="4.2.2"
OPENAPI_CODEGEN_CLI_URL="http://central.maven.org/maven2/org/openapitools/openapi-generator-cli/${OPENAPI_CODEGEN_VRSION}/openapi-generator-cli-${OPENAPI_CODEGEN_VRSION}.jar"
OPENAPI_CODEGEN_LOCAL="${DOWNLOAD}/openapi-generator-cli-${OPENAPI_CODEGEN_VRSION}.jar"
SERVER_SDK_CODEGEN_JAR="${HOME}/.m2/repository/org/openapitools/java-server-sdk-openapi-generator/1.0.0/java-server-sdk-openapi-generator-1.0.0.jar"

cleanupDownload() {
    rm "${OPENAPI_CODEGEN_LOCAL}"
    exit 1
}

installOPENAPICodegen() {
    mkdir -p "${DOWNLOAD}"
    [ -f "${OPENAPI_CODEGEN_LOCAL}" ] || wget -O "${OPENAPI_CODEGEN_LOCAL}" "${OPENAPI_CODEGEN_CLI_URL}" || cleanupDownload
}

installServerSdkCodegen() {
    local SERVER_SDK_GENERATOR_MODULE="./generators/java-server-sdk"

   # [ -f "${SERVER_SDK_CODEGEN_JAR}" ] || \
        (cd "${SERVER_SDK_GENERATOR_MODULE}" && mvn clean install -DskipTests) || \
        exit 1
}

installOPENAPICodegen
installServerSdkCodegen
#java -jar "${OPENAPI_CODEGEN_LOCAL}" "$@"
java -DdebugOperations -DdebugModels -cp "${SERVER_SDK_CODEGEN_JAR}:${OPENAPI_CODEGEN_LOCAL}" org.openapitools.codegen.OpenAPIGenerator "$@"
#java -jar download/openapi-generator-cli-4.2.2.jar meta -o generators/java-server-sdk -n java-server-sdk -p social.api