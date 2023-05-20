#!/bin/bash

clear
javac -d bin -cp bin:src/main/resources/gson/gson-2.10.1.jar src/main/java/mycompany/app/CustomJsonObject.java
javac -d bin -cp bin:src/main/resources/gson/gson-2.10.1.jar src/main/java/mycompany/app/ResponseObject.java
javac -d bin -cp bin:src/main/resources/gson/gson-2.10.1.jar src/main/java/mycompany/app/App.java
javac -d bin -cp bin:src/main/resources/gson/gson-2.10.1.jar src/main/java/mycompany/app/AppUtils.java
java -cp bin:src/main/resources/gson/gson-2.10.1.jar mycompany.app.App
