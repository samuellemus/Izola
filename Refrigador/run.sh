#!/bin/bash
mvn compile
mvn package
clear
java -cp target/Refrigador-1.0-SNAPSHOT.jar mycompany.app.App
mvn clean
