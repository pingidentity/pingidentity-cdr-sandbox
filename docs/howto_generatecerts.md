# Generating MTLS Authentication Keys

The Consumer Data Rights requires that selected communication between the 3 parties, Data Holder, Data Recipient and Data Registry, be authenticated using a private/public key pair signed by the ACCC Registry. To allow the CDR Sandox to operate standalone Ping Identity has included a "Mock Registry" as well as a certificate authority to facilitate the generation of a network key for MTLS Authentication.

Follow the instructions below to generate a network.p12 file. 

> Using ~/projects/cdr will make it easier to follow this guide. It will also make it easier for us to help you if you encounter issues.

## Generate keys via Command line (Recommended)


### 1. Change to the postman scripts folder

```sh
cd ~/projects/cdr/scripts/postman
```

### 2. Execute the certificate generation script

```sh
./create-postman-certs.sh
```
> Note: The script will output network.p12 in ~/projects/cdr/scripts/postman/cert folder

### 3. Validate that the following files have been created 

```sh
ls -l ~/projects/cdr/scripts/postman/cert
```
* client.csr
* client.key
* csrresponse.p7b
* csrresponse.pem
* network.p12
* public.cer

> Note: The default password used to protect the network.p12 keystore is **P@ssword1**

## Generate keys via a Web Browser

### 1. Change to the postman cert folder

```sh
cd ~/projects/cdr/scripts/postman/cert
```

### 2. Generate a new private key

```sh
openssl genrsa -out MyClient1.key 2048
```

### 3. Generate a Certificate Signing Request(CSR)

```sh
openssl req -new -key MyClient1.key -out MyClient1.csr -subj "/CN=sampleDR/O=My Company Name LTD./C=AU"
```

### 4. Browse to the Mock Registry Certificate Authority

<https://mockregister.data-holder.local/csrFlow>

### 5. Download CA Certificate
>The CA certificate will be provided in the signed response, however depending on your system, you may require the CA certificate to complete the chaining process.

* Click on the ***Download CA Certificate*** button
* Move the CA Certificate to the cert folder
```sh
cp ~/Downloads/public.cer ~/projects/cdr/scripts/postman/cert/public.cer
```

### 6. Download the Signed Certificate

* Click "Choose File"
* Select the ***MyClient1.csr*** file to upload
* Click Download CSR Request
* Move the Signed Certificate to the cert folder
```sh
cp ~/Downloads/csrresponse.p7b ~/projects/cdr/scripts/postman/cert/csrresponse.p7b
```

### 7. Convert the PKCS response to a PEM
```sh
openssl pkcs7 -print_certs -in csrresponse.p7b -out csrresponse.pem
```

### 8. Create the PKCS12 Keystore
```sh
openssl pkcs12 -export -out network.p12 -inkey MyClient1.key -in csrresponse.pem
```
