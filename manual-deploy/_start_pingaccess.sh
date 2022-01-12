mkdir temp

unzip -q binaries/pingaccess-*.zip -d ./temp
mv temp/pingaccess-* temp/pingaccess

cp -R ../server_profiles/pingaccess/instance/conf/template/* ./temp/pingaccess/conf/template/ 
cp -R ../server_profiles/pingaccess/instance/lib/* ./temp/pingaccess/lib/
cp -R overlays/pingaccess/* ./temp/pingaccess/
cp licenses/pingaccess.lic ./temp/pingaccess/conf/

nohup ./temp/pingaccess/bin/run.sh &> pingaccess.out &
