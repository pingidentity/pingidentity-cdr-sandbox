# Administration Consoles

Once you see that the containers are healthy in `docker ps`

## PingFederate

* Go to [https://localhost:9999/pingfederate/app](https://localhost:9999/pingfederate/app)
* Log in with `Administrator / 2FederateM0re'

## PingAccess

* Go to [https://localhost:9000](https://localhost:9000)
* Log in with 'Administrator / 2FederateM0re'
* Note: You may be asked to accept license agreement and change password

## PingDataGovernance Symphonic PAP

* Go to [https://localhost:8443](https://localhost:8443)
* Log in with 'Admin / password123'

## PingDirectory

* Go to [https://localhost:8443/console](https://localhost:8443/console)
* Log in with:
 server: pingdirectory
 user: Administrator
 password: 2FederateM0re

## PingDirectory LDAPs Traffic

* PingDirectory exposes LDAP traffic via an LDAPS port 1636
* Navigate to [https://localhost:1636/dc=example,dc=com](https://localhost:1636/dc=example,dc=com)
