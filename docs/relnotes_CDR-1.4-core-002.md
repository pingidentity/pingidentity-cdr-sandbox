# Release notes

## CDR Sandbox for OpenBanking, version CDR-1.4-core-002 (Oct 12, 2020)

### New Features
- CDR-11	Configuration Injection method updated to API
- CDR-10	Replaced Grant Management to use PingDirectory Consent APIs directly
- CDR-189	Acheive FAPI RW Certification for new CDR testing profile (https://www.certification.openid.net/plan-detail.html?plan=KbvzUzkOkII3W&public=true)

### Compliance Alignment
- CDR-57	9.3 Client Authentication: Data Recipients calling Data Holders
- CDR-58	10. OIDC Client Types
- CDR-59	11.1 Consent: OIDC Scopes
- CDR-67	12.2 Tokens: Access Token
- CDR-251	Track FAPI compliance issue #820
- CDR-261	fapi-rw-id2-ensure-valid-pkce-succeeds
- CDR-276	PKCE for PAR
- CDR-284	paragentless adapter set to 60 seconds, but PAR servlet indicating it is 30 seconds
- CDR-285	fapi-rw-id2-ensure-valid-pkce-succeeds
- CDR-289	PAR to return invalid_request_object for invalid request uri

### Security
NIL

### Documentation
- CDR-263	Update the Getting Started Guide w/ New Key Request Process

### Resolved defects
- CDR-173	cdr hooks are not leverage correct admin password
- CDR-194	not possible to retrieve arrangement_id's for a given user
- CDR-204	PF Requires "Sector Identifier URI" for Clients with Multiple redirectURLs
- CDR-242	PingDataConsole is not pulling from GCR
- CDR-259	iss missing in ACCC DCR Request JWT
- CDR-260	MTLS not enforced without client_id parameter
- CDR-265	DCR POST not enforcing MTLS
- CDR-266	Postman Code Exchange Fails
- CDR-269	Investigate resilient RT
- CDR-270	PERSISTENT GRANT IDLE TIMEOUT is not disabled
- CDR-275	Improve Refresh Token Expiry
- CDR-279	Consent API Access Grant Manager plugin not filtering out revoked consents
- CDR-282	PF Engines not pulling config over cluster
- CDR-286	Enforce PKCE
- CDR-287	Add response header indicating MTLS has been verified
- CDR-288	Userinfo and CDR Arrangement Endpoints not enforcing MTLS
- CDR-290	Add idle timeout to consent api pooled connections
- CDR-291	PF Consent API Grants losing connections to PD Consent APIs
