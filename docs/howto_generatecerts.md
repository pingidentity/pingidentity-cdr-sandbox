Follow the instructions here to generate a network.p12 file

http://sso.data-holder.local:8084/csrFlow

Manage your application access
 

1. Set up your client certificate
Generate a new key
openssl genrsa -out MyClient1.key 2048
Generate a CSR Request
openssl req -new -key MyClient1.key -out MyClient1.csr -subj "/CN=sampletpp/O=My Company Name LTD./C=AU"
2. Download CA Certificate
The CA certificate will be provided in the signed response, however depending on your system, you may require the CA certificate to complete the chaining process.


 

3. Download CSR Response
Upload CSR: 


 

4. Complete certificate chaining - CSR Response
Convert PKCS7 response to PEM
openssl pkcs7 -print_certs -in csrresponse.p7b -out csrresponse.pem
Create PKCS12 Keystore
openssl pkcs12 -export -out network.p12 -inkey MyClient1.key -in csrresponse.pem

