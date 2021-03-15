getEnvKeys()
{
    env | cut -d"=" -f1 | sed -e "s/^/$/"
}

mkdir temp
cd temp

echo "Extracting new PingFederate binary..."
unzip -q ../binaries/pingfederate-*.zip -d .
mv ./pingfederate-*/pingfederate ./pingfederate
rm -R ./pingfederate-*

echo "Copying license..."
docker run --rm -it \
  -v /tmp:/tmp pingidentity/pingdownloader \
  --license \
  --devops-user ${PING_IDENTITY_DEVOPS_USER} \
  --devops-key ${PING_IDENTITY_DEVOPS_KEY} \
  --product pingfederate

cp /tmp/product.lic ./pingfederate/server/default/conf/pingfederate.lic

echo "Copying server profile..."

cp -R ../../server_profiles/pingfederate/instance/* ./pingfederate/

echo "Substituting PF configuration..."
while read line; do
    export $line
done < ../../docker-compose/pf.env

while read line; do
    export $line
done < ../pf.override.env

while read line; do
    export $line
done < ../cdr.env

sed -e "s/pingdirectory:443/pingdirectory:3443"/g ./pingfederate/etc/webdefault.xml.subst > ./pingfederate/etc/webdefault.xml.tmp

envsubst "'$(getEnvKeys)'" < ./pingfederate/etc/webdefault.xml.tmp > ./pingfederate/etc/webdefault.xml
envsubst "'$(getEnvKeys)'" < ./pingfederate/bin/run.properties.subst > ./pingfederate/bin/run.properties
envsubst "'$(getEnvKeys)'" < ./pingfederate/server/default/conf/log4j2.xml.subst.default > ./pingfederate/server/default/conf/log4j2.xml

echo "Copying Consent API Grant Storage Module..."
cp -R ../scripts/files/pf-data/* ./pingfederate/server/default/data

echo "Starting PingFederate..."
nohup ./pingfederate/bin/run.sh & > pingfederate-nohup.out &

echo "Waiting for PingFederate to finish booting..."
../scripts/helper/wait-for localhost:9999 -t 200

cd ..
