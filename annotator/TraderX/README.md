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

You need to add the same jwt keys used in the main server application. 
```
security:
  jwt:
    token:
      secret-key: secret-key
      expire-length: 3600000 # 1 hour  duration by default (in miliseconds)
  alpha-api-key1: "**"
  alpha-api-key2: "***************"
```
Main database url, user, password info which were used in backend application.  
```
app.datasource:
  main:
    url: jdbc:mysql://main_db:3306/cmpe451?allowPublicKeyRetrieval=true&useSSH=false
    username: user
    password: pass
```
Annotator database url, user, password info
```
  annotator:
    url: jdbc:mysql://annotator_db_url:3306/cmpe451?allowPublicKeyRetrieval=true&useSSH=false
    username: user
    password: pass
```

#### Step4: Run the application in the project directory (where pom.xml exists)
```
mvn spring-boot:run
```

### Project Structure

 [![image](https://i.hizliresim.com/Z5mNdZ.png)](https://hizliresim.com/Z5mNdZ)


