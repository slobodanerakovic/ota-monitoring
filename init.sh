function cleanup_handler {
	echo "******** CLEANUP **********"
	sleep 1
	docker kill ota_monitoring
	sleep 1
	docker rm --force ota_monitoring
	sleep 1

	docker kill pg_database
	sleep 1
	docker rm --force pg_database
	sleep 1

	docker kill activemq_jms
	sleep 1
	docker rm --force activemq_jms
	sleep 

	echo "******** CLEANUP FINISHED **********"
	exit
}
echo "******** DOCKER DEPLOYMENT STARTED **********"
trap cleanup_handler INT

./postgresinit.sh

./activemqinit.sh

./prepareApp.sh

sleep 5
echo "******** START BUILDING OTA_MONIORING CONTAINER **********"
docker build -t ota-monitoring ota-monitoring/
echo "******** FINISHED BUILDING OTA_MONIORING CONTAINER **********"
sleep 3
echo "******** RUN OTA_MONIORING CONTAINER **********"
docker run -P --name ota_monitoring -p 6789:6789 -t ota-monitoring

