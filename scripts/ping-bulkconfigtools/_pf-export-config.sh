function prop {
    grep "${1}" ../../docker-compose/pf.env|cut -d'=' -f2
}

echo "Creating output folder..."
mkdir out

echo "Downloading config from https://localhost:9999..."
curl -X GET --basic -u Administrator:$(prop 'administrativeAccounts_items_Administrator_password') --header 'Content-Type: application/json' --header 'X-XSRF-Header: PingFederate' https://localhost:9999/pf-admin-api/v1/bulk/export --insecure > out/pf-export.json

echo "Creating/modifying docker-compose/pf.env and server_profiles/pingfederate/instance/import-bulkconfig.json.subst..."
java -jar ping-bulkexport-tools-project/target/ping-bulkexport-tools-0.0.1-SNAPSHOT-jar-with-dependencies.jar pf-config.json ./out/pf-export.json ../../docker-compose/pf.env ../../server_profiles/pingfederate/instance/import-bulkconfig.json.subst > out/pf-export-convert.log

echo "Copying pf.env to k8 server profile..."
cp ../../docker-compose/pf.env ../../k8/02-completeinstall
