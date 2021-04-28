
generate_post_data()
{
  cat <<EOF
{
  "alias": "siteauthenticator-keypair-$RANDOM",
  "fileData": "$(cat network.p12 | base64)",
  "password": "P@ssword1"
}
EOF
}

generate_siteauthenticator_data()
{
  cat <<EOF
{
   "configuration": {"keyPairId": "$keypairId"},
   "name": "CDR-SiteAuthenticator",
   "className": "com.pingidentity.pa.siteauthenticators.MutualTlsSiteAuthenticator",
   "id": $siteAuthenticatorId
}
EOF
}


curl -o public.pem http://cdrregister:8084/public/download

openssl genrsa -out client.key 2048
openssl req -new -key client.key -out client.csr -subj "/CN=pingaccess-admin/O=My Company Name LTD./C=AU"

curl -X POST -H --silent -F csr=@client.csr -o csrresponse.p7b http://cdrregister:8084/public/sign

openssl pkcs7 -print_certs -in csrresponse.p7b -out csrresponse.pem
openssl pkcs12 -export -out network.p12 -inkey client.key -in csrresponse.pem -password pass:P@ssword1

echo $("generate_post_data") > install-cert.txt

keypairId=$(curl -X POST --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' --data '@install-cert.txt' https://localhost:9000/pa-admin-api/v3/keyPairs/import --insecure | jq .id)

if [ -z "$keypairId" ] || [ $keypairId == null ]
then
  echo "Keypair not imported"
else
  echo "Keypair imported - ID: $keypairId"

  siteAuthenticatorId=$(curl --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' https://localhost:9000/pa-admin-api/v3/siteAuthenticators --insecure | jq '.items[] | select(.name=="ADR-MTLS-To-DataHolder") | .id')

  echo $("generate_siteauthenticator_data") > configure-siteauthenticator.txt
  configureHttpsResponse=$(curl -X PUT --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' --data '@configure-siteauthenticator.txt' https://localhost:9000/pa-admin-api/v3/siteAuthenticators/${siteAuthenticatorId} --insecure)

  echo "Keypair assigned to Admin HTTPS listener: $siteAuthenticatorId"
fi
