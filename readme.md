Tools used:<br/>
Spring Boot<br/>
Spring Data<br/>
H2 - in-memory relational database<br/>
ModelMapper - DTO-Entity mapper<br/>

App uses the H2 in-memory database.<br/>
Database is erased as soon as the application is stopped and recreated when application is started.<br/>
Database console can be accessed using URL http://localhost:8080/h2-console 
when the application is running. All necessary information for logging into the H2 console 
can be found in application.properties file.<br/><br/>

SpringDoc is used to generate API docs.<br/>
Documentation can be accessed using URL http://localhost:8080/swagger-ui.html when the application is running.<br/><br/>

Adding event JSON example:<br/>
{<br/>
&emsp; "name": "FIFA World Cup 2026",<br/>
&emsp; "type": "Football",<br/>
&emsp; "status": "INACTIVE",<br/>
&emsp; "startTime": "2026-01-01T20:00:00"<br/>
}<br/><br/>
