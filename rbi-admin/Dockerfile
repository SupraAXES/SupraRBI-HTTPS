FROM ubuntu:22.04 as build

# install yarn maven
RUN apt-get update && apt-get install -y openjdk-17-jdk maven

WORKDIR /
COPY rbi rbi
RUN cd rbi \
    && mvn clean install
    # result in /rbi/target/rbi-0.0.1-SNAPSHOT.jar

FROM ubuntu:22.04

ARG DEBIAN_FRONTEND=noninteractive
ENV LC_ALL=C.UTF-8
ENV LANG=C.UTF-8
ENV LANGUAGE=C.UTF-8

# Install OpenJDK
# Update the system
RUN apt-get -y update \
    && apt-get install -y openjdk-17-jdk \
    && mkdir -p /opt/supra/logs

COPY --from=build /rbi/target/rbi-*-SNAPSHOT.jar /opt/supra/java/
COPY config /opt/supra/config.default
COPY entry.sh /opt/supra/bin/entry.sh
RUN mkdir -p /opt/supra/config \
    && chmod +x /opt/supra/bin/entry.sh

# Expose the nginx port
EXPOSE 8080

CMD /opt/supra/bin/entry.sh
