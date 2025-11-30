FROM eclipse-temurin:21-alpine

VOLUME /tmp

ARG APP_FILE
ARG APP_VERSION
ENV INFO_APP_VERSION ${APP_VERSION:-undef2}


RUN echo -e "\
    \t ENV::APP_FILE = ${APP_FILE} \n\
    \t ENV::INFO_APP_VERSION = ${INFO_APP_VERSION} \n\
"
RUN java -XX:+PrintFlagsFinal -XX:MaxRAMPercentage=75.0 -version | grep -Ei 'maxheapsize|maxram'


COPY ${APP_FILE} app.war

ENTRYPOINT exec java $JAVA_OPTS -server -Djava.awt.headless=true -Dfile.encoding=utf-8 \
	-Duser.timezone=America/Santiago -Duser.language=es -Duser.region=CL \
	-XX:+CrashOnOutOfMemoryError  -XX:MaxRAMPercentage=75.0 \
	-Djava.security.egd=file:/dev/./urandom \
	-jar /app.war
