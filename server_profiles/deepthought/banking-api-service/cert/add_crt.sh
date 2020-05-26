#!/bin/sh

for file in `ls *.crt`
do
  keytool -importcert -cacerts -storepass changeit -noprompt -trustcacerts -alias ${file} -file ${file}
done
