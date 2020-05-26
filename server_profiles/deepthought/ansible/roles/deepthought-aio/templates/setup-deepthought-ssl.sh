#!/bin/bash
#*******************************************************************************
# Copyright (C) 2020 Biza Pty Ltd
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#*******************************************************************************

set -x

if test -f "/root/.deepthoughtssl"; then
	echo "Deep Thought SSL Initialisation has already been complete, exiting"
	exit 0
fi

HOSTNAME=$(curl -s {{ cdr_zone_dns_mapper_endpoint }})

echo "Setting up for $HOSTNAME, sleeping 120 seconds for DNS propogation"
sleep 120

if ! certbot --webroot -n certonly --agree-tos --email dev@null.com -d "$HOSTNAME" --webroot-path /var/www/html
then
	echo "Certbot failed retrieval, halting progress"
	exit 1
fi

echo "Linking fullchain.pem"
rm -f /etc/letsencrypt/fullchain.pem
ln -s /etc/letsencrypt/live/"$HOSTNAME"/fullchain.pem /etc/letsencrypt/fullchain.pem
echo "Linking privkey.pem"
rm -f /etc/letsencrypt/privkey.pem
ln -s /etc/letsencrypt/live/"$HOSTNAME"/privkey.pem /etc/letsencrypt/privkey.pem

echo "Restarting nginx server"
/bin/systemctl restart nginx

echo "Performing search and replace for hostnames"
sed -i "s/http:\/\/localhost\:8081\/dio-au/https:\/\/$HOSTNAME\/dio-au/g" /var/www/html/assets/config.json
sed -i "s/http:\/\/localhost\:8080/https:\/\/$HOSTNAME/g" /var/www/html/assets/config.json
sed -i "s/http:\/\/localhost\:8080/https:\/\/$HOSTNAME/g" /etc/deepthought-product-api.yml
sed -i "s/http:\/\/localhost\:8080/https:\/\/$HOSTNAME/g" /etc/deepthought-admin-service.yml
sed -i "s/https:\/\/localhost/https:\/\/$HOSTNAME/g" /etc/update-motd.d/999-deepthought

echo "Restarting deepthought-admin-service"
/bin/systemctl restart deepthought-admin-service

echo "Restarting deepthought-product-api"
/bin/systemctl restart deepthought-product-api

echo "Touching initialisation success"
touch /root/.deepthoughtssl
