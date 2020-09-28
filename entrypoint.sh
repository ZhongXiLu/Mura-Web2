#!/bin/bash
runuser -l postgres -c '/usr/lib/postgresql/10/bin/postgres -D 10/main -c config_file=/etc/postgresql/10/main/postgresql.conf' &

until pg_isready -h localhost -p 5432 -U postgres; do
  sleep 0.1
done

java -jar /usr/src/mura-web/mura-web-*.jar