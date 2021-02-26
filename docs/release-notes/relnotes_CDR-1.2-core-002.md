# Release Notes

## CDR Sandbox for OpenBanking, version CDR-1.2-core-002

Release Date: June 16, 2020

### New Features

- CDR-19: Update Users in DR app.
- CDR-33: Tag images inline with version releases.
- CDR-97: Update certificates to be self-signed and match documented hostname
- CDR-120: Add Postman DCR Test Scripts
- CDR-126: Create Release-specific docker-compose directories/files

### Compliance Alignment

- CDR-65: 12.1.004 and 12.1.009: s_hash in id_token
- CDR-66: 12.2.002: Configure access token validity between 2 and 10 minutes.
- CDR-72: 14. LoA's: Correct configuration to align with specifications
- CDR-81: 27.1 Restrict ID Token encryption algorithms
- CDR-86: 30.1.003: Return appropriate error message for invalid DCR data
- CDR-114: HoK validation should verify that there is only 1 Authorization header

### Security

- CDR-107: Review/Remediate Git Security Alerts
- CDR-111: Manage security vulnerabilities with docker images
- CDR-118: Fix Security Vulnerabilities in DeepThought images

### Documentation

- CDR-125: Add Mockregister hosts file entry to documentation

### Resolved defects

- CDR-109: PingAccess incorrectly enforcing MTLS
- CDR-112: Update Authoriation screen to reference ALink
- CDR-113: Fix "I Confirm" In Consent Screen
- CDR-121: cdr.env.template should not reference host.docker.internal
- CDR-123: Removing grant record did not revoke the AT
- CDR-26: Fix the footer in the DR client
