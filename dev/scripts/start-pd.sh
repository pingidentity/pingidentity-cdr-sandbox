mkdir temp
cd temp

echo "Substituting PD configuration..."

while read line; do
    export $line
done < ../cdr.env

while read line; do
    export $line
done < ../pd.env

echo "Extracting new PingDirectory binary..."
unzip -q ../binaries/PingDirectory-*.zip -d .

cd PingDirectory

echo "Copying license..."
docker run --rm -it \
  -v /tmp:/tmp pingidentity/pingdownloader \
  --license \
  --devops-user ${PING_IDENTITY_DEVOPS_USER} \
  --devops-key ${PING_IDENTITY_DEVOPS_KEY} \
  --product pingdirectory

cp /tmp/product.lic ./PingDirectory.lic

echo ${PING_IDENTITY_PASSWORD} > password-file

cp -R ../../../local_profiles/pingdirectory/pd.profile .
cp ../../scripts/files/pd-setup-arguments.txt ./pd.profile/setup-arguments.txt
cp ../../../local_profiles/pingdirectory/instance/lib/extensions/* ./lib/extensions

rm pd.profile/dsconfig/30-daily-encrypted-export.dsconfig

export CERT_ALIAS=pingdirectory

../../scripts/create-localhost-keypair.sh

bin/manage-profile setup --profile pd.profile -E

bin/encryption-settings create --cipher-algorithm AES --key-length-bits 128 --set-preferred --passphrase-file password-file

cd ..
