generate_post_data()
{
  cat <<EOF
{
  "alias": "cdr-register-local-$RANDOM",
  "fileData": "$(cat public.pem | sed '1,1d' | sed '$ d')"
}
EOF
}

generate_put_trustedgroup()
{
  cat <<EOF
{
  "name": "MTLS Certificate Group",
  "useJavaTrustStore": false,
  "systemGroup": false,
  "ignoreAllCertificateErrors": false,
  "skipCertificateDateCheck": true,
  "certIds": [
    $idVal
  ]
}
EOF
}

curl -o public.pem http://cdrregister:8084/public/download

echo $("generate_post_data") > install-cert.txt

response=$(curl -X POST --silent --basic -u Administrator:${PA_ADMIN_PASSWORD_INITIAL} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' --data '@install-cert.txt' https://localhost:9000/pa-admin-api/v3/certificates --insecure)

idVal="$(echo $response | jq .id)"

if [ -z "$idVal" ] || [ $idVal == null ]
then
  echo "Cert not imported"
else
  echo "Cert imported - ID: $idVal"

  trustedCAGroup=$(curl --silent --basic -u Administrator:${PA_ADMIN_PASSWORD_INITIAL} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' https://localhost:9000/pa-admin-api/v3/trustedCertificateGroups?name=MTLS%20Certificate%20Group --insecure | jq .items[0].id)

  echo $("generate_put_trustedgroup") > install-certgroup.txt

  installCertResponse=$(curl -X PUT --silent --basic -u Administrator:${PA_ADMIN_PASSWORD_INITIAL} --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'X-XSRF-Header: PingAccess' --data '@install-certgroup.txt' https://localhost:9000/pa-admin-api/v3/trustedCertificateGroups/${trustedCAGroup} --insecure)
fi
