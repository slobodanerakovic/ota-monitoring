echo "******** START BUILDING MYPOSTGRES CONTAINER **********"
docker build -t mypostgres postgres/
echo "******** FINISHED BUILDING MYPOSTGRES CONTAINER **********"
sleep 3
echo "******** RUN MYPOSTGRES CONTAINER **********"
docker run -P --name pg_database -p 5432:5432 mypostgres &
sleep 5
