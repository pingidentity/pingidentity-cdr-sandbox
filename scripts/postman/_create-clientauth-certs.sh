MOCKREGISTER_BASEURL=https://mockregister.data-holder.local
SERVERCERT_SUBJECT="/CN=johnsmith@companya.com/ST=VIC/L=MELBOURNE/OU=DATAHOLDER/O=DATAHOLDER/C=AU"


rm -R ./cert/*
mkdir ./cert

curl -o ./cert/public.cer ${MOCKREGISTER_BASEURL}/public/download --insecure

openssl genrsa -out ./cert/client.key 2048

openssl req -new -key ./cert/client.key -out ./cert/client.csr -subj ${SERVERCERT_SUBJECT}

curl -X POST -H --silent -F csr=@./cert/client.csr -o ./cert/csrresponse.p7b ${MOCKREGISTER_BASEURL}/public/signClientAuth --insecure

openssl pkcs7 -print_certs -in ./cert/csrresponse.p7b -out ./cert/csrresponse.pem
openssl pkcs12 -export -out ./cert/network.p12 -inkey ./cert/client.key -in ./cert/csrresponse.pem -password pass:P@ssword1

openssl pkcs12 -in ./cert/network.p12 -out ./cert/FAPI_Client1.crt.pem -clcerts -nokeys -password pass:P@ssword1
openssl pkcs12 -in ./cert/network.p12 -out ./cert/FAPI_Client1.key.pem -nocerts -nodes -password pass:P@ssword1
