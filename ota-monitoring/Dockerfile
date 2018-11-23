
#Install java
FROM java:8-jdk

VOLUME /tmp

# Install maven
RUN apt-get update
RUN apt-get --no-install-recommends install -y maven

WORKDIR /code

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

# Adding source, compile and package into a fat jar
ADD src /code/src
RUN ["mvn", "package"]

CMD ["java", "-jar", "target/ota-monitoring.jar"]
