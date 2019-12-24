#!/bin/bash

DOWNLOAD="$(pwd)/download"
SWAGGER_CODEGEN_VRSION="3.0.14"
SWAGGER_CODEGEN_CLI_URL="https://repo1.maven.org/maven2/io/swagger/codegen/v3/swagger-codegen-cli/${SWAGGER_CODEGEN_VRSION}/swagger-codegen-cli-${SWAGGER_CODEGEN_VRSION}.jar"
SWAGGER_CODEGEN_LOCAL="${DOWNLOAD}/swagger-codegen-cli-${SWAGGER_CODEGEN_VRSION}.jar"

installSwaggerCodegen() {
    mkdir -p "${DOWNLOAD}"
    [ -f "${SWAGGER_CODEGEN_LOCAL}" ] || wget -O "${SWAGGER_CODEGEN_LOCAL}" "${SWAGGER_CODEGEN_CLI_URL}"
}

installSwaggerCodegen
java -jar "${SWAGGER_CODEGEN_LOCAL}" "$@"