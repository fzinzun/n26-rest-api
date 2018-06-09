#!/bin/bash
#echo " - Transactions out of time - "
#ab -p transaction.json -T 'application/json' -c 10 -n 2000 http://localhost:8080/transactions


echo -e "\n-----------------------------------------\n"
echo " - Transactions in time - "

x=1
while [ $x -le 7 ]
do

CURRENT_MILIS=$(date +'%s')000
RANDOM_NUMBER=$(( ( RANDOM % 100 )  + 1 ))

NUMBER_OF_TRANSACTIONS=2000

jq .amount=$RANDOM_NUMBER transaction.json | tee tmp.json
jq .timestamp=$CURRENT_MILIS tmp.json | tee transaction_in_date.json

ab -p transaction_in_date.json -T 'application/json' -c 10 -n $NUMBER_OF_TRANSACTIONS http://localhost:8080/transactions
sleep 1

  echo "Welcome $x times"
  x=$(( $x + 1 ))
done
