# Countries APIs

The project is for APIs development to get all countries information such as population, capital, flag, etc. from restcountries.eu. <br/>
This project is developed using Spring Boot with reactive programming in Java and circuit breaker. With microservice approach, a rate limiter should have been implemented in this project as well, however it is not available at the moment. <br/>
Moreover, the project is developed with inspiration of clean architecture from Uncle Bob.

## Apis Document

The api doc of the project is available at /api-docs/

## Note
The current port of this project is 8081, however, the port number can be changed in application properties. <br/>

At the time this project is being developed, the service at https://restcountries.eu is currently out of service for some reason. Therefore, I cloned the github of restcountries project at https://github.com/apilayer/restcountries to local machine to fetch data for this project. <br/>
Therefore, some resources such as images url might not displayed correctly at the moment.  