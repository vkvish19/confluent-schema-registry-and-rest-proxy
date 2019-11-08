#!/usr/bin/env bash

cd /home/vishu/Softwares/Apache
#put this in any directory you like
wget http://central.maven.org/maven2/org/apache/avro/avro-tools/1.9.1/avro-tools-1.9.1.jar

AVRO_TOOL_JAR=/home/vishu/Softwares/Apache/avro-tools-1.9.1.jar
AVRO_PROJECT_FOLDER=/home/vishu/Learnings/Udemy/ApacheKafkaSeries/ConfluentSchemaRegistryAndRESTProxy/kafka-avro-java-course

#run this from your project folder.
cd $AVRO_PROJECT_FOLDER
java -jar $AVRO_TOOL_JAR tojson --pretty "$AVRO_PROJECT_FOLDER/customer-generic.avro"
java -jar $AVRO_TOOL_JAR tojson --pretty "$AVRO_PROJECT_FOLDER/customer-specific.avro"

#getting the schema
java -jar $AVRO_TOOL_JAR getschema "$AVRO_PROJECT_FOLDER/customer-specific.avro"