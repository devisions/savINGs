#!/usr/bin/env bash

##
## file: run-dev.sh
## desc: This script can be used for running the app in 'redeploy' mode
##       (code changes trigger redeployment of verticles).
##

export LAUNCHER="io.vertx.core.Launcher"
export VERTICLE="org.devisions.labs.savings.vx.StartVerticle"
export CMD="mvn compile"
export VERTX_CMD="run"

mvn compile dependency:copy-dependencies
java \
  -cp  $(echo target/dependency/*.jar | tr ' ' ':'):"target/classes" \
  ${LAUNCHER} ${VERTX_CMD} ${VERTICLE} \
  --redeploy="src/main/**/*" --on-redeploy="$CMD" \
  --launcher-class=${LAUNCHER} \
  $@
