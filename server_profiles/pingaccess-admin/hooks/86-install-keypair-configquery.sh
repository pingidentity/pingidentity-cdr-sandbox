
generate_post_data()
{
  cat <<EOF
{
  "alias": "${PA_CONSOLE_HOST}",
  "fileData": "$(cat network.p12 | base64)",
  "password": "P@ssword1"
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


curl -o public.pem http://cdrregister:8084/public/download

apk add openssl
openssl genrsa -out client.key 2048
openssl req -new -key client.key -out client.csr -subj "/CN=pingaccess-admin/O=My Company Name LTD./C=AU"

curl -X POST -H --silent -F csr=@client.csr -o csrresponse.p7b http://cdrregister:8084/public/sign

openssl pkcs7 -print_certs -in csrresponse.p7b -out csrresponse.pem
openssl pkcs12 -export -out network.p12 -inkey client.key -in csrresponse.pem -password pass:P@ssword1

echo $("generate_post_data") > install-cert.txt

keypairId=$(curl -X POST --silent --basic -u Administrator:${PA_ADMIN_PASSWORD_INITIAL} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' --data '@install-cert.txt' https://localhost:9000/pa-admin-api/v3/keyPairs/import --insecure | jq .id)

if [ -z "$keypairId" ] || [ $keypairId == null ]
then
  echo "Keypair not imported"
else
  echo "Keypair imported - ID: $keypairId"

  httpsEngineId=$(curl --silent --basic -u Administrator:${PA_ADMIN_PASSWORD_INITIAL} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' https://localhost:9000/pa-admin-api/v3/httpsListeners --insecure | jq '.items[] | select(.name=="CONFIG QUERY") | .id')

  engineName="CONFIG QUERY"
  echo $("generate_httpslistener_data") > configure-https.txt
  configureHttpsResponse=$(curl -X PUT --silent --basic -u Administrator:${PA_ADMIN_PASSWORD_INITIAL} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' --data '@configure-https.txt' https://localhost:9000/pa-admin-api/v3/httpsListeners/${httpsEngineId} --insecure)

  echo "Keypair assigned to Admin HTTPS listener: $httpsEngineId"
fi
