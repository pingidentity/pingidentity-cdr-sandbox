# Release Notes

<<<<<<< HEAD
## CDR Sandbox for OpenBanking, version CDR-1.9-core-001

Release Date: June 04, 2021

### New Features
- cdr-359: Multi branding configuration for DataSync, PingAccess, and PingFederate configuration. The sandbox now has 2 ADHs/Banks configured in a single deployment.
- cdr-361: Expose cdr_arrangement_id when introspecting AT's. This can be useful for internal introspection of AT's.
- cdr-369: Add http header injected client authentication into MTLS and HoK policies. This allows load balances to terminate SSL whereas before it had to be terminated at PingAccess.
- cdr-371: Mock Register to add certificate extended key usage for client authentication and server authentication

#### Added Support for CDR 1.7
NIL

#### Added Data Recipient Functionality
- cdr-363: Add second data holder to data-in-application

#### Miscellaneous Improvements
- cdr-364: Improvements to data structure to meet best practices and also align to P1AS deployments.


### Compliance Alignment
NIL

### Security
- cdr-360: DCR PUT and POST not validating request JWT audience against incoming hostname


### Documentation
NIL

### Resolved Defects
- cdr-365: CDR AdminAPI not patching Software Product metadata object
