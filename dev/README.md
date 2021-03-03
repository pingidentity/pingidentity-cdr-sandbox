
1) Add the following hostnames: 
```
127.0.0.1       pingdatagovernancepap
127.0.0.1       pingdatagovernance
127.0.0.1       mock-dh-api
127.0.0.1       agentless-consentapp
127.0.0.1       pingdirectory
127.0.0.1       cdrregister
127.0.0.1       mockregister.data-holder.local
127.0.0.1       consent.data-holder.local
127.0.0.1       pd.data-holder.local
127.0.0.1       sso-local.data-holder.local
127.0.0.1       sso.data-holder.local
127.0.0.1       api.data-holder.local
127.0.0.1       dr.data-recipient.local
127.0.0.1       spa.data-recipient.local
```

2) Copy Ping software binaries to the binaries folder. Software versions currently tested:
- directory-8.2.0.0-EA-image.zip
- pingaccess-6.2.0.zip
- pingfederate-10.2.1.zip

3) Run ./start-all.sh to start all services.

Alternatively run the following in order:
- docker-compose up -d
- ./scripts/start-pd.sh
- ./scripts/start-pf.sh
- ./scripts/start-pa.sh

4) Update the following Postman Collection Variables:
- cdr-register-testharness-host = https://mockregister.data-holder.local:6443
- dh-idp-host = https://sso-local.data-holder.local:6443
- dh-api-host = https://api.data-holder.local:6443/cds-au
- dh-dir-host = https://pd.data-holder.local:6443
5) Run ./stop-all.sh to stop all services.
