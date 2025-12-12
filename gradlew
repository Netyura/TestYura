#!/bin/sh

##############################################################################
# Gradle start up script for POSIX systems
##############################################################################

# Attempt to set APP_HOME
APP_HOME=$( cd "${BASH_SOURCE%/*}" && pwd )

exec "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"
