FROM gamecore:latest

MAINTAINER Pseudow

WORKDIR /usr/minecraft/

ENV PROJECT_NAME=altarise-fallenkingdom
ENV FOLDER=/usr/minecraft/plugins/

RUN bash download_artifacts.sh -C

# Downloading dependencies....

RUN ls -C
RUN ls plugins/ -C

RUN echo Starting minecraft server...

ENV MC_SERVER_GAME_ID=fk-classic

ENTRYPOINT ["java", "-Xmx3072M", "-XX:+UseG1GC", "-XX:+DisableExplicitGC", "-jar", "paper.jar"]
