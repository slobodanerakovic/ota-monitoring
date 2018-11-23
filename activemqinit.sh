echo "******** START BUILDING MYACTIVEMQ CONTAINER **********"
docker build -t myactivemq activemq/	

echo "******** RUN MYACTIVEMQ CONTAINER **********"
docker run -P --name activemq_jms -p 61616:61616 myactivemq &
sleep 5

