# Pre-requisites #

- Unix/linux based operating system (tested on macos). 
- docker engine 20.10.2 and docker-compose installed (tested with docker engine 20.10.2).
- Ping Identity devops registration and setup (click [here](https://devops.pingidentity.com/get-started/devopsRegistration/))
- Java 11 (e.g. [adoptjdk](https://adoptopenjdk.net/)) with JAVA_HOME and PATH variables
- Packages installed: unzip, curl, jt, sed, envsubst (gettext)

# Steps #

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
127.0.0.1       sso.data-holder.local
127.0.0.1       api.data-holder.local
127.0.0.1       dr.data-recipient.local
127.0.0.1       spa.data-recipient.local
127.0.0.1	sso.internal
127.0.0.1	sso-2.internal
```

2) Git clone this repository and navigate to the /dev folder.

```
git clone https://github.com/pingidentity/pingidentity-cdr-sandbox.git
cd pingidentity-cdr-sandbox
git checkout edge (currently only available in edge)
cd dev
```

3) Copy Ping software binaries to the binaries folder. Software versions currently tested:
- PingDirectory-8.2.0.2.zip
- pingaccess-6.2.0.zip
- pingfederate-10.2.1.zip
- PingDataSync-8.2.0.2.zip

4) Run:
- docker-compose up -d
- Wait 30 seconds for the CDR Register to start
- ./start-all.sh

Alternatively run the following in order:
- docker-compose up -d
- Wait 30 seconds for the CDR Register to start
- ./scripts/start-pd.sh
- ./scripts/start-pf.sh
- ./scripts/start-pa.sh
- ./scripts/start-pds.sh
- ./scripts/configure_datain.sh

5) When creating postman network certificates, use the create-postman-certs-dev.sh script instead.

6) Run postman scripts or Data In module, starting at: https://spa.data-recipient.local:6443

7) Run ./stop-all.sh to stop all services.
