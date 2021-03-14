# Release Notes

## CDR Sandbox for OpenBanking, version CDR-1.7-core-001

Release Date: March 15, 2021

### New Features

#### Added Support for CDR 1.7 
- CDR-344: Confirmation of handover

#### Added Data Recipient Functionality
- CDR-346: Data-out and data-in convergence

#### Miscellaneous Improvements
- CDR-350:	Access Token retrieval management for Data In CDRInjectDataHolderTokenRule
- CDR-351:	Default new 10.2 client options


### Compliance Alignment
NIL

### Security

- CDR-355:	Requested scopes require validation

### Documentation
- CDR-356: Update Quickstart with Data-In requirements and run through


### Resolved Defects
- CDR-345	incorrect placement of cdr_arrangement_id claim
- CDR-348	Data In PF Arrangements Endpoint not validating aud correctly
- CDR-349	Should not store Access Token JWT's for Data In
- CDR-352	Revocable JWT Bearer Token not compatible with 10.2 SDK



