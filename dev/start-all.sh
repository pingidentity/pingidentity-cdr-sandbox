echo "Stopping all services"
./stop-all.sh

echo "Starting auxiliary services via docker-compose"
docker-compose up -d

./scripts/helper/wait-for cdrregister:8084 -t 200

echo "Starting PingDirectory"
./scripts/start-pd.sh

echo "Staring PingFederate"
./scripts/start-pf.sh

echo "Starting PingAccess"
./scripts/start-pa.sh

echo "Starting PingDataSync"
./scripts/start-pds.sh

echo "Configuring Data In"
./scripts/configure_datain.sh
