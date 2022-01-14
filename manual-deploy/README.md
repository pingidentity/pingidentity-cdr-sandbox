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
- Docker and docker-compose installed.
- /etc/hosts configured as described here: [(Link)](../docs/README.md)
    - Also add: sso-mtls.data-holder.local

## Steps

1. Navigate to the manual-deploy folder in terminal.
2. Start auxiliary servers using docker-compose:
    - This will start up a mock register, mock API for accounts, and a sample consent application.
      ```
        docker-compose up -d
      ```

3. Start PingDirectory
    - This will start PingDirectory in a pre-configured state.
      ```
        ./_start_pingdirectory.sh
      ```

4. Start PingFederate
    - This will start PingFederate in an unconfigured state.
      ```
        ./_start_pingfederate.sh
      ```

5. Start PingAccess
    - This will start PingAccess in an unconfigured state.
      ```
        ./_start_pingaccess.sh
      ```

6. Configure and run Postman collection to configure PF and PA:
    - Configure Collection Variables:
      - Configure the CIAM IDP collection variables (ciamIdpIssuer, ciamIdpClientId, ciamIdpClientSecret).
        - You'll need to create an OIDC client and secret in your IdP.
        - The CIAM IdP Client will need to accept client credentials from post parameters. Alternatively PingFederate can be configured to send client credentials using HTTP Basic Auth.
        - You'll need to add the following redirect uri to your CIAM IdP Client (note that the variable component portion of the URL is provided by PingFederate once it is configured):
           ```
             https://sso.data-holder.local:3000/sp/{variable-component}/cb.openid
           ```
      - Configure cdrACCCTrustedCA
        - Cert up MTLS certificates as described [(here)](../docs/howtos/howto_generatecerts.md)
        - The cdrACCCTrustedCA value can then be obtained by running the following:
           ```
             openssl base64 -in path/to/pingidentity-cdr-sandbox/scripts/postman/cert/public.cer | tr -d '\n'
           ```
      - The remaining default values should be fine for a POC environment.
    - Run the collection found here [(link)](scripts/cdr-au.configure_pa_pf.postman_collection.json) to configure PingFederate and PingAccess.

## Test

Configure and run Postman collection to perform a test:
  - Default configuration should work.
  - Set up Postman certificates described [(here#update-the-postman-configuration)](../docs/postman.md#update-the-postman-configuration) for the following hosts/port:
    ```
      sso-mtls.data-holder.local:3000
    ```

  - Collection found here [(link)](scripts/cdr-au.test_pa_pf.postman_collection.json)

When testing, you will have generated a CDR initiation URL. Copy and paste this into Firefox, accept SSL errors, until the browser redirects to httpbin.org with a code parameter. Copy the code parameter back into the Postman request when exchanging the code for an access token.

## Shutdown steps

1. Navigate to the manual-deploy folder in terminal.
2. Run ./_stopall.sh

## Notes

To avoid conflicts with other deployments, the following endpoints have been configured with unique ports:
- PingFederate Admin: https://localhost:2222
- PingFederate Engine: https://localhost:2031
- PingAccess Admin: https://localhost:2000
- PingAccess Engine: https://localhost:3000 (may conflict depending on configuration of other PingAccess servers)
