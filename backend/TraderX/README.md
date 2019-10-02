# CmpE 451 - Group 6 API Service

To test the service on your localhost make sure the following dependencies are installed on your compputer:

- Maven

- MySQL (version 8.x)

- JDK8 (application is written in Java 8, you might encounter possible failures using a higher version)

---
## NOTES

- Since this is the very first version of the app, some endpoints might be changed as the time goes. So, you 
better not use hardcoded endpoints in your app. i.e. use a structure like the following:

```
// Endpoint constants
API_URL = localhost:8080
USER_ROOT = "/users"
USER_SIGN_IN = "/signin"
USER_SIGN_UP = "/signup"
USER_GET_INFO = "/me"
...
...
```
Then use the variables in your requests. For instance
```
...
request.path = API_URL + USER_ROOT + USER_SIGN_IN 
... 
```

- Some exceptions are not defined properly yet and you may get "Something went wrong" error
message. In such a case, see the SpringBoot application's log for the detailed explanation.

- If you encounter an unexpected behaviour or an advice, please open an issue with `backend` tag.

- Swagger api is enabled on `localhost:8080/v2/api-doc` but have missing descriptions and is not
explained in a detailed way. However, it will help you to use APIs properly.

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

#### Step2: Run the application in the project directory (where pom.xml exists)
```
mvn spring-boot:run
```

#### Step3: Verify the application is running:

```
curl -X GET localhost:8080/trial/public
```



## Registration Scenario


---

To create an account, you have to use `/users/signup` endpoint. Compulsory fields are:

- Username (min 5, max 20 char)
- Password
- Email (regexp: "^[A-Z]{2}[0-9]{18}$")
- Latitude (regexp: "^(-?\d{1,5}(\.\d{1,10})?)$")
- Longitude (regexp: "^(-?\d{1,5}(\.\d{1,10})?)$") 

If you add a valid IBAN number (Starting with 2 letters followed by 18 numbers without any whitespace),
the created account will be trader user. You better check for the validation of the values before send
a request to the server to prevent the unnecessary server calls.  

Here is a sample request for creating a basic user(*POST*):

![sign_up](https://user-images.githubusercontent.com/44522401/66070741-94eeb500-e55a-11e9-9e7a-3c057ed8bf7e.png)

If the creation is successful, you will get a success response and server will create a Token specified for 
your client and valid for 30 mins. This token will be returned as String response. From now on, the server will recognize your client 
with this Token. You have to include this Token in your requests. To get a token for
already signed in user, use `signin` endpoint(*POST* again).

Now that you have the token, reach some privileged endpoints (*GET*) (If you remove the Token from your request
you will get 403 Error.):

![user_me](https://user-images.githubusercontent.com/44522401/66070740-94eeb500-e55a-11e9-8357-a7246ab29e91.png)


![trial_basic](https://user-images.githubusercontent.com/44522401/66070739-94eeb500-e55a-11e9-8b0f-9c1a7247a68e.png)

Try to access not allowed fields for you:

![trial_unauth](https://user-images.githubusercontent.com/44522401/66070738-94561e80-e55a-11e9-964e-4b3536c96955.png)




