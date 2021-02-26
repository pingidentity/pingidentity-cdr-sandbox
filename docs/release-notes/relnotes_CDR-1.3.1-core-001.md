# Release notes

## CDR Sandbox for OpenBanking, version CDR-1.3.1-core-001

Release Date: Aug 04, 2020

### New Features

- **Added CDR 1.3.1 Compliance Requirements**
    - Concurrent Consent
        - CDR-151: produce refresh_token with metadata
        - CDR-152: Enrich introspection call with refresh token JWT metadata
        - CDR-156: Manage historical consent records
        - CDR-157: Verify CDR Arrangement ID is valid
        - CDR-158: Update ADR sample app for CDR Arrangement ID capabilities
        - CDR-159: Sample ADR app needs to display more context for concurrent requests
    - Pushed Authorization Request
        - CDR-153: Push Authorization Request
        - CDR-160: Validate request_uri is not in the request object for a PAR request
        - CDR-161: Enable client authentication for PAR
    - "cdr_arrangement_id" Requirements
        - CDR-154: CDR Arrangement Endpoint
        - CDR-162: Change CDR Arrangement Endpoint to POST method
        - CDR-163: rename CDR arrangement endpoint to revocation
        - CDR-175: update refresh token metadata to obtain cdr_arrangement_id from AT
- **Updated all images to latest Ping Products**
    - CDR-150: Upgrade PingDirectory docker image to latest
    - CDR-170: Update images for PF, PA, and PD
    - CDR-145: Move DCRM off servlets and onto DCRM interface in 10.1
    - CDR-147: Software_statement to only return on POST and PUT
    - CDR-148: Enable DELETE for DCRM
- **Kubernetes sample deployment**
    - CDR-165: Initial K8 sample commands

### Compliance Alignment

- CDR-63: 12.1 Tokens: ID Token
- CDR-69: 12.3 Tokens: Refresh Token
- CDR-70: 13.1 Identifier: sub value
- CDR-71: 13.2 CDR Arrangement ID
- CDR-77: 17.3 Request Object: Specifying an existing arrangement
- CDR-177: update test harness to provide id_token reflection
- CDR-178: update postman scripts

### Documentation

- CDR-166: Update Postman Docs to not validate certs

### Resolved Defects

- CDR-146: DCR clients created with "validate against all ATMs" configuration
- CDR-149: mock-register isn't persisting software-statement keystore instances.
- CDR-169: enable id_token encryption
- CDR-174: enforce id_token encryption algorithms in DCR
- CDR-176: update data recipient application to decrypt id_token for nonce validation
- CDR-179: update test harness to accept id token encryption algorithms for dcr
