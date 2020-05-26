#!/bin/bash
# Create a dsconfig batch script to set up the delegator
THIS_SCRIPT=${0}

searchbasedn="dc=example,dc=com"
pingfedport=9031
pingfedhost_public=localhost
pingfedclientid_ds=pingdirectory
pingfedclientid_app=dadmin
script=delegated-admin.dsconfig
config=app/config.js

echo ""
echo "Enter the PingDirectory base DN for Delegated Admin resource data."
read -e -p "Search Base DN [$searchbasedn]:" input
searchbasedn=${input:-$searchbasedn}

echo ""
echo "Enter the public address of the PingFederate server where the app can redirect the user's browser to login."
read -e -p "Public PingFederate hostname or IP address [$pingfedhost_public]:" input
pingfedhost_public=${input:-$pingfedhost_public}

pingfedhost_private=$pingfedhost_public

echo ""
echo "Enter the private address of the PingFederate server reachable by PingDirectory to validate access tokens."
echo "Provide different public and private addresses if the PingFederate Server is not reachable on its public address by PingDirectory."
read -e -p "Private PingFederate hostname or IP address [$pingfedhost_private]:" input
pingfedhost_private=${input:-$pingfedhost_private}

echo ""
read -e -p "PingFederate port number [$pingfedport]:" input
pingfedport=${input:-$pingfedport}

echo ""
echo "PingDirectory Server must be configured as a client of PingFederate to validate access tokens."
echo "This client configuration requires a Client ID and a Client Secret."
read -e -p "PingFederate Client ID for PingDirectory Server [$pingfedclientid_ds]:" input
pingfedclientid_ds=${input:-$pingfedclientid_ds}

echo ""
while [ -z "$pingfedclientsecret" ]; do
  unset pingfedclientsecret
  prompt="PingFederate Client Secret for PingDirectory Server:"
  while IFS= read -p "$prompt" -r -s -n 1 char
  do
    if [[ $char == $'\0' ]]
    then
        break
    fi
    prompt='*'
    pingfedclientsecret+="$char"
  done
  printf "\n"
done

echo ""
echo "The Delegated Admin application must be configured as a client of PingFederate to obtain access tokens."
echo "This client configuration requires a Client ID but not a Client Secret."
read -e -p "PingFederate Client ID for the application [$pingfedclientid_app]:" input
pingfedclientid_app=${input:-$pingfedclientid_app}

sed -e "s/\${searchbasedn}/${searchbasedn}/g" \
    -e "s/\${pingfedhost}/${pingfedhost_private}/" \
    -e "s/\${pingfedport}/${pingfedport}/" \
    -e "s/\${pingfedclientid_ds}/${pingfedclientid_ds}/" \
    -e "s/\${pingfedclientsecret}/${pingfedclientsecret}/" \
   "delegated-admin-template.dsconfig" > ${script}

chmod 600 ${script}

sed -e "s/PF_HOST = 'localhost'/PF_HOST = '${pingfedhost_public}'/" \
    -e "s/PF_PORT = '9031'/PF_PORT = '${pingfedport}'/" \
    -e "s/DADMIN_CLIENT_ID = 'dadmin'/DADMIN_CLIENT_ID = '${pingfedclientid_app}'/" \
   "app/example.config.js" > ${config}

chmod 600 ${config}

echo ""
echo "A dsconfig batch script has been written to ${script}."
echo "A webapp config file has been written to ${config}."
echo "Please review the contents of these files and make any necessary adjustments."
echo ""
echo "Use dsconfig with the --batch-file option to apply the commands in the batch script to each PingDirectory or PingDirectoryProxy server instance in the topology."
echo "See dsconfig --help for further information and an example of applying a batch file."
echo ""
echo "WARNING: Running the batch script requires special consideration in an environment that includes replicated PingDirectory Servers."
echo "         Consult the Delegated Administration Guide before proceeding."
echo ""
