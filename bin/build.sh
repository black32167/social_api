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

generator2ArtifactType() {
  case "${1}" in
  java)
    echo "client"
    ;;
  java-server-sdk)
    echo "server"
    ;;
  *)
    echo "${1}"
  esac

}
generate() {
    local api="${1}"
    local generator="${2}"
    local api_dir="./api/${api}"
    local config_file="${api_dir}/config/${generator}.json"
    local api_out="${GEN_TARGET}/${generator}/${api}"
    local api_pom="${api_out}/pom.xml"
    local artifact_type=$(generator2ArtifactType ${generator})

    local params=""
    if [ -f "${config_file}" ]; then
        echo "Found config file ${config_file}"
        params+="-c ${config_file}"
    fi

    echo "Generating API '${api}' using generator '${generator}'"
    ${SCRIPT_DIR}/openapi.sh generate \
            -g "${generator}" \
            -i "${api_dir}/${api}.yaml" \
            -o "${api_out}" \
            -p groupId=social.api \
            -p "artifactId=${api}-${artifact_type}" \
            -p "apiPackage=social.api.${api}.${artifact_type}" \
            -p "modelPackage=social.api.${api}.model" \
             ${params} >> "${LOG}" 2>&1  || die "Error generating ${api}"


    if [ -f "${api_pom}" ]; then
        echo "Building ${api} artifacts using pom ${api_pom}"
        mvn_install "${api_out}" || die "Error building artifact for api '${api}'"
    fi
}

mvn_install() {
    local workdir="${1}"
    (cd "${workdir}" && mvn install) >> "${LOG}" 2>&1
}

generateApi() {
    local api="${1}"
    for impl in ${GENERATORS[@]}; do
      generate "${api}" "${impl}"
    done
}

buildApiGenerators() {
    echo "Building API generators..."
    local SERVER_SDK_GENERATOR_MODULE="$(pwd)/generators/java-server-sdk"
    local SERVER_SDK_CODEGEN_JAR="${HOME}/.m2/repository/org/openapitools/java-server-sdk-openapi-generator/1.0.0/java-server-sdk-openapi-generator-1.0.0.jar"

    # [ -f "${SERVER_SDK_CODEGEN_JAR}" ] || \
    (cd "${SERVER_SDK_GENERATOR_MODULE}" && mvn clean install) >> "${LOG}" 2>&1 || die "Error API artifacts generators"

    cp "${SERVER_SDK_CODEGEN_JAR}" "${DOWNLOAD}"
}

generateApis() {
    buildApiGenerators
    rm -rf "${GEN_TARGET}"
    for api in ${APIS[@]}; do
      generateApi "${api}"
    done
    echo "Apis are built"
}

generateDocs() {
    GENERATORS="html" generateApis
    echo "Documentation generated in ${GEN_TARGET}/html"
    cp -rv "${GEN_TARGET}/html/." "${DOCUMENTATION_OUT}/"
}

buildMockServer() {
    (cd "${SERVER_MOCKS_MODULE}" && mvn clean install -DskipTests) >> "${LOG}" 2>&1 || die "Error generating mock server"
    echo "Mock server is built"
}

SCRIPT_DIR=${BASH_SOURCE%/*}
. ${SCRIPT_DIR}/shared.sh
: ${GENERATORS:="java" "java-server-sdk"}
: ${APIS:="task" "admin" "user" "message"}
DOCUMENTATION_OUT="docs"

rm -rf "${LOG}"

CMD="${1}"
case "${CMD}" in
    gens)
        buildApiGenerators
        ;;
    apis)
        generateApis
        ;;
    docs)
        generateDocs
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
        echo "${0} {gens|apis|docs|mock-server|all}"
esac