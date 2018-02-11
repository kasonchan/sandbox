#!/bin/bash
mkdir -p logs
nohup bin/sandbox-configs -Dconfig.file=configs/application.conf > logs/server.log 2>&1 &
