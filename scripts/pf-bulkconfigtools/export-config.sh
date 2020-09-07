mkdir out
curl -X GET --basic -u Administrator:2FederateM0re --header 'Content-Type: application/json' --header 'X-XSRF-Header: PingFederate' https://localhost:9999/pf-admin-api/v1/bulk/export --insecure > out/pf-export.json
java -jar pf-bulkexport-tools-0.0.1-SNAPSHOT-jar-with-dependencies.jar config.json ./out/pf-export.json ../../docker-compose/pf.env ../../server_profiles/pingfederate/instance/import-bulkconfig.json.subst
