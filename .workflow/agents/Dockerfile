FROM jenkins/inbound-agent:alpine as jnlp

FROM maven:3.8.3-openjdk-17-slim

RUN apt-get update && \
    apt-get install -y \
        git \
        libfontconfig1 \
        libfreetype6

COPY --from=jnlp /usr/local/bin/jenkins-agent /usr/local/bin/jenkins-agent
COPY --from=jnlp /usr/share/jenkins/agent.jar /usr/share/jenkins/agent.jar

USER root

ENTRYPOINT ["/usr/local/bin/jenkins-agent"]