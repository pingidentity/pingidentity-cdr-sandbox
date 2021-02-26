# Release Notes

## CDR Sandbox for OpenBanking, version CDR-1.6-core-001

Release Date: Feb 19, 2021

### New Features

#### Added Meta Cache Management

- CDR-305: Poll Registry for DR Status
- CDR-321: Metadata Cache Retrieval
- CDR-322: PingAccess enforce inactive ADR status
- CDR-323: Create API to receive critical metadata update notification
- CDR-324: Update Metadata Cache sync process to handle Remove events

#### Added Data Holder Initiated Consent Revocation

- CDR-36:  Notification to DR
- CDR-313: Revocation - DataHolder to Data Recipient
- CDR-317: Create CDR Arrangements Endpoint for sample ADR client
- CDR-329: Create Access Token Manager for DH to DR scenarios

#### Added Fine Grained Policy Enforcement and Decision Point (PEP/PDP)

- CDR-296: Add Ping Data Gov to the SandBox.

#### Miscellaneous Improvements

- CDR-168: DataConsole needs image from GCR
- CDR-262: Monitor STAGING-11243 in Ping Jira
- CDR-280: Expose PingDirectory API's through PingAccess
- CDR-283: Configure PingFederate client authentication to PingDirectory for Consent API Grant Mgt
- CDR-312: Create and configure mock API's
- CDR-315: update cdr register test harness to v2
- CDR-318: Persist data recipient data in mock register v2
- CDR-319: Update config for PF 10.2 and PA 6.2
- CDR-326: Separate TLS fqdn from MTLS
- CDR-330: HTTP Pooling eviction configuration for DataSync
- CDR-331: implement transactions mock api
- CDR-335: Update docker images
- CDR-336: GET software_statement member for DCRM request
- CDR-337: Optimise PA modules - AT retrieval

### Compliance Alignment

- CDR-83:  27. ACCC DCR - Request JWT consumption
- CDR-320: CDR Arrangement ID Validation for PAR for PF 10.2
- CDR-316: Test client_assertion aud values for 1.6 compliance
- CDR-311: Add /register uri mapping to /as/clients.oauth2
- CDR-310: CDR Register 1.2.3 changes

### Security

NIL

### Documentation

- CDR-341: Updated Administration documentation to include Data Gov login

### Resolved Defects

- CDR-308: request_object_signing_alg optional and default to PS256
- CDR-309: software_roles should be single value
- CDR-314: CDR IK fails in 10.2 with PAR
- CDR-325: Fix PingFederate configuration hardcoded entryUUID values
- CDR-327: Updating Consent via DR app removes consent
- CDR-328: Follow up IK request to fix CDR Grant Management HttpPatch
- CDR-333: DCR not updating all attributes
- CDR-334: AT Revocation not working
- CDR-339: S004.T009.002.004 - Initiate PAR is returning 403 and our tests expect 400.
- CDR-340: S002.T001.003 - Initiate Consent Request - is failing
