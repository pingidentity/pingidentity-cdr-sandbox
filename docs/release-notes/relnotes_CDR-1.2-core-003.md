# Release Notes

## CDR Sandbox for OpenBanking, Version CDR-1.2-core-003

Release Date: July 06, 2020

### New Features

- CDR-129: Create Postman scripts for consent flow and Dynamic Client Registration
- CDR-131: Upgrade PingFederate to 10.1 and PingAccess to 6.1
- CDR-132: CDR Mock Registry CORS configuration to allow public host
- CDR-139: Create mockregister.data-holder.com.au DNS

### Compliance Alignment

- CDR-60: Consent Claims revisit:
- CDR-61: Consent Claims: refresh_token_expires_at
- CDR-62: Consent Claims: sharing_expires_at
- CDR-68: Refresh Token Duration
- CDR-73: Transport Security MTLS, HoK:
- CDR-76: Request Object: Sharing Duration
- CDR-78: End Points: Update openid-connect well-known
- CDR-87: ACCC DCR - Enforce cdr:registration scope for mgmt
- CDR-130: PingAccess to block introspect for access tokens
- CDR-136: Changes to auth_time
- CDR-134: ID Token must not contain PII

### Documentation

- CDR-128: Update Cert creation steps
- CDR-127: Document Postman Usage

### Resolved defects

- CDR-137: Persistent grant not extending on subsequent consent requests
- CDR-138: Showcase instance settings has incorrect domain
- CDR-141: 403 Error when accessing https://mockregister.data-holder.com.au/csrFlow
