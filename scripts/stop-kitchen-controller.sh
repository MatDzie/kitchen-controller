#!/bin/bash
# File location on target: /usr/local/bin/stop-kitchen-controller.sh

kill -9 $(ps -ef | grep kitchen-controller | grep -v grep | awk '{print $2}')