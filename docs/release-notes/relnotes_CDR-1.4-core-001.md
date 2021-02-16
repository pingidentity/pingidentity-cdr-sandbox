# Release Notes

## CDR Sandbox for OpenBanking, version CDR-1.4-core-001

Release Date: Aug 24, 2020

### New Features

- **Added CDR 1.4 Compliance Requirements**
    - CDR-198: Update OpenID Connect Metadata Discovery non-normative example
    - CDR-247: Joint accounts demonstration
    - CDR-248: Load bank account data with owner=false
    - CDR-249: Update consent page in accordance to the CX guidelines for Joint Accounts

- **Conformance Testing for FAPI Read/Write OPs - CDR Profile**
    - CDR-209: fapi-rw-id2-ensure-request-object-with-long-nonce
    - CDR-210: fapi-rw-id2-ensure-request-object-with-long-state
    - CDR-212: fapi-rw-id2-ensure-mtls-holder-of-key-required
    - CDR-217: fapi-rw-id2-refresh-token
    - CDR-218: fapi-rw-id2-par-attempt-invalid-redirect_uri
    - CDR-219: fapi-rw-id2-par-attempt-reuse-request_uri
    - CDR-220: fapi-rw-id2-par-attempt-to-use-expired-request_uri
    - CDR-221: fapi-rw-id2-par-authorization-request-containing-request_uri
    - CDR-222: fapi-rw-id2-par-attempt-to-use-request_uri-for-different-client
    - CDR-223: fapi-rw-id2-par-pushed-authorization-url-as-audience-in-request-object
    - CDR-224: fapi-rw-id2-ensure-request-object-without-exp-fails
    - CDR-225: fapi-rw-id2-ensure-request-object-without-scope-fails
    - CDR-226: fapi-rw-id2-ensure-expired-request-object-fails
    - CDR-227: fapi-rw-id2-ensure-request-object-with-bad-aud-fails
    - CDR-228: fapi-rw-id2-ensure-authorization-request-without-request-object-fails
    - CDR-229: fapi-rw-id2-ensure-matching-key-in-authorization-request
    - CDR-230: fapi-rw-id2-ensure-request-object-signature-algorithm-is-not-none
    - CDR-231: fapi-rw-id2-ensure-signed-request-object-with-RS256-fails
    - CDR-234: fapi-rw-id2-par-authorization-request-containing-request_uri-form-param

- **New Consent Storage Model**
    - CDR-186: enable cdr-arrangement-id bound AT subject
    - CDR-192: update consent adapter to revoke grants based on userId as primary key and arrangementId as secondary key
    - CDR-193: update cdr arrangement id to search client_id as primary key and cdr_arrangement_id as secondary key
    - CDR-238: Enable PF Grant Management Service
    - CDR-240: Sample DR application change how it reads cdr_arrangement_id
    - CDR-254: Create Consent API persistent grant storage

- **Updated Product Images**
    - CDR-256: Updated PingFederate, PingAccess and PingDirectory to 20200824

- **Compliance Alignment**
    - CDR-197: Reduce available scopes
    - CDR-199: Update OpenID Connect Metadata description for id_token_encryption_alg_values_supported
    - CDR-200: Update the Authorisation endpoint non-normative example to align to FAPI R/W 5.2.2
    - CDR-203: userinfo_signing_alg_values_supported Missing in wellknown
    - CDR-213: Allow configuration to alter PingAccess restriction for request parameter size
    - CDR-233: validate client_id and response_type in Request Object
    - CDR-235: PingFederate returning "400" errors instead of "invalid_request" or "access_denied"
    - CDR-236: validate client_assertion has aud=token endpoint
    - CDR-237: request_uri validation error code issue for par
    - CDR-245: OIDC metadata returning "request_uri_parameter_supported": false

### Security

- CDR-239: able to send fictitious values when selecting accounts
- CDR-241: authentication flow not validating the same customer logged in when specifying an arrangement_id

### Documentation

- CDR-246: Remove requirement to set up Postman preferences

### Resolved defects

- CDR-183: Persisted mock register keys have incorrect public certs
- CDR-185: cdr_arrangement_id not persisted in grant
- CDR-190: PA certificate not being provisioned
- CDR-191: grant management site not working after concurrent request
- CDR-196: Fix the statement for CDR Arrangement ID incorrectly mentions the ID Token
- CDR-205: Obtain acr values from "value" member
- CDR-206: handle absent sharing duration
- CDR-207: JWKS needs to include encryption key
- CDR-208: Cancel button on user id screen not working
- CDR-232: Calling CDR Register API's should be over MTLS
- CDR-255: User info stopped returning givenName and sn
