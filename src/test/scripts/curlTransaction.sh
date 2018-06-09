RANDOM_NUMBER=$(( ( RANDOM % 10 )  + 1 ))
echo $RANDOM_NUMBER
CURRENT_MILIS=$(date +'%s')000
amount
jq .amount=$RANDOM_NUMBER transaction.json | tee tmp.json
jq .timestamp=$CURRENT_MILIS tmp.json | tee transaction_in_date.json
curl -X POST -d @transaction_in_date.json -H "Content-Type: application/json" http://localhost:8080/transactions
rm tmp.json

