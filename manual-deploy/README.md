# Manual Deployment for PingDirectory, PingAccess, and PingFederate

These instructions demonstrate how the CDR IK can be deployed manually. In this example, PingDirectory, PingAccess, and PingFederate, are deployed manually using shell scripts (currently tested on Mac). 

This scenario simulates PingOne Advanced Services deployment of the CDR solution, where PingDirectory, PingAccess, and PingFederate are deployed minimally. A Postman collection is provided (details below) which completes the configuration for a basic deployment.

## Pre-requisites

- Java 11 is deployed on your host.
- Min Ping product versions:
  - PingFederate 11.0
  - PingAccess 7.0
  - PingDirectory 9.0
- Binaries are copied over to ./binaries [(Details)](binaries/README.md)
- Licenses are copied over to ./licences [(Details)](licences/README.md)
- Postman installed.
- Docker and docker-compose installed.
- /etc/hosts configured as described here: [(Link)](../docs/README.md)

## Steps

1. Navigate to the manual-deploy folder in terminal.
2. Start auxiliary servers using docker-compose:
  - docker-compose up -d
3. Start PingDirectory
  - ./_start_pingdirectory.sh
4. Start PingFederate
  - ./_start_pingfederate.sh
5. Start PingAccess
  - ./_start_pingaccess.sh
6. Configure and run Postman collection to configure PF and PA:
  - Default configuration should work.
  - Collection found here [(link)](scripts/cdr-au.configure_pa_pf.postman_collection.json)

## Shutdown steps

1. Navigate to the manual-deploy folder in terminal.
2. Run ./_stopall.sh
