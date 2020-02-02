#!/bin/bash
VERSION="0.0.1"
REPO_ID="github"
REPO_URL="https://maven.pkg.github.com/black32167/social_api"
GROUP_ID="social.api"
: ${M2_REPO:=${HOME}/.m2}
GROUP_ROOT="${M2_REPO}/repository/social/api"
TMP_DIR="/var/tmp/social_api"

deploy() {
  local artifactId="${1}"
  cp "${GROUP_ROOT}/${artifactId}/${VERSION}/${artifactId}-${VERSION}.jar" "${TMP_DIR}/"
  mvn deploy:deploy-file \
    -DartifactId="${artifactId}" \
    -DgroupId="${GROUP_ID}" \
    -Dversion="${VERSION}" \
    -Dfile="${TMP_DIR}/${artifactId}-${VERSION}.jar" \
    -DrepositoryId="${REPO_ID}" \
    -Durl="${REPO_URL}"
}

mkdir -p "${TMP_DIR}"

deploy admin-server
deploy admin-client
deploy message-server
deploy message-client
deploy task-server
deploy task-client
deploy user-server
deploy user-client
