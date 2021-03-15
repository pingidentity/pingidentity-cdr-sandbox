#!/usr/bin/env sh
# shellcheck source=../../../../pingcommon/opt/staging/hooks/pingcommon.lib.sh

# Quiesce the license acceptance screen
# TODO: we should handle the returned HTTP code and display a useful error if the PUT returns a message
_license_http_code=$(
    curl \
        --insecure \
        --silent \
        --request PUT \
        --write-out '%{http_code}' \
        --user "Administrator:2Access" \
        --output license.acceptance \
        --header "X-Xsrf-Header: PingAccess" \
        --data '{ "email": null, "slaAccepted": true, "firstLogin": false, "showTutorial": false,"username": "'"Administrator"'"}' \
        https://localhost:9000/pa-admin-api/v3/users/1 \
        2>/dev/null
    )
if test "${_license_http_code}" != "200"
then
    echo "ERROR: Could not accept license"
fi

#Only change password if PING_IDENTITY_PASSWORD is supplied
if test -n "${PING_IDENTITY_PASSWORD}"
then
    echo "INFO: changing admin password"
    _pwChange=$(
        curl \
            --insecure \
            --silent \
            --write-out '%{http_code}' \
            --output /dev/null \
            --request PUT \
            --user "Administrator:2Access" \
            --header "X-Xsrf-Header: PingAccess" \
            --data '{"currentPassword": "'"2Access"'","newPassword": "'"${PING_IDENTITY_PASSWORD}"'"}' \
            https://localhost:9000/pa-admin-api/v3/users/1/password \
            2>/dev/null
        )

    if test "${_pwChange}" != "200"
    then
        echo "Error: Password not accepted"
    fi
fi
