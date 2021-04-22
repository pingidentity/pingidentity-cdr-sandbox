# Release Notes

## CDR Sandbox for OpenBanking, version CDR-1.7-core-002

Release Date: April 22, 2021

### New Features
- CDR-353: Developer scripts to launch the sandbox without Docker

#### Added Support for CDR 1.7
NIL

#### Added Data Recipient Functionality
- CDR-362: New Data Recipient application for Data In!

#### Miscellaneous Improvements
- CDR-359: Configured the data-holder to support multiple brands
- CDR-361: Updated Integration Kit to expose cdr_arrangement_id when introspecting AT's internally


### Compliance Alignment
NIL

### Security

- CDR-357: Docker images now run as non-root user "ping"
- CDR-360: Fixed issue where DCR audience validation was not performed for POST and PUT

### Documentation
NIL

### Resolved Defects
- CDR-365: AdminAPI was not clearing
