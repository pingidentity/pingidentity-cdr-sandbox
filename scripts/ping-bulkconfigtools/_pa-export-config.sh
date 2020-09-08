function prop {
    grep "${1}" ../../docker-compose/cdr.env|cut -d'=' -f2
}

echo "Creating output folder..."
mkdir out

echo "Downloading config from https://localhost:9000..."
curl -X GET --basic -u Administrator:$(prop 'PING_IDENTITY_PASSWORD') --header 'Content-Type: application/json' --header 'X-XSRF-Header: PingAccess' https://localhost:9000/pa-admin-api/v3/config/export --insecure > out/pa-export.json

echo "Creating/modifying docker-compose/pa.env and server_profiles/pingaccess/instance/data/start-up-deployer/data.json.subst..."
java -jar ping-bulkexport-tools-project/target/ping-bulkexport-tools-0.0.1-SNAPSHOT-jar-with-dependencies.jar pa-config.json ./out/pa-export.json ../../docker-compose/pa.env ../../server_profiles/pingaccess/instance/data/start-up-deployer/data.json.subst > out/pa-export-convert.log

echo "Creating/modifying k8/02-completeinstall/pa.env and server_profiles/pingaccess-admin/instance/data/start-up-deployer/data.json.subst..."
java -jar ping-bulkexport-tools-project/target/ping-bulkexport-tools-0.0.1-SNAPSHOT-jar-with-dependencies.jar pa-admin-config.json ./out/pa-export.json ../../k8/02-completeinstall/pa.env ../../server_profiles/pingaccess-admin/instance/data/start-up-deployer/data.json.subst > out/pa-admin-export-convert.log

