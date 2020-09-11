#!/usr/bin/env sh
#
# Ping Identity DevOps - Docker Build Hooks
#

function prop {
    echo "Reading properties file: $API_DROPIN_FOLDER/$apiFolder/request.properties"
    grep "${1}" $API_DROPIN_FOLDER/$apiFolder/request.properties|cut -d'=' -f2
}

${VERBOSE} && set -x

# shellcheck source=../../../../pingcommon/opt/staging/hooks/pingcommon.lib.sh
. "${HOOKS_DIR}/pingcommon.lib.sh"

# ideally replace hardcoded drop-in folder to staging directory
# currently not in staging because we require subst process
API_DROPIN_FOLDER=/opt/out/instance/server/default/drop-in-config

apiFolder=

if test -d "${API_DROPIN_FOLDER}" ; then
    for apiFolderName in $( find "${API_DROPIN_FOLDER}/" 2>/dev/null | sort | uniq ) ; do
        apiFolder=$(basename ${apiFolderName})
        echo "Calling API in: ${apiFolder}"
        httpStatus=$(curl $(prop 'request-curl-commands') -d @${apiFolderName}/requestBody.json $(prop 'request-url') --insecure --write-out '%{http_code}' --silent --output /dev/null)

        echo "HTTP Status Code: ${httpStatus}"
    done
fi


