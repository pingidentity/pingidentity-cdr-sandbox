# Quick Start Guide

The CDR Sandbox is an all-encompassing “black-box” environment created using Docker to deploy images in stable, network-enabled containers. The sandbox includes preconfigured Docker Compose YAML files for quick and easy deployment in a testing/lab scenario. For production deployments, a more comprehensive orchestration solution (eg. Kubernetes) is recommended.

## Prerequisites

* Root access to a Linux or MacOS machine with:
  * Git
  * Docker
  * At least 8GB of RAM available to docker
  
  
* A GitHub Account **[2 min]**<br>
https://github.com/join

* A working Ping Identity devops installation **[60 min; allow 1-2 days for credentials]**<br>
https://pingidentity-devops.gitbook.io/devops/getstarted

# Step by Step Guide

## 1. Clone the CDR Repository
While installing Ping Identity devops, you should have created a project folder in your home directory. We’re going to host the CDR sandbox here too:<br>
```sh
git clone https://github.com/pingidentity/pingidentity-cdr-sandbox.git ~/projects/cdr
```

 > Using ~/projects/cdr will make it easier to follow this guide. It will also make it easier for us to help you if you encounter issues.

## 2. Configure and Start the Stack

1.  Copy the cdr.env.template file to cdr.env
    
```sh
cp ~/projects/cdr/docker-compose/cdr.env.template ~/projects/cdr/docker-compose/cdr.env
```
2.  Add the following entries to the /etc/hosts file
```sh
127.0.0.1 sso-admin.data-holder.local
127.0.0.1 sso.data-holder.local
127.0.0.1 api.data-holder.local
127.0.0.1 dr.data-recipient.local
```

3.  Use docker-compose to bring the CDR Sandbox stack up:
    

  
```sh
docker-compose -f ~/projects/cdr/docker-compose/docker-compose.yaml up -d
```
  

4.  To display the server logs as the stack starts up, run the following command:
 
```sh
docker-compose -f ~/projects/cdr/docker-compose/docker-compose.yaml logs -f
```
(ctrl-c to exit)

5.  To display the status of each container, run one of the following commands:
```sh
docker ps
```

## 3. Run the Data Recipient Demonstration Application

The Data Recipient Demonstration Application (DR Client) has been provided for the purposes of demonstrating the basic Consumer Journey as detailed in the [Consumer Standards Consumer Experience Guidelines](https://consumerdatastandards.org.au/wp-content/uploads/2020/01/CX-Guidelines-v1.2.0-1.pdf).

  

The DR Client is designed to demonstrate the Data Recipient and Data Holder interaction throughout a consent lifecycle; specifically it implements the Consent, Authenticate and Authorise stages of the Consumer Journey - The Consent Flow.

![CDR Consent Flow](images/consent_flow.png)

The DR Client implements the three main roles as defined in the Consumer Data Rights standard:

1.  The Data Holder - AnyBank
2.  The Data Recipient - Account Link (ALink)
3.  The Customer - Alice Stone
    
The following steps will demonstrate how:  
-   To establish a consent with Alice’s bank: AnyBank
-   To use the ALink application to access transaction data
-   Alice can adjust and/or revoke her consent
    
Throughout the content lifecycle Alice will be able to verify the consent is being enforced by refreshing the Alink application and confirming whether the balances for each shared transaction account are visible or not.

The DR Client is already running as a service if you followed the steps detailed above. To confirm that the service is running enter:

  
```sh
docker ps -f "name=pingdirectory"
```
And verify that the value of STATUS is “healthy”

### Running the DR Client

1.  Open your web browser and goto:<br>
[http://dr.data-recipient.local:8080/](http://dr.data-recipient.local:8080/)

 > The sandbox includes a self-signed SSL certificate. You will need to accept the security warning to access the DR Client. 

2.  At the ALink Data Recipient login page provide the following:<br>
    * Username: astone
    * Password: password
    
3.  You should now be logged into ALink as the CDR Consumer “astone”
 
4.  Select the My Banks button (top right corner)
 
5.  From the drop down of Data Holders select “AnyBank” and click the Connect button

6.  Validate the Consent request and click Start. You will now be redirected to AnyBank to Authenticate and Authorise the consent.
    
7.  At the AnyBank Customer ID prompt enter in Alice’s bank identifier:

-   Customer ID: crn1
    
8.  At the SMS OTP field provide the value:

-   SMS OTP: 123456

 > The sandbox includes a mock SMS provider that will accept the OTP value of “123456” for testing purposes.

9. AnyBank will present the Authorisation prompt. Review the authorisation and confirm that it matches the Consent requested by ALink.

 > The right hand column is dynamic, being populated based upon scopes that are sent from the Data Recipient. For the brevity of the UI and demo the scopes are set to Basic Account Details and Transaction Details.

10.  Select the 1st the Transaction Account to share transaction data with ALink

11.  Click the Confirm button. Alison should be redirected back to ALink.

12.  On the Alink page the consent should be shown. The consent can be revoked from here.

13.  Click on the ALink logo to view the shared account balances.
 
14.  Repeat steps 5 to 13 specifying additional transaction accounts in step 10 to update the consent
    
15..  Note the extra Transaction Accounts and associated balances.

16.  Select the My Banks button and click the Revoke Consent button. You will receive an alert stating that the Consent has been revoked.
  
17.  To validate the revoked consent click on the ALink logo to view no more shared accounts.
    
The DR Client uses the following DR users and DH Customer Numbers. Any combination of Username and Customer Number can be used however only one at a time.

![Accounts Table](images/account_table.png)

## 4. Shut Down the Stack

When you no longer want to run the CDR Reference Sandbox, you can either stop the running stack, or bring the stack down.  
  

-   To stop the running stack without removing any of the containers or associated Docker networks:
    
```sh
docker-compose -f ~/projects/cdr/docker-compose/docker-compose.yaml stop
```
  

-   To remove all of the containers and associated Docker networks:
    

  
```sh
docker-compose -f ~/projects/cdr/docker-compose/docker-compose.yaml down
```
  

-   To completely remove the CDR Reference images and configuration:
    

  
```sh
docker system prune --force  
docker image prune --all --force  
rm -rf ~/projects/cdr
```
  

## 5. Other Considerations

## Environment Configuration (cdr.env)

The CDR Sandbox....<TODO>

  

SERVER_PROFILE_URL

Default Value: https://github.com/pingidentity/pingidentity-cdr-sandbox.git

  

SERVER_PROFILE_BRANCH=cdr-1.2-core-001

Default Value: cdr-1.2-core-001

  

BASE_HOSTNAME=

Default Value: data-holder.local

  

OPERATIONAL_MODE=

Default Value: STANDALONE

  

USER_BASE_DN=

Default Value :dc=data-holder,dc=com

  

BRAND1_BGCOLOR=

Default Value: rgb(50, 115, 220)

  

BRAND2_BGCOLOR=#373C41

Default Value:

  

DOLLAR=$

Default Value:

  

PF_AUD=[https://data-holder](https://data-holder)

Default Value:

  

PF_ISS=cdr-register

Default Value:

  

CDR_REGISTER-pki-base-url=[http://host.docker.internal:8084](http://host.docker.internal:8084)

Default Value:

  

CDR_REGISTER-ca-keystore-commonname=cdr-register.local

Default Value:

  

SOFTWARE_STATEMENT_ACCC_JWKS_ENDPOINT=[http://host.docker.internal:8084/registration/JWKS](http://host.docker.internal:8084/registration/JWKS)

Default Value:

  

SOFTWARE_STATEMENT_ACCC_ISSUER=cdr-register

Default Value:

  

SOFTWARE_STATEMENT_BANK_AUDIENCE=[https://data-holder](https://data-holder)

Default Value:

PF_BASE_HOST=sso.data-holder.local

Default Value:

  

PF_BASE_PORT=443

Default Value:

  

DA_CLIENTID=admin_delegator

Default Value:

  

BASE_URL=[http://dr.data-recipient.local:8080](http://dr.data-recipient.local:8080)

Default Value:

  

CDR_REGISTER_BASE_URL=[http://host.docker.internal:8084](http://host.docker.internal:8084)

Default Value:

  

PA_POST_CONFIGURATION_DELAY=20

Default Value:

  

DR_CLIENT-authorization_endpoint=[https://sso.data-holder.local/as/authorization.oauth2](https://sso.data-holder.local/as/authorization.oauth2)

Default Value:

  

DR_CLIENT-ss_redirect_uri=[http://dr.data-recipient.local:8080/*](http://dr.data-recipient.local:8080/*)

Default Value:

  

## Saving Server Configurations

For your initial deployment of the sandbox, we recommend you make no changes to the docker-compose.yaml file to ensure you have a successful first-time deployment. Any configuration changes you make will not be saved when you bring down the stack. For subsequent deployments, see [Saving your configuration changes](https://github.com/pingidentity/pingidentity-devops-getting-started/blob/master/docs/saveConfigs.md).
