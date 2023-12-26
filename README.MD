# Scottish Power Tech Task

## Database Structure

As the assignment definition stated that the data table structure had not yet been agreed, I've initially gone with the simplest
structure that works with the given requirements. This means there is currently only one table that holds both the meter reading 
details and the relevant Account ID.

It would have been logical to have stored the data into separate relational tables, e.g. with an additional table specifically 
for the Account details. However, this would introduce extra requirements, and questions, such as the initial creation of an 
Account, which are out of scope for this task.

Therefore, there is no specific action of Account creation, so no checks in place (yet) as to whether the submitted readings 
are for an existing, active Account (which would be a normal part of the process).

## Development

Build the app using:

```code
    mvn clean package
```

This runs all Unit and Integration Tests

### Integration Tests

The Integration Tests use Cucumber and Spring Boot Test (the latter for starting the application)

The code for the integration tests can be found in `${PROJECT_ROOT}/src/it/java` and the Gherkin style test feature
files can be found at `${PROJECT_ROOT}/src/it/resources/features`

The integration test setup has been done using junit-platform-suite, which is now the recommended approach for Cucumber and JUnit-5. 
The tests wil be triggered as part of the Maven build, but it's possible to also run them directly using class `RunCucumberIT` from 
within your IDE.

The integration tests have now been updated to use a PostgreSQL TestContainer.

## Running the app

The app is a standard Spring boot project, therefore running the build step above will create a runnable artifact, located at 
`${PROJECT_ROOT}/target/scottish-power-tech-task.jar`

This can be run with the command:
```
    java -jar scottish-power-tech-task.jar
```

...or from the root directory of the project, with the command...

```
   mvn spring-boot:run
```

The app will be running on port 8081, which you can verify by following the link `http://localhost:8081/actuator`

### Live Reload

When run with the `spring-boot:run` command, or directly from the IDE, the app is running on live reload mode.
This means that any changes made to the code while the app is running will restart the app with the latest changes, which
allows an experimental development style.

### SWAGGER

The app can be viewed, or interacted with, on Swagger at `http://localhost:8081/swagger-ui`

The endpoints are documented with descriptions and any required input.