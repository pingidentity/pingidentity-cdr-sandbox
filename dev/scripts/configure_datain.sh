echo "Substituting PD configuration..."

while read line; do
    export $line
done < cdr.env

while read line; do
    export $line
done < pd.env

dcrActionId=$(curl -X GET --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingFederate' https://pingfederate:9999/pf-admin-api/v1/idp/adapters/ADRDataInSelector/actions --insecure | jq -r '.items[] | select(.name=="Execute Dynamic Client Registration") | .id')

dcrResponseCode=

if [ -z "$dcrActionId" ] || [ $dcrActionId == null ]
then
  echo "Could not find DCR action ID"
else
  echo "DCR Action ID found - ID: $dcrActionId"
  
  echo "Invoking action https://pingfederate:9999/pf-admin-api/v1/idp/adapters/ADRDataInSelector/actions/${dcrActionId}/invokeAction"
  
  dcrResponseCode=$(curl -X POST --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --write-out '%{http_code}' --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingFederate' https://pingfederate:9999/pf-admin-api/v1/idp/adapters/ADRDataInSelector/actions/${dcrActionId}/invokeAction --insecure  --output /dev/null)
  
fi

if test $dcrResponseCode == "200" 
then
  echo "DCR Response: Success! Registering client now..."
    
  registerActionId=$(curl -X GET --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingFederate' https://pingfederate:9999/pf-admin-api/v1/idp/adapters/ADRDataInSelector/actions --insecure | jq -r '.items[] | select(.name=="Persist Configuration") | .id')
    
  if [ -z "$registerActionId" ] || [ $registerActionId == null ]
  then
    echo "Could not find Register action ID"
  else
    echo "Register Action ID found - ID: $registerActionId"
  
    echo "Invoking action https://pingfederate:9999/pf-admin-api/v1/idp/adapters/ADRDataInSelector/actions/${registerActionId}/invokeAction"
  
    registerResponse=$(curl -X POST --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingFederate' https://pingfederate:9999/pf-admin-api/v1/idp/adapters/ADRDataInSelector/actions/${registerActionId}/invokeAction --insecure)
    
    echo "Client Registration Response: $registerResponse"
  fi
fi
