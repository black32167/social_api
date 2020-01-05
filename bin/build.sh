#!/bin/bash

PROTO_SUBDIR="src/main/proto"

GEN_TARGET="$(pwd)/generated-api"
SCRIPT_DIR="${BASH_SOURCE%/*}"
SERVER_MOCKS_MODULE="server-mocks"
LOG="generate.log"

die() {
    echo "${1}"
    tail -n50 "${LOG}"
    exit 1
}

generate() {
    local api="${1}"
    local generator="${2}"
    local api_dir="./api/${api}"
    local CONFIG_FILE="${api_dir}/config/${generator}.json"
    local api_pom="${GEN_TARGET}/${api}/${generator}/pom.xml"

    PARAMS=""
    if [ -f "${CONFIG_FILE}" ]; then
        echo "Found config file ${CONFIG_FILE}"
        PARAMS+="-c ${CONFIG_FILE}"
    fi

    echo "Generating API '${api}' using generator '${generator}'"
    ${SCRIPT_DIR}/openapi.sh generate \
            -g "${generator}" \
            -i "${api_dir}/${api}.yaml" \
            -o "${GEN_TARGET}/${api}/${generator}" \
            -p groupId=social.api,artifactId=${api}-${generator} \
             ${PARAMS} >> "${LOG}" 2>&1  || die "Error generating ${api}"


    if [ -f "${api_pom}" ]; then
        echo "Building ${api} artifacts using pom ${api_pom}"
        mvn_install "${GEN_TARGET}/${api}/${generator}" || die "Error building artifact for api '${api}'"
    fi
}

mvn_install() {
    local workdir="${1}"
    (cd "${workdir}" && mvn install) >> "${LOG}" 2>&1
}

generateApi() {
    local api="${1}"

    local impl_types=(
         "java-server-sdk"
#        "kotlin-server"
#        "kotlin-spring"
        "java"
#        "jaxrs-spec"
#        "jaxrs-jersey"
#        "spring"
#        "html"
    )
    for impl in ${impl_types[@]}; do
      generate "${api}" "${impl}"
    done
}

buildApiGenerators() {
    echo "Building API generators..."
    local SERVER_SDK_GENERATOR_MODULE="$(pwd)/generators/java-server-sdk"
    local SERVER_SDK_CODEGEN_JAR="${HOME}/.m2/repository/org/openapitools/java-server-sdk-openapi-generator/1.0.0/java-server-sdk-openapi-generator-1.0.0.jar"

    # [ -f "${SERVER_SDK_CODEGEN_JAR}" ] || \
    (cd "${SERVER_SDK_GENERATOR_MODULE}" && mvn clean install -DskipTests) >> "${LOG}" 2>&1 || die "Error API artifacts generators"

    cp "${SERVER_SDK_CODEGEN_JAR}" "${DOWNLOAD}"
}

generateApis() {
    buildApiGenerators
    rm -rf "${GEN_TARGET}"
    generateApi "task"
    generateApi "infra"

    echo "Apis are built"
}

buildMockServer() {
    (cd "${SERVER_MOCKS_MODULE}" && mvn clean install -DskipTests) >> "${LOG}" 2>&1 || die "Error generating mock server"
    echo "Mock server is built"
}

SCRIPT_DIR=${BASH_SOURCE%/*}
. ${SCRIPT_DIR}/shared.sh

rm -rf "${LOG}"

CMD="${1}"
case "${CMD}" in
    gens)
        buildApiGenerators
        ;;
    apis)
        generateApis
        ;;
    mock-server)
        buildMockServer
        ;;
    all)
        generateApis
        buildMockServer
        ;;
    *)
        echo "Usage:"
        echo "${0} {apis|mock-server|all}"
esac