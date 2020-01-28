#!/bin/bash

###############
### Functions
###############

cleanupDownload() {
    rm "${OPENAPI_CODEGEN_LOCAL}"
    exit 1
}

installOPENAPICodegen() {
    [ -f "${OPENAPI_CODEGEN_LOCAL}" ] || wget --no-check-certificate -O "${OPENAPI_CODEGEN_LOCAL}" "${OPENAPI_CODEGEN_CLI_URL}" || cleanupDownload
}

###############
### Variables
###############

SCRIPT_DIR=${BASH_SOURCE%/*}
. ${SCRIPT_DIR}/shared.sh
OPENAPI_CODEGEN_VRSION="4.2.2"
OPENAPI_CODEGEN_CLI_URL="https://central.maven.org/maven2/org/openapitools/openapi-generator-cli/${OPENAPI_CODEGEN_VRSION}/openapi-generator-cli-${OPENAPI_CODEGEN_VRSION}.jar"
OPENAPI_CODEGEN_LOCAL="${DOWNLOAD}/openapi-generator-cli-${OPENAPI_CODEGEN_VRSION}.jar"

###############
### Execution flow
###############

installOPENAPICodegen

java -DdebugOperations -DdebugModels -cp "${DOWNLOAD}/*" org.openapitools.codegen.OpenAPIGenerator "$@"