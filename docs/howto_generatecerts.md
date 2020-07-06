

The Consumer Data Rights requires that selected communicates between the 3 parties, Data Holder, Data Recipient and Data Registry, be authenticated using a private/public key pair signed by the ACCC Registry. To facilitate  
Follow the instructions here to generate a network.p12 file

**Commandline
/Users/andrewlatham/projects/cdr/scripts/postman
./create-postman-certs.sh

/Users/andrewlatham/projects/cdr/scripts/postman/cert

-rw-r--r--  1 andrewlatham  staff   948  6 Jul 16:03 client.csr
-rw-r--r--  1 andrewlatham  staff  1675  6 Jul 16:03 client.key
-rw-r--r--  1 andrewlatham  staff  6724  6 Jul 16:03 csrresponse.p7b
-rw-r--r--  1 andrewlatham  staff  4088  6 Jul 16:03 csrresponse.pem
-rw-r--r--  1 andrewlatham  staff  4373  6 Jul 16:03 network.p12
-rw-r--r--  1 andrewlatham  staff  1963  6 Jul 16:03 public.cer


**Browser

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

