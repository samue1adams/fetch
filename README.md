##FETCH REWARDS coding assessment

###RECEIPT PROCESSOR

This is a spring boot application with H2 in-memory DB spun up at runtime. Using Spring JPA to handle basic saves and retrieval of receipt data. 

assumptions: java 8 is installed. Gradle is installed as the build tool 

to run: 

```./gradlew bootRun```

run tests: 

```./gradlew clean test```


Future enhancements:

* create rest exception handler to give more detailed information on errors. (db exceptions would be most likely for this small of an API)

* move hard coded point values to Constants class or properties file

* swagger endpoints for public documentation

* api security

* add more tests for the controller class and save and retrieval of receipts. (figure points processing was important part of assessment)





