#!/bin/bash

source /bootstrap/utils.sh
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
rm /opt/amps-standalone-bamboo-${BAMBOO_VERSION}/target/* -rf
mkdir -p /opt/amps-standalone-bamboo-${BAMBOO_VERSION}/target
ln -s /opt/bamboo /opt/amps-standalone-bamboo-${BAMBOO_VERSION}/target/bamboo

if [ ! -z "$pluginPath" ]
then
    echo [BOOTSTRAP] Running with plugin $pluginPath...
    (sleep 60 && waitForApp $app && makeDirDeletableForAll /opt/bamboo && installPlugin $app $creds $pluginPath)&
else
    echo [BOOTSTRAP] Running without plugin...
fi

cd /opt
atlas-run-standalone --product bamboo --version $BAMBOO_VERSION -DskipAllPrompts=true
