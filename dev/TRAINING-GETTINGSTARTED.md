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
127.0.0.1	spa.data-recipient.local
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

4) Run the following in order to stand up PingFederate and PingAccess, unconfigured:
- docker-compose up -d
- ./scripts/start-pd.sh
- ./scripts/start-pf-unconfigured.sh
- ./scripts/start-pa-unconfigured.sh
- ./scripts/start-pds.sh

5) When creating postman network certificates, use the create-postman-certs-dev.sh script instead.

