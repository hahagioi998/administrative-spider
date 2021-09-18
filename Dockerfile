FROM openjdk:8-jdk

WORKDIR /app

ARG JAR_FILE

ADD ${JAR_FILE} app.jar

ARG OPS="-Xms64m -Xmx256m"

ENV JAVA_OPS=$OPS

RUN mkdir /data

VOLUME ["/data"]

ENTRYPOINT ["/bin/bash", "-c", "java $JAVA_OPS -jar app.jar"]
