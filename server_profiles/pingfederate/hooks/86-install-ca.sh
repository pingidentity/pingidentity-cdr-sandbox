generate_post_data()
{
  cat <<EOF
{
  "fileData": "$(cat public.pem | sed '1,1d' | sed '$ d')"
}
EOF
}

curl -o public.pem http://cdrregister:8084/public/download

echo $("generate_post_data") > install-cert.txt

response=$(curl -X POST --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingFederate' --data '@install-cert.txt' https://localhost:9999/pf-admin-api/v1/certificates/ca/import --insecure)

idVal="$(echo $response | jq .id)"

if [ -z "$idVal" ] || [ $idVal == null ]
then
  echo "Cert not imported"
else
  echo "Cert imported - ID: $idVal"
fi
