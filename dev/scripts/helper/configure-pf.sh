#!/usr/bin/env sh
#
# Ping Identity DevOps - Docker Build Hooks
#

${VERBOSE} && set -x

# shellcheck source=../../../../pingcommon/opt/staging/hooks/pingcommon.lib.sh
#. "${HOOKS_DIR}/pingcommon.lib.sh"

# ideally replace hardcoded drop-in folder to staging directory
# currently not in staging because we require subst process

expectedHttpStatus=200
curlCommand="curl -X POST --basic -u Administrator:2FederateM0re --header 'Content-Type: application/json' --header 'X-XSRF-Header: PingFederate' --header 'X-BypassExternalValidation: true' -d @pingfederate/bulk-config/data.json https://localhost:9999/pf-admin-api/v1/bulk/import?failFast=false --insecure --write-out '%{http_code}' --silent --output /dev/null"
echo "Calling API: $curlCommand"
httpStatus=$(eval $curlCommand)

echo "HTTP Status Code: ${httpStatus}"
