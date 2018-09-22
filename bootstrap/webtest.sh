#!/bin/bash

source /bootstrap/utils.sh
umask 000
set -e

app=${BAMBOO_URL}/bamboo

waitForApp $app
sleep 10

trap "makeDirDeletableForAll /opt/test" EXIT

cd /opt/test && ./gradlew -Dgeb.env=chromeHeadless --info --stacktrace test
