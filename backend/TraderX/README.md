# CmpE 451 - Group 6 API Service

To test the service on your localhost make sure the following dependencies are installed on your compputer:

- Maven

- MySQL (version 8.x)

- JDK8 (application is written in Java 8, you might encounter possible failures using a higher version)

---
## NOTES

- Some exceptions are not yet handled explicitly and you may get "[GENERIC ERROR]" error
message. In such a case, see the SpringBoot application's log for the detailed explanation and
please report it to backend team.

- If you encounter an unexpected behaviour or an advice, please open an issue with `backend` tag.

- API documentation is enabled on `localhost:8080/swagger-ui.html`.

- If you see a wrong defined response codes on ApiDoc, please report to the backend team.

- If you want to disable email verification for new users, comment out the line 65 on `cmpe451.group6.authorization.security.OncePerRequestFilter`

- During signing up process, if you supply a valid IBAN, the user will be Trader.

- The link sent to user's email to reset password will not include new password. The new value
should be asked to user and then this request has to be sent to the server with `newpassword` value
appended to the link. Frontend team's help is appreciated to create a basic page for this purpose. 

- Endpoint changes:

/users/signin --> /login
/users/signup --> /signup

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

#### Step3: Configure email properties 
on `EmailServiceConfig.java`, use group email adress & password
```
static private final String senderAdress = "??";
static private final String senderPassword = "??";
```

#### Step4: Run the application in the project directory (where pom.xml exists)
```
mvn spring-boot:run
```

#### Step5: Verify the application is running:

```
curl -X GET localhost:8080/trial/public
```



