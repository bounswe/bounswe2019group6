# CmpE 451 - Group 6 Backend Service

__NOTE: SpringFramework v1.5.6 and MySQL8 is used in the system.__

You can find the API documentation [here](https://api.traderx.company/swagger-ui.html).  
Annotations are included in the above link but to see its documentation on annotation server, visit [here](https://annotator.traderx.company/swagger-ui.html).

# To run the service:

1) Create database named `cmpe451`

2) You can run in Docker container or in JVM from jar file:

### Running via jar file:

java -jar group6backend-1.0.jar

### Running in Docker container:

* Fill the necessary fields on `src/main/resources/application.yml` file (Database username, password, email password, etc.)
* In the directory where `Dockerfile` exists, run the commands:
(Note that, in order to use full functionality of AWS image upload, the environment variables on the second line below must be set properly)
```sh
$ docker build . -t traderx:<tag>
$ docker run -d --restart=unless-stopped -e ENDPOINT_URL=${ENDPOINT_URL} -e BUCKET_NAME=${BUCKET_NAME} -e ACCESS_KEY=${ACCESS_KEY} -e SECRET_KEY=${SECRET_KEY} -p 8080:8080 --name traderx traderx:<tag>
```

