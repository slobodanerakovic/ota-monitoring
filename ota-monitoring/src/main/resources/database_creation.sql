-- database and user creation, and granting
sudo su - postgres
psql template1
template1=# create user test with password 'test';
template1=# create database otamonitoring;
template1=# grant all privileges on database otamonitoring to test;
template1=# grant all on database otamonitoring to test;
\q
exit
-- exit psql and logout as user postgres