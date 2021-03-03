echo "Stopping all services"
./stop-all.sh

echo "Starting auxiliary services via docker-compose"
docker-compose up -d

echo "Starting PingDirectory"
./scripts/start-pd.sh

echo "Staring PingFederate"
./scripts/start-pf.sh

echo "Starting PingAccess"
./scripts/start-pa.sh
