# ota-monitoring : 24 September, 2018
## Architecture of backend to vehicle communications for Tesla Motors, along with the analytical suite for the vehicle generated data and the Over-The-Air (OTA) software update system

**REQUIREMENT** <br /><br />
Architecture

The main aim is to design the architecture of backend to vehicle communications for Tesla Motors, along with the analytical suite for the vehicle generated data and the Over-The-Air (OTA) software update system. 

Assume that the cars have an onboard mobile 3G SIM card, which is used for communications with the company’s backend systems. On board the car there is a Linux host which is used to acquire the data from the vehicle’s sensors. The host can be updated with new system packages related to it, or new features for the autopilot, the infotainment system, navigation system and so on. A good explanation of the OTA system in real-life Teslas can be found here. Note that the vehicle’s location can also be retrieved.

The analytical system should capture telemetry data and diagnostic messages for failures emitted from the vehicles and provide a basic overview for individual vehicles and the overall fleet in an area or country. 

Some examples of answers that should arise from the dataset are given below:

Is range anxiety justified for Tesla drivers driving on the German Autobanhs or the California highways?
Do the customers have a degraded user experience due to increased rate of failures or not?
Should Tesla install more superchargers in Switzerland?

**Code**

Please implement the OTA service, on the backend and the vehicle unit. On the backend you should have a list of entries containing the vehicles in the fleet and also a list of installed packages per vehicle and the respective version installed. 

When a new version is released for a package, it should be sent for installation in the whole fleet. 

The on-vehicle part of the OTA system should acknowledge the correct download of the package and the successful installation of it. It should then emit an acknowledgement, which should update the respective entries in the back end system. 

You should not be concerned for the creation of the vehicle and package entries, you can assume that they are already handled from an ERP system. In our example you can add the entries manually. Your concern is the release of a new package version and the subsequent installation in the vehicle fleet. 
<br /><br />**END REQUIREMENT**

<br /><br />

## Techinical overview:
 - The application is spring-boot microservice, 2.0.5.RELEASE version. 
 - Java 8 is used for development
 - Technologies: Spring, JPA, JMS, Maven, Docker
 - Postgres database
 - ActiveMQ
 
<br /><br />
**NOTE:** Docker deployment process has been tested on Ubuntu 16.04 from local docker repository scratch.<br />

File: **Architecture Documentation.pdf** conains architectural overview of the application, as a response to delivered requirements <br /> <br />
This manual contains instructions for environment configuration, application deployment and usage. <br />
**If you do not have installed docker, go to section - NO DOCKER README, within this file**<br />
I am using docker for environment setup, so all you need is installed **docker** on your machine.<br />
Code folder contains file: init.sh - which is the entry point for:
1. database docker container/image build and run
2. activemq docker container/image build and run
3. application docker container/image build and run
<br /><br />
## DOCKER DEPLOYMENT<br />

If you have docker installed, just go into Code folder, run this bash script file **(./init.sh)** and everything will be configured (keep in mind that it will takes several minutes to configure environment and build application - depending on you machine and Internet connection).
Prerequisite is that following ports are free on your pc:<br />
**5432**, **61616**, **8161**, which are needed for postgres database and activemq, used by the app (this can be also customized as well).<br />
<br /><br />
One of the main issue which could be is database and activemq running Ip. Since docker has various strategy for assigning ip address to its container, and the spring boot, which this application is, requires in the file "ota-monitoring/src/main/resources/application.yml" contains valid url for application database driver just as for activemq location:<br />        
    **url: jdbc:postgresql://postgres_host/otamonitoring**
    **brokerUrl: tcp://jms_host:61616**
<br /><br />
However, the **init.sh** script, accompanied by other present scripts as well, does this ip detection and change on its own, but, I am just stressing if any issue about it raise.
If you are running this on windows (I guess not) then application database driver url and active url in the file **ota-monitoring/src/main/resources/application.yml** needs to be updated manually, and docker build/run needs to be executed manually from the init.sh file.
<br /><br />
**NOTE**: If you have already installed postgres and activemq on your machine, running on these ports, just update **ota-monitoring/src/main/resources/application.yml** for each url (postgres_host, and jms_host) to localhost, and create application database from sql: 
**ota-monitoring/src/main/resources/database_creation.sql**.<br />
Once environment is setup (by docker hopefully or by you manual for any reasons) schema creation, and test data will be inserted in database, automatically at first run.
If you are deploying app manually (by executing mvn package for compilation, and issuing **java -jar target/ota-monitoring.jar** from ota-monitoring folder) makes sure that after first stop, **initialization-mode: always* (from file: **ota-monitoring/src/main/resources/application.yml**) is change from 'always' to 'embedded' (to prevent repeating of schema creation). <br />But these are all if you do manual deployment, and not using docker.
<br /><br />
Once the application is started, you can access frontend part of the app by going to: *http://localhost:6789/ota-monitoring/* link.
<br /><br />
## NO DOCKER README <br />
If you have no docker then:
- Make sure you have installed postgres, running on 5432 port
- Make sure you have running activemq on port 61616
- Create database from sql: Code/ota-monitoring/src/main/resources/database_creation.sql
- Go to Code/ota-monitoring folder
- Build app by running: mvn package
- Start app from same location by: java -jar target/ota-monitoring.jar
- Wait application to start (tomcat) and navigate your browser to http://localhost:6789/ota-monitoring


*** Once started, application will create some mock data for application production testing, just as real time generation of mocked ERP data regarding package releases (new and update)
