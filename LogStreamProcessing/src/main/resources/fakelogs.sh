#!/usr/bin/env sh
rm /tmp/logdata
touch /tmp/logdata
tail -f /tmp/logdata | ../../../../../nmap-7.60/ncat.exe -lk 7777 &
TAIL_NC_PID=$!
cat ./fake_logs/log1.log >> /tmp/logdata
sleep 5
cat ./fake_logs/log2.log >> /tmp/logdata
sleep 1
cat ./fake_logs/log1.log >> /tmp/logdata
sleep 2
cat ./fake_logs/log1.log >> /tmp/logdata
sleep 3
sleep 20
kill $TAIL_NC_PID