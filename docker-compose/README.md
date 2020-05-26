# Ping CDR Reference Implementation

## CDR Getting Started
1) Edit the cdr.env file to ensure it matches the environment
	* Ensure that the ${BASE_HOSTNAME} is set correctly
2) Create host entries
	* 127.0.0.1  sso-admin.${BASE_HOSTNAME} sso.${BASE_HOSTNAME} api.${BASE_HOSTNAME} dr.data-recipient.local
3) URLs
	* https://sso.${BASE_HOSTNAME} - Used for CDR flows
	* https://sso-admin.${BASE_HOSTNAME} - Used for CDR flows
	* https://api.${BASE_HOSTNAME} - Bank API's provided by deepthought (https://github.com/bizaio/deepthought)
	* http://dr.data-recipient.local - Sample DR application
4) Build Apache Tomcat Docker - pingtomcat (if required)
	* git clone https://github.com/arnaudlacour/giterize.git
	* cd giterize
	* ./build.sh -i tomcat:9-jdk8 -t pingidentity/apache-tomcat:9-jdk8 -p SERVER_PROFILE

## Administrator Consoles
Once you see that the containers are healthy in `docker ps`

To see the PingFederate management console

* Go to [https://localhost:9999/pingfederate/app](https://localhost:9999/pingfederate/app)
* Log in with `Administrator / 2FederateM0re

To see the PingAccess management console

* Go to [https://localhost:9000](https://localhost:9000)
* Log in with `Administrator / 2FederateM0re`
* Note: You will be asked to accept license agreement and change password

To see the PingDirectory management console

* Go to [https://localhost:8443/console](https://localhost:8443/console)
* Log in with:
 server: pingdirectory
 user: Administrator
 password: 2FederateM0re

PingDirectory exposes LDAP traffic via an LDAPS port 1636.

* Navigate to [https://localhost:1636/dc=example,dc=com](https://localhost:1636/dc=example,dc=com)


## DevOps Getting started
Please refer to the [Docker Compose Overview](./) for details on how to start, stop, cleanup stacks.

To start the stack, from the directory this file is in run:

`docker-compose up -d`

Watch the directories initialize with:

`docker-compose logs -f`

To stand up multiple containers, run compose with the `--scale` argument:

`docker-compose up --scale pingdirectory=3 --pingfederate=2`

## Cleaning up

To bring the stack down:

`docker-compose down`
