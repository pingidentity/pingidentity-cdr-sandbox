#!/usr/bin/env sh
for app in docs example host-manager manager ; do
    rm -rf /usr/local/tomcat/webapps/${app}
done