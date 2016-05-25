FROM debian:stable

ADD infra/java-8-debian.list /etc/apt/sources.list.d/java-8-debian.list

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886 && \
    apt-get update

RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections

RUN apt-get install oracle-java8-installer -y && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf /var/cache/oracle-jdk8-installer

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

ADD service/target/scala-2.11/rest-server-service.jar /coloso/service.jar

EXPOSE 8888

CMD ["java -jar /coloso/service.jar"]
