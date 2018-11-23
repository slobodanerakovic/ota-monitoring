echo "******** CHECK DOCKER IP AND UPDATE APPLICATION.YML **********"
postgresIP=$(docker inspect -f '{{ .NetworkSettings.IPAddress }}' pg_database)
echo "postgres IP: $postgresIP"

activemqIP=$(docker inspect -f '{{ .NetworkSettings.IPAddress }}' activemq_jms)
echo "activemq IP: $activemqIP"

sed -i -e 's/postgres_host/'${postgresIP}'/g' ota-monitoring/src/main/resources/application.yml
sed -i -e 's/jms_host/'${activemqIP}'/g' ota-monitoring/src/main/resources/application.yml
