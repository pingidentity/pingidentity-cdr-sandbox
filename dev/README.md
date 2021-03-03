
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

2) Run ./start-all.sh to start all services.

Alternatively run the following in order:
- docker-compose up -d
- ./scripts/start-pd.sh
- ./scripts/start-pf.sh
- ./scripts/start-pa.sh

3) Run ./stop-all.sh to stop all services.
