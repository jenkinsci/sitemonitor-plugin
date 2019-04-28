#!/bin/sh
rm -rf work/plugins
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home
export MAVEN_OPTS="$MAVEN_OPTS -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"
#mvn -Dmaven.test.skip=true -DskipTests=true -Djetty.port=9191 clean compile org.jenkins-ci.tools:maven-hpi-plugin:run
mvn -Djetty.port=9191 clean compile org.jenkins-ci.tools:maven-hpi-plugin:run
