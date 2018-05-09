#!/bin/bash

umask 000
set -e

app=http://localhost:6990/bamboo
creds=admin:admin
pluginPath=$(ls /opt/plugin/*.jar 2>/dev/null | head -1)

if [ -z "$BAMBOO_VERSION" ]
then
    echo "[BOOTSTRAP] \$BAMBOO_VERSION is empty" >&2
    exit 1
fi

rm /opt/bamboo/* -rf
mkdir -p /opt/amps-standalone-bamboo-${BAMBOO_VERSION}/target
ln -s /opt/bamboo /opt/amps-standalone-bamboo-${BAMBOO_VERSION}/target/bamboo

waitForApp()
{
    local url=$1

    while ! curl --output /dev/null --silent --head -f -m 1 $url
    do 
        sleep 10 && echo [BOOTSTRAP] Waiting for $url...
    done
}

installPlugin()
{
    local url=$1/rest/plugins/1.0/
    local creds=$2
    local pluginPath=$3

    local token=$(curl -sI -u $creds "$url?os_authType=basic" | grep upm-token | cut -d: -f2- | tr -d '[[:space:]]')
    echo [BOOTSTRAP] Installing plugin $pluginPath...
    curl -XPOST -u $creds "$url?token=$token" -F plugin=@$pluginPath
    echo [BOOTSTRAP] Ready
}

if [ ! -z "$pluginPath" ]
then
    echo [BOOTSTRAP] Running with plugin $pluginPath...
    (sleep 60 && waitForApp $app && installPlugin $app $creds $pluginPath)&
else
    echo [BOOTSTRAP] Running without plugin...
fi

cd /opt
atlas-run-standalone --product bamboo --version $BAMBOO_VERSION -DskipAllPrompts=true
