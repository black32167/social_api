# Social Collaboration Apis
This repository is supposed to contain:
* OpenAPI declarations of Application Programming Interfaces (APIs) for social collaboration services
* Scripts which are needed to generate clients and service stubs in different languages
* Service mocks implementing declared APIS
* Blackbox tests for declared APIs

## Repository structure
* _api_ - APIs declarations
* _bin_ - scripts for APIs generation and mocks building
* _generators_ - custom API clients/stubs generators
* _server-mocks_ - APIs server mocks and blackbox tests

## Building artifacts
    # Builds APIs artifacts:
    <social_api>$ ./bin/build.sh apis
    
    # Builds mock API server artifact:
    <social_api>$ ./bin/build.sh mock-server
    
