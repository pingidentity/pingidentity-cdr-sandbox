

echo "Stopping existing PingAccess..."
kill $(ps aux | grep pingaccess | grep -v grep | awk '{print $2}')

echo "Removing existing PingAccess..."

rm -rf ./temp/pingaccess
