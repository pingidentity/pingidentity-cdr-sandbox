rm -R ./cert/*
mkdir ./cert

curl -o ./cert/public.cer http://sso.data-holder.local:8084/public/download

openssl genrsa -out ./cert/client.key 2048

openssl req -new -key ./cert/client.key -out ./cert/client.csr -subj "/CN=dcr-test-postman/O=PingIdentity/C=AU"

curl -X POST -H --silent -F csr=@./cert/client.csr -o ./cert/csrresponse.p7b http://sso.data-holder.local:8084/public/sign

openssl pkcs7 -print_certs -in ./cert/csrresponse.p7b -out ./cert/csrresponse.pem
openssl pkcs12 -export -out ./cert/network.p12 -inkey ./cert/client.key -in ./cert/csrresponse.pem -password pass:P@ssword1


