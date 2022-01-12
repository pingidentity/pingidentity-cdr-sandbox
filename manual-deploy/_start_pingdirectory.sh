./scripts/stop-pd.sh
mkdir temp

unzip -q binaries/PingDirectory-*.zip -d ./temp

cp -R overlays/pingdirectory/* ./temp/PingDirectory/

export DR_BASE_HOSTNAME=data-recipient.local
export USER_BASE_DN=dc=data-holder,dc=com
export PINGFEDERATE_HOSTNAME=localhost:2031
export ROOT_USER_DN=cn=administrator
export ROOT_USER_PASSWORD_FILE=../../sec/default-password
export BASE_DC_VALUE=data-holder

./temp/PingDirectory/bin/manage-profile setup --licenseKeyFile ./licenses/PingDirectory.lic --profile ./temp/PingDirectory/pd.profile --useEnvironmentVariables
# --rejectFile rejectFile.ldif
