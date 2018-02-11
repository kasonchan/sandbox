#!/bin/bash
mkdir -p logs
nohup sh ../bin/sandbox-configs > logs/server.log &
