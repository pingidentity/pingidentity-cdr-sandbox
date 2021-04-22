getEnvKeys()
{
    env | cut -d'=' -f1 | sed -e 's/^/$/'
}

mkdir temp
cd temp


echo "Extracting new PingAccess binary..."
unzip -q ../binaries/pingaccess-*.zip -d .
mv ./pingaccess-* ./pingaccess

echo "Copying license..."
docker run --rm -it \
  -v /tmp:/tmp pingidentity/pingdownloader \
  --license \
  --devops-user ${PING_IDENTITY_DEVOPS_USER} \
  --devops-key ${PING_IDENTITY_DEVOPS_KEY} \
  --product pingaccess

cp /tmp/product.lic ./pingaccess/conf/pingaccess.lic

echo "Copying server profile..."

cp -R ../../server_profiles/pingaccess/instance/* ./pingaccess/

echo "Substituting PA configuration..."
while read line; do
    export $line
done < ../../docker-compose/pa.env

while read line; do
    export $line
done < ../cdr.env

sed -e "s/pingdatagovernance:1443/pingdatagovernance:7443/g" -e "s/pingdirectory:1443/pingdirectory:3443/g" -e "s/\\\\\"443\\\\\"/\\\\\"6443\\\\\"/g" ./pingaccess/data/start-up-deployer/data.json.subst > ./pingaccess/data/start-up-deployer/data.json.tmp

envsubst "'$(getEnvKeys)'" < ./pingaccess/data/start-up-deployer/data.json.tmp > ./pingaccess/data/start-up-deployer/data.json



echo "Starting PingAccess..."
nohup ./pingaccess/bin/run.sh & > pingaccessnohup.out &

echo "Waiting for PingAccess to finish booting..."
../scripts/helper/wait-for localhost:9000 -t 200

echo "Agreeing to license and setting up password..."
../scripts/helper/pa-setpassword.sh

echo "Installing CDR Register CA into PA trust store. This may fail if the cert is already there..."
../../server_profiles/pingaccess/hooks/84-install-ca.sh

echo "Installing CDR Keypairs..."
../../server_profiles/pingaccess/hooks/85-install-keypair.sh
../../server_profiles/pingaccess/hooks/86-install-keypair-configquery.sh
../../server_profiles/pingaccess/hooks/87-install-keypair-siteauthenticator.sh
