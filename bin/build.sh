#!/bin/bash

PROTO_SUBDIR="src/main/proto"

GEN_TARGET="$(pwd)/generated-api"
SCRIPT_DIR="${BASH_SOURCE%/*}"
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

buildTools() {
    echo "Building Tools..."

    mvn clean install -f ./tools >> "${LOG}" 2>&1 || die "Error building tools"
}

generateApis() {
    buildGenerators
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

buildGenerators() {
    local SERVER_SDK_CODEGEN_JAR="${HOME}/.m2/repository/org/openapitools/java-server-sdk-openapi-generator/1.0.0/java-server-sdk-openapi-generator-1.0.0.jar"

    mvn clean install -DskipTests \
      -f ./generators >> "${LOG}" 2>&1 || die "Error generating mock server"

    cp "${SERVER_SDK_CODEGEN_JAR}" "${DOWNLOAD}"

    echo "Custom API generators are built"
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
      buildGenerators
      ;;
    tools)
      buildTools
      ;;
    apis)
      generateApis
      ;;
    docs)
      generateDocs
      ;;
    all)
      generateApis
      buildTools
      ;;
    *)
      echo "Usage:"
      echo "${0} {gens|apis|docs|tools|all}"
esac