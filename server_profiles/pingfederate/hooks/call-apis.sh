#!/usr/bin/env sh
#
# Ping Identity DevOps - Docker Build Hooks
#

${VERBOSE} && set -x

# shellcheck source=../../../../pingcommon/opt/staging/hooks/pingcommon.lib.sh
. "${HOOKS_DIR}/pingcommon.lib.sh"

function apiprops {
    grep "${1}" $API_DROPIN_FOLDER/$apiFolder/request.properties|cut -d'=' -f2
}

# ideally replace hardcoded drop-in folder to staging directory
# currently not in staging because we require subst process
API_DROPIN_FOLDER=/opt/out/instance/server/default/drop-in-config

apiFolder=

if test -d "${API_DROPIN_FOLDER}" ; then
    for apiFolderName in $( find "${API_DROPIN_FOLDER}/" -type d ! -name "drop*" 2>/dev/null | sort | uniq ) ; do
        apiFolder=$(basename ${apiFolderName})
        echo "API Folder: $apiFolder"
        requestUrl=$(apiprops 'request-url')
        curlParams=$(apiprops 'request-curl-commands')
        curlCommand="curl $curlParams -d @${apiFolderName}/requestBody.json $requestUrl --insecure --write-out '%{http_code}' --silent --output /dev/null"
        echo "Calling API: $curlCommand"
        httpStatus=$(eval $curlCommand)

        echo "HTTP Status Code: ${httpStatus}"

	rm -r $API_DROPIN_FOLDER/$apiFolder
    done
fi
