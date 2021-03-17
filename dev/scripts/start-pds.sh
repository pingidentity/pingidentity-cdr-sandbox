mkdir temp
cd temp

echo "Substituting PDS configuration..."

while read line; do
    export $line
done < ../cdr.env

while read line; do
    export $line
done < ../pds.env

echo "Extracting new PingDataSync binary..."
unzip -q ../binaries/PingDataSync-*.zip -d .

cd PingDataSync

echo "Copying license..."
docker run --rm -it \
  -v /tmp:/tmp pingidentity/pingdownloader \
  --license \
  --devops-user ${PING_IDENTITY_DEVOPS_USER} \
  --devops-key ${PING_IDENTITY_DEVOPS_KEY} \
  --product pingdirectory

cp /tmp/product.lic ./PingDirectory.lic

echo ${PING_IDENTITY_PASSWORD} > password-file

cp -R ../../../server_profiles/pingdatasync/pd.profile .
cp ../../scripts/files/pds-setup-arguments.txt ./pd.profile/setup-arguments.txt
cp ../../../server_profiles/pingdatasync/instance/lib/extensions/* ./lib/extensions

export CERT_ALIAS=pingdatasync

../../scripts/create-localhost-keypair.sh

bin/manage-profile setup --profile pd.profile -E

cd ..
