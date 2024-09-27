#!/bin/bash -e

launch_engine() {
  echo "Launching engine"

  ENGINE_PATH="/opt/supra/java/"
  JAVA_FILE=$(find $ENGINE_PATH -name "*.jar")

  DEFAULT_JAVA_OPTS="-XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m \
                     -Xms512m -Xmx512m -Xmn128m -Xss256k \
                     -XX:SurvivorRatio=8 \
                     -XX:+PrintGCDetails \
                     -XX:ParallelGCThreads=4 \
                     -Xverify:none"

  JAVA_RUNNING_OPTIONS=${JAVA_OPTS:=$DEFAULT_JAVA_OPTS}

  java -jar $JAVA_RUNNING_OPTIONS $JAVA_FILE > /opt/supra/logs/app.log &
  echo "Engine Java launched with options: $JAVA_RUNNING_OPTIONS"
}

if [ ! -f /opt/supra/config/token.json ] || [ ! "$(ls -A /opt/supra/config/policies)" ]; then
  echo "Copying default config files"
  cp -r /opt/supra/config.default/* /opt/supra/config/
fi

launch_engine

# Wait for any process to exit
wait -n

# Exit with status of process that exited first
exit $?
