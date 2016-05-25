#!/usr/bin/env bash
sbt 'project service' clean update compile assembly; java -jar service/target/scala-2.11/rest-server-service.jar -doc.root="/Users/leo/Downloads"