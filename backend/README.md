## System Manual

- To restore the DB dump, make sure MySQL8 is installed on your computer and MySQL server is running.  

- `Maven 3.x` must be installed on your device and your `Java` version must be 8. Make sure you have these requirements before
the next step.

- Run the following with your DB username as parameter:
```sh
$ sh restore.sh <username>
```

- Enter password on propmt if you have one.

- Fill the required fields (passwords etc.) on 
`TraderX/src/main/resources/application.yml` first. In order to use the service running with full functionality, all 
fields must be filled with valid values.

- Run the following to start up the server:

```sh
$ cd TraderX
$ mvn spring-boot:run
```
- Once you see the below line at the end of logs, you are all set:

```
INFO 1 --- [main] cmpe451.group6.Group6BackendService: Started Group6BackendService in 29.651 seconds (JVM running for 30.688)

```
- Send a request and see the server responding:

```sh
$ curl localhost:8080/trial/public

This message is public to all universe.% 
```


## API Documentation

You can find the API documentation [here](https://api.traderx.company/swagger-ui.html).  
Annotations are included in the above link but to see its documentation on annotation server, visit [here](https://annotator.traderx.company/swagger-ui.html).
