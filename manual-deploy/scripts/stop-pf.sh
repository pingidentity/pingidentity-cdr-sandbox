echo "Stopping existing PingFederate..."
kill $(ps aux | grep pingfederate | grep -v grep | awk '{print $2}')

echo "Removing existing PingFederate..."

rm -rf ./temp/pingfederate
