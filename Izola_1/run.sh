#!/bin/bash
# mvn compile
# mvn package
# clear
# java -cp target/Refrigador-1.0-SNAPSHOT.jar mycompany.app.AppDriver
# mvn clean

javac -d bin -cp bin:resources/gson-2.10.1.jar src/main/java/mycompany/app/CustomJsonObject.java
javac -d bin -cp bin:resources/gson-2.10.1.jar src/main/java/mycompany/app/ResponseObject.java
javac -d bin -cp bin:resources/gson-2.10.1.jar src/main/java/mycompany/app/AppUtils.java
javac -d bin -cp bin:resources/gson-2.10.1.jar src/main/java/mycompany/app/App.java

# java -cp bin:resources/gson-2.10.1.jar mycompany.app.App
java -cp bin:resources/gson-2.10.1.jar mycompany.app.CustomJsonObject
