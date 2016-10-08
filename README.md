A test app that tries to use the following
1. Angular single page webapp
2. Spring boot backend
3. A AuthServer with tokens backed by a jdbc token store
4. Facebook login that links DB user to facebook account
5. username/password login

## to run
- Install java
- Install maven
- In base directory, run `mvn spring-boot:run` 
- go to `http://localhost:8080`

References:

[Spring Security and Angular JS](https://spring.io/guides/tutorials/spring-security-and-angular-js/)

[Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)

[Reddit OAuth with Spring](http://www.baeldung.com/spring-security-oauth2-authentication-with-reddit);
