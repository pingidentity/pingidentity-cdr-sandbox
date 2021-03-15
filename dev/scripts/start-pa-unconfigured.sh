getEnvKeys()
{
    env | cut -d'=' -f1 | sed -e 's/^/$/'
}

mkdir temp
cd temp


echo "Extracting new PingAccess binary..."
unzip ../binaries/pingaccess-*.zip -d .
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

echo "Starting PingAccess..."
nohup ./pingaccess/bin/run.sh & > pingaccessnohup.out &

echo "Waiting for PingAccess to finish booting..."
../scripts/helper/wait-for localhost:9000 -t 200

echo "Agreeing to license and setting up password..."
../scripts/helper/pa-setpassword.sh

