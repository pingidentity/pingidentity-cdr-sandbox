# Administration Consoles
Once you see that the containers are healthy in `docker ps`

## PingFederate management console

* Go to [https://localhost:9999/pingfederate/app](https://localhost:9999/pingfederate/app)
* Log in with `Administrator / 2FederateM0re'

## PingAccess management console

* Go to [https://localhost:9000](https://localhost:9000)
* Log in with 'Administrator / 2FederateM0re'
* Note: You will be asked to accept license agreement and change password

## PingDirectory management console

* Go to [https://localhost:8443/console](https://localhost:8443/console)
* Log in with:
 server: pingdirectory
 user: Administrator
 password: 2FederateM0re

    ### PingDirectory exposes LDAP traffic via an LDAPS port 1636.

    * Navigate to [https://localhost:1636/dc=example,dc=com](https://localhost:1636/dc=example,dc=com)
