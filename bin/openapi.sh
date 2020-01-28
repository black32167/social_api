#!/bin/bash

###############
### Functions
###############

cleanupDownload() {
    rm "${OPENAPI_CODEGEN_LOCAL}"
    exit 1
}

installOPENAPICodegen() {
    [ -f "${OPENAPI_CODEGEN_LOCAL}" ] || cp "${M2_REPO}/repository/org/openapitools/openapi-generator-cli/${OPENAPI_CODEGEN_VRSION}/openapi-generator-cli-${OPENAPI_CODEGEN_VRSION}.jar" "${OPENAPI_CODEGEN_LOCAL}"
}

###############
### Variables
###############

SCRIPT_DIR=${BASH_SOURCE%/*}
. ${SCRIPT_DIR}/shared.sh
OPENAPI_CODEGEN_VRSION="4.2.2"
OPENAPI_CODEGEN_LOCAL="${DOWNLOAD}/openapi-generator-cli-${OPENAPI_CODEGEN_VRSION}.jar"

###############
### Execution flow
###############

: ${M2_REPO:=${HOME}/.m2}

echo "M2_REPO=${M2_REPO}"
installOPENAPICodegen

java -DdebugOperations -DdebugModels -cp "${DOWNLOAD}/*" org.openapitools.codegen.OpenAPIGenerator "$@"