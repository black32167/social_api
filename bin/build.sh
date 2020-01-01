#!/bin/bash

PROTO_SUBDIR="src/main/proto"

GEN_TARGET="$(pwd)/generated-api"
SCRIPT_DIR="${BASH_SOURCE%/*}"
SERVER_MOCKS_MODULE="server-mocks"

generate() {
    local api="${1}"
    local generator="${2}"
    local api_dir="./api/${api}"
    local CONFIG_FILE="${api_dir}/config/${generator}.json"

    PARAMS=""
    if [ -f "${CONFIG_FILE}" ]; then
        PARAMS+="-c ${CONFIG_FILE}"
    fi

    ${SCRIPT_DIR}/swagger.sh generate \
            -l "${generator}" \
            -i "${api_dir}/${api}.yaml" \
            -o "${GEN_TARGET}/${api}/${generator}" \
             ${PARAMS}
           # -Dapis,models,supportingFiles

    if [ -f "${GEN_TARGET}/${api}/${generator}/pom.xml" ]; then
        mvn_install "${GEN_TARGET}/${api}/${generator}"
    fi
}

mvn_install() {
    local workdir="${1}"
    (cd "${workdir}" && mvn install)
}

generateApi() {
    local api="${1}"

    local impl_types=(
        "java"
        "jaxrs-di"
        "html"
    )
    for impl in ${impl_types[@]}; do
      generate "${api}" "${impl}"
    done
}

generateApis() {
    rm -rf "${GEN_TARGET}"
    generateApi "task"
    generateApi "infra"
}

buildMockServer() {
    (cd "${SERVER_MOCKS_MODULE}" && mvn clean install -DskipTests)
}

CMD="${1}"
case "${CMD}" in
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