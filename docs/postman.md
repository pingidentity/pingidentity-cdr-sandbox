# Setup Postman to Run Consent Scripts



/scripts/postman/cdr-au.consent.postman_collection.json

This will require:

Update edge. This will update everything including docker-compose.yaml.

Postman configuration:

After creating the network certificates using create-postman-certs.sh, apply the network.p12 to the following hostnames in Postman:

sso.data-holder.local

api.data-holder.local

Disable “Automatically follow redirects”
