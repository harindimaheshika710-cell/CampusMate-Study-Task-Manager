#!/bin/sh
APP_HOME="$(cd "$(dirname "$0")" && pwd)"
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$CLASSPATH" ]; then
  echo "ERROR: gradle-wrapper.jar not found at $CLASSPATH"
  echo "Extract the full ZIP and run this command from the main project folder."
  exit 1
fi
exec java -cp "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
