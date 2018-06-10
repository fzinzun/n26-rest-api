#!/bin/bash

#This test makes 2000 calls to the endpoint
#echo " - GET transactions - "
#ab -c 10 -n 2000 http://localhost:8080/statistic

while :
do
  curl http://localhost:8080/statistics
  echo ""
  sleep 0.1
done
