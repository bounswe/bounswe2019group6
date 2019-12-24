# CmpE 451 - Group 6 Annotator Service

To test the service on your localhost make sure the following dependencies are installed on your compputer:

- Maven

- MySQL (version 8.x)

- JDK8 (application is written in Java 8, you might encounter possible failures using a higher version)
 
---

#### Step1: Create a database in MySQL server

```mysql
create database cmpe451;
```

#### Step2: Configure your app to connect database
in application.yml under resources, edit the fields properly:
```
username: root
password: root
```
 

#### Step4: Run the application in the project directory (where pom.xml exists)
```
mvn spring-boot:run
```

### Project Structure

 [![image](https://i.hizliresim.com/Z5mNdZ.png)](https://hizliresim.com/Z5mNdZ)


