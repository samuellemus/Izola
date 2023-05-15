#!/bin/bash
mvn clean
mvn compile
mvn package
java -cp target/refrigador-1.0-SNAPSHOT.jar com.mycompany.app.App
mvn clean
