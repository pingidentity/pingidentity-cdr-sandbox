echo "Stopping auxiliary services via docker-compose"
docker-compose down

echo "Stopping PingDirectory"
./scripts/stop-pd.sh

echo "Stopping PingFederate"
./scripts/stop-pf.sh

echo "Stopping PingAccess"
./scripts/stop-pa.sh
