#!/bin/bash

SERVER_MOCKS_MODULE="server-mocks"
MAIN_CLASS=""
# Runs mock server

cd "${SERVER_MOCKS_MODULE}"
mvn exec:java -Dexec.mainClass="social.api.test.server.MocksRunnerKt"