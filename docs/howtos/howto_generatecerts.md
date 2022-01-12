# Generating MTLS Authentication Keys

The Consumer Data Rights requires that selected communication between the 3 parties, Data Holder, Data Recipient and Data Registry, be authenticated using a private/public key pair signed by the ACCC Registry. To allow the CDR Sandox to operate standalone Ping Identity has included a "Mock Registry" as well as a certificate authority to facilitate the generation of a network key for MTLS Authentication.

Follow the instructions below to generate a network.p12 file.

!!! Tip
    Using ~/projects/cdr will make it easier to follow this guide. It will also make it easier for us to help you if you encounter issues.

## Generate Keys via Command Line (Recommended)

1. Change to the postman scripts folder

    ```sh
    cd ~/projects/cdr/scripts/postman
    ```

2. Execute the certificate generation script

    ```sh
    ./create-postman-certs.sh
    ```

    !!! Note
        The script will output network.p12 in ~/projects/cdr/scripts/postman/cert folder

3. Validate that the following files have been created

    ```sh
    ls -l ~/projects/cdr/scripts/postman/cert
    ```

    * client.csr
    * client.key
    * csrresponse.p7b
    * csrresponse.pem
    * network.p12
    * public.cer

    !!! Note
        The default password used to protect the network.p12 keystore is: **P@ssword1**

