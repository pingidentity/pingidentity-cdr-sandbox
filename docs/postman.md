# Setup Postman to Run Consent Scripts
The CDR Sandbox includes a Postman collection that includes all the REST calls, in order, that follows the Consumer user journey as implemented in the Data Recipient Demonstration Application (https://github.com/pingidentity/pingidentity-cdr-sandbox/blob/master/docs/quickstart.md#3-run-the-data-recipient-demonstration-application)

## Prerequisites

* Postman 7 or above

   https://www.postman.com/downloads/
* MTLS Authentication keys

   https://github.com/pingidentity/pingidentity-cdr-sandbox/blob/master/docs/howto_generatecerts.md

> Using ~/projects/cdr will make it easier to follow this guide. It will also make it easier for us to help you if you encounter issues.

## Update the Postman Configuration 
1. Open the Postman Settings page
2. On the General tab
   * Disable "Automatically follow redirects"
   * Disable "SSL Certificate Verification"
3. On the Certificates tab
   * Add a Certificate with the following details
      * Host: sso.data-holder.local
      * PFX File: ~/projects/cdr/scripts/postman/cert/network.p12
      * Passphrase: P@ssword1
   * Add a 2nd Certificate with the following details
      * Host: api.data-holder.local
      * PFX File: ~/projects/cdr/scripts/postman/cert/network.p12
      * Passphrase: P@ssword1
      
## Import the Consent Flow Postman Collection
1. Click on the "Import" button
2. Click "Upload Files"
3. Click on the Import button
4. Select the Consent Flow Postman Collection

   ~/projects/cdr/scripts/postman/cdr-au.consent.postman_collection.json
5. Click the "Import" Button
   
## Execute a Consent Flow Runner Test
1. Click the "Runner" button
2. Select "Consent Flow" in the All Collections window
3. Click  the "Run Consent Flow" button
4. Verify all tests passed

## Manually Execute Consent Requests

> Note: To execute individual requests you must specify a Postman "Environment". If none exists please create an empty Environment and ensure its selected.

1. Expand the Consent Flow collection in the left pane
2. Expand S001 Create Client | S001.T001 Create Client
3. Select each request and click "Send" in order
4. Expand S002 Consent Flow - Happy Scenario
5. Expand each section and select each request and click "Send" in order
   > Verify each request result returns an expected result

6. Check the respoce from request "S002.T004.001 - Get account list" and ensure representative satat is returned.
