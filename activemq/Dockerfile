# Java 8 MUST be already present. IF present, remove two lines below !!!
# Install java
FROM java:8-jdk

RUN sed -i -e "s/us.archive.ubuntu.com/archive.ubuntu.com/g" /etc/apt/sources.list
RUN apt-get update && apt-get install -y curl

RUN curl http://apache.crihan.fr/dist/activemq/5.14.5/apache-activemq-5.14.5-bin.tar.gz | tar xz -C /opt/
RUN cp /opt/apache-activemq-5.14.5/conf/activemq.xml /opt/apache-activemq-5.14.5/conf/activemq.xml.save

EXPOSE 61616 8161

#CMD java -Xms1G -Xmx1G -Djava.util.logging.config.file=logging.properties -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote -#Djava.io.tmpdir=/opt/apache-activemq-5.14.5/tmp -Dactivemq.classpath=/opt/apache-activemq-5.14.5/conf -Dactivemq.home=/opt/apache-#activemq-5.14.5 -Dactivemq.base=/opt/apache-activemq-5.14.5 -Dactivemq.conf=/opt/apache-activemq-5.14.5/conf -Dactivemq.data=/opt/apache-#activemq-5.14.5/data -jar /opt/apache-activemq-5.14.5/bin/activemq.jar start

CMD ["java", "-jar", "/opt/apache-activemq-5.14.5/bin/activemq.jar", "start"]
