#!/bin/bash
set -e

WYBY=$WYVERN_HOME/bin/wyby
WYVERN=$WYVERN_HOME/bin/wyvern

if [ "$1" = "travis" ]; then
    # keep travis alive
    function bell() {
        while true; do
            sleep 60
            echo -n "."
        done
    }
    bell &
fi

(
cd src/
rm -f main_js.wyb
$WYBY main_js.wyv
echo "Bootstrapping..."
time $WYVERN main_java.wyv > ../boot.js
) || rm -f boot.js

./self-bootstrap.sh
diff -q boot.js boot.js.old || (echo "sanity check failed" && exit 1)

if [ "$1" = "travis" ]; then
    kill %1
fi
