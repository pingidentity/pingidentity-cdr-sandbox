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
- Licenses are copied over to ./licences [(Details)](licenses/README.md)
- Postman installed.
  - Cert up MTLS certificates as described [(here)](../docs/howtos/howto_generatecerts.md) for the following hosts/port:
    - sso-mtls.data-holder.local:3000
- Docker and docker-compose installed.
- /etc/hosts configured as described here: [(Link)](../docs/README.md)

## Steps

1. Navigate to the manual-deploy folder in terminal.
2. Start auxiliary servers using docker-compose:
    - docker-compose up -d
    - This will start up a mock register, mock API for accounts, and a sample consent application.
3. Start PingDirectory
    - ./_start_pingdirectory.sh
    - This will start PingDirectory in a pre-configured state.
4. Start PingFederate
    - ./_start_pingfederate.sh
    - This will start PingFederate in an unconfigured state.
5. Start PingAccess
    - ./_start_pingaccess.sh
    - This will start PingAccess in an unconfigured state.
6. Configure and run Postman collection to configure PF and PA:
    - At minimum, you need to configure CIAM IDP collection variables (ciamIdpIssuer, ciamIdpClientId, ciamIdpClientSecret). The remaining default values should be fine for a POC environment.
    - Collection found here [(link)](scripts/cdr-au.configure_pa_pf.postman_collection.json)

## Test

Configure and run Postman collection to perform a test:
  - Default configuration should work.
  - Collection found here [(link)](scripts/cdr-au.test_pa_pf.postman_collection.json)

When testing, you will have generated a CDR initiation URL. Copy and paste this into Firefox, accept SSL errors, until the browser redirects to httpbin.org with a code parameter. Copy the code parameter back into the Postman request when exchanging the code for an access token.

## Shutdown steps

1. Navigate to the manual-deploy folder in terminal.
2. Run ./_stopall.sh
