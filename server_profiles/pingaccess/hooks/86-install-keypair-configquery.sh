
generate_post_data()
{
  cat <<EOF
{
  "alias": "config-query",
  "city": "Melbourne",
  "commonName": "pingaccess-admin",
  "country": "AU",
  "keyAlgorithm": "RSA",
  "keySize": "2048",
  "organization": "Ping Identity",
  "organizationUnit": "APAC",
  "signatureAlgorithm": "SHA256withRSA",
  "state": "VIC",
  "validDays": "365"
}
EOF
}

generate_httpslistener_data()
{
  cat <<EOF
{
   "name": "$engineName",
   "keyPairId": $keypairId,
   "useServerCipherSuiteOrder": true,
   "restartRequired": false
}
EOF
}

echo $("generate_post_data") > install-cert.txt

keypairId=$(curl -X POST --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' --data '@install-cert.txt' https://localhost:9000/pa-admin-api/v3/keyPairs/generate --insecure | jq .id)

if [ -z "$keypairId" ] || [ $keypairId == null ]
then
  echo "Keypair not imported"
else
  echo "Keypair imported - ID: $keypairId"

  httpsEngineId=$(curl --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' https://localhost:9000/pa-admin-api/v3/httpsListeners --insecure | jq '.items[] | select(.name=="CONFIG QUERY") | .id')

  engineName="CONFIG QUERY"
  echo $("generate_httpslistener_data") > configure-https.txt
  configureHttpsResponse=$(curl -X PUT --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' --data '@configure-https.txt' https://localhost:9000/pa-admin-api/v3/httpsListeners/${httpsEngineId} --insecure)

  echo "Keypair assigned to Config HTTPS listener: $httpsEngineId"
fi

