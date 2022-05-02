#!/bin/bash

echo 'Running Entrypoint shell script'
set +x
env
exec java -jar ${app_path}
