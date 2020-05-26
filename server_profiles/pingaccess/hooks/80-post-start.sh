#!/usr/bin/env sh
#
# Ping Identity DevOps - Docker Build Hooks
#
#- This script is used to import any configurations that are
#- needed after PingAccess starts

# shellcheck source=pingcommon.lib.sh
. "${HOOKS_DIR}/pingcommon.lib.sh"

if test -z "${OPERATIONAL_MODE}" || test "${OPERATIONAL_MODE}" = "STANDALONE"  ; then
  echo "INFO: waiting for PingAccess to start before importing configuration"
    while true; do
    curl -ss --silent -o /dev/null -k https://localhost:9000/pa/heartbeat.ping
    if ! test $? -eq 0 ; then
        echo "Starting PA Admin: Server not started, waiting.."
        sleep 5
    else
        echo "PA started, begin configuration"
	break
    fi
    done
        sleep 30
  	${HOOKS_DIR}/81-after-start-process.sh

        sleep ${PA_POST_CONFIGURATION_DELAY}
        ${HOOKS_DIR}/90-install-ca.sh
        ${HOOKS_DIR}/91-install-keypair.sh
fi

if test "${OPERATIONAL_MODE}" = "CLUSTERED_CONSOLE"  ; then
  echo "Bringing eth0 back up..."
  ip link set eth0 up
fi
