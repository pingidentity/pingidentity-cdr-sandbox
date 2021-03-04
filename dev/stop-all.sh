echo "Stopping auxiliary services via docker-compose"
docker-compose down

echo "Stopping PingDataSync"
./scripts/stop-pds.sh

echo "Stopping PingFederate"
./scripts/stop-pf.sh

echo "Stopping PingAccess"
./scripts/stop-pa.sh

echo "Stopping PingDirectory"
./scripts/stop-pd.sh
