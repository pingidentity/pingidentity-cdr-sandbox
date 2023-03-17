# Setup Postman to Run Consent Scripts

The CDR Sandbox includes a Postman collection that includes all the REST calls, in order, that follows the Consumer user journey as implemented in the Data Recipient Demonstration Application (https://github.com/pingidentity/pingidentity-cdr-sandbox/blob/master/docs/quickstart.md#3-run-the-data-recipient-demonstration-application)

## Prerequisites

- Postman 7 or above
    - Download Postman [here](https://www.postman.com/downloads/)
    - MTLS Authentication keys. [Generating Certs](howtos/howto_generatecerts.md)

!!! Tip
    Using ~/projects/cdr will make it easier to follow this guide. It will also make it easier for us to help you if you encounter issues.

## Update the Postman Configuration

1. Open the Postman Settings page
1. On the Certificates tab
    - Add a Certificate with the following details
         - Host: sso.data-holder.local
         - PFX File: ~/projects/cdr/scripts/postman/cert/network.p12
         - Passphrase: P@ssword1
    - Add a 2nd Certificate with the following details
         - Host: api.data-holder.local
         - PFX File: ~/projects/cdr/scripts/postman/cert/network.p12
         - Passphrase: P@ssword1
    - Add a 3rd Certificate with the following details
         - Host: mockregister.data-holder.local
         - PFX File: ~/projects/cdr/scripts/postman/cert/network.p12
         - Passphrase: P@ssword1

## Import the Consent Flow Postman Collection

1. Click on the "Import" button
1. Click "Upload Files"
1. Click on the Import button
1. Select the Consent Flow Postman Collection

    !!! Note "File location"
        ~/projects/cdr/scripts/postman/cdr-au.consent.postman_collection.json

1. Click the "Import" Button

## Execute a Consent Flow Runner Test

1. Click the "Runner" button
1. Select "Consent Flow v1.1 - External Consent" in the All Collections window
1. Click  the "Run Consent Flow" button
1. Verify all tests passed

## Manually Execute Consent Requests

!!! Note
    To execute individual requests you must specify a Postman "Environment". If none exists please create an empty Environment and ensure its selected.

1. Expand the "Consent Flow v1.1 - External Consent" collection in the left pane
1. Expand S001 Create Client | S001.T001 Create Client
1. Select each request and click "Send" in order
1. Expand S002 Consent Flow - Happy Scenario
1. Expand each section and select each request and click "Send" in order
    - Verify each request result returns an expected result
1. Check the response from request "S002.T004.001 - Get account list" and ensure representative data is returned.
