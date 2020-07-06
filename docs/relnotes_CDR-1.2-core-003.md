# Release notes

## CDR Sandbox for OpenBanking, version CDR-1.2-core-003 (July 06, 2020)

### New Features
- (CDR-139) Create mockregister.data-holder.com.au DNS
- (CDR-132) CDR Mock Registry CORS configuration to allow public host
- (CDR-131) Upgrade PingFederate and PingAccess
- (CDR-129) Create Postman scripts for consent flow

### Compliance Alignment
- (CDR-87) 31.1.006 ACCC DCR - Enforce cdr:registration scope for mgmt
- (CDR-78) 18. End Points: Update openid-connect well-known
- (CDR-76) 17.2 Request Object: Sharing Duration
- (CDR-73) 15. Transport Security
- (CDR-68) 12.3.004: Refresh Token Duration
- (CDR-62) 11.2.011 Consent: Claims: sharing_expires_at
- (CDR-61) 11.2.010 Consent: Claims: refresh_token_expires_at
- (CDR-60) 11.2 Consent: Claims (revisit)
- (CDR-130) PingAccess to block introspect for access tokens

### Security
N/A

### Documentation
- (CDR-127) Document Postman Usage	CDR-127
- (CDR-128) Update Cert creation steps	CDR-128

### Resolved defects


- (CDR-141) 403 Error when accessing https://mockregister.data-holder.com.au/csrFlow
- (CDR-138) showcase settings have data-holder.come.au
- (CDR-137) Persistent grant not extending on subsequent consent requests
- (CDR-134) ID Token must not contain PII





