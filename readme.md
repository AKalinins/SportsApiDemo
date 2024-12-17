Tools used:<br/>
Spring Boot<br/>
Spring Data<br/>
H2 - in-memory relational database<br/>
ModelMapper - DTO-Entity mapper<br/>
JUnit<br/>
Mockito<br/><br/>

App uses the H2 in-memory database.<br/>
Database is erased as soon as the application is stopped and recreated when application is started.<br/>
Database console can be accessed using URL http://localhost:8080/h2-console 
when the application is running. All necessary information for logging into the H2 console 
can be found in application.properties file.<br/><br/>

SpringDoc is used to generate API docs.<br/>
Documentation can be accessed using URL http://localhost:8080/swagger-ui.html when the application is running.<br/><br/>
