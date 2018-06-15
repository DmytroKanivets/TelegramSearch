#!/bin/sh

apiKey="151369"
apiHash="12f93c908c66d32bdf975a483b3f0691"
apiPhone="+380688109527"
login="admin"
password="QW123456"
host="localhost:80"
authUri="/api/admin/telegram/auth/"

sudo pkill -f "java.*webserver-spring.*"

echo "Starting server"
sudo nohup java -jar webserver-spring-1.0.jar --telegram.api.key=${apiKey} --telegram.api.hash=$apiHash --telegram.api.phone=${apiPhone} --PORT=80 &> server.log&
echo "Server is loading"

code=000
while [ $code -ne 200 ]
do
sleep 5
echo "Checking server status..."
code=$(curl --write-out %http_code --silent --output /dev/null "localhost")
done;

echo -n "Server started; enter auth code: "

read authCode

authEncoded=$(echo -n ${login}:${password} | base64)


code=$(curl -X PUT -H "Authorization: Basic $authEncoded" --write-out %http_code --silent --output /dev/null "$host$authUri$authCode")

if [  $code -ne 200  ]
then
    echo "Error at code sending"
else
    echo "Done"
fi