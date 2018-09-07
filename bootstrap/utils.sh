#!/bin/bash

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

makeDirDeletableForAll()
{
    find $1 -type d -exec chmod 777 {} \;
}
