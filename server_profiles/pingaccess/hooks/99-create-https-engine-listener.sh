
generate_post_data()
{
  cat <<EOF
{
    "port": 443,
    "name": "HTTPS Engine Listener",
    "secure": true
}
EOF
}

echo $("generate_post_data") > install-https-enginelistener.txt

engineListenerId=$(curl -X POST --silent --basic -u Administrator:${PING_IDENTITY_PASSWORD} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' --data '@install-https-enginelistener.txt' https://localhost:9000/pa-admin-api/v3/engineListeners --insecure | jq .id)

if [ -z "$engineListenerId" ] || [ $engineListenerId == null ]
then
  echo "Keypair not imported"
else
  echo "Engine Listener imported - ID: $engineListenerId"
fi
