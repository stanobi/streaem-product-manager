# Streaem Product Service
This is an application which helps to manage product data

## Content
View Project Structure description and Instruction.

```
.
├── /src/
├──── /main/
├────── /java/
├──────── /com/streaem/productmanager/
├────────── /config                                 -> contains all app configuration
├────────── /controller                             -> contains all restful api controller
├────────── /dto                                    -> contains all generated data transfer objects
├────────── /exception                              -> contains exception handling class and custom exceptions
├────────── /model                                  -> contains datasource entity/pojo
├────────── /repo                                   -> contains class used to access and manage data
├────────── /restclient                             -> contains interface used to consume api
├────────── /service                                -> contains classes containing the app business logic
├────────── /util                                   -> contains classes containing common operations and values
├────────── ProductManagerApplication               -> Main Class
├────── /resources
├──────── /static
├──────── /templates
├──────── application.properties                    -> contains all external configurations
├──────── logback.xml                               -> contains logging configuration
├──── /test                                         -> contains all test classes
├── /target                                         -> contains auto generated files
├── .gitignore                                      -> contains all files and folder that shouldn't be pushed to git
├── pom.xml                                         -> file used to manage dependencies   
├── system.properties                               -> heroku config file instructing heroku on the java version to be used. 
└── README.md                                       -> application structure description and startup/setup guide
    
Instructions 

    1. To setup the application,  
        a. Ensure that jdk17 is installed
        b. Ensure that maven is installed 
        c. You will have to clone the project.
        d. Go to the project root directory on the terminal 
        e. Run the maven command on the terminal "mvn clean install" to generate the openapi java classes
        f. Ensure that port 8081 is available to be used
    
    2. Start up the application by running the command on the terminal 'mvn spring-boot:run' in the root directory
    
    3. Considering that we have the swagger-ui configured for the service, View api detail via browser by visiting "http://localhost:8081/swagger-ui.html"

    4. To Test: we can test via swagger-ui by visiting any of the links provided in the step 3 and providing the parameter as required. 

    5. To run unit test, you can run the command on the terminal 'mvn test'
    
    6. You can also run application on docker, follow the instruction below.
        a. Ensure that you have docker running
        b. update the application-docker.properties with the right url for a running garryturk/mock-product-data service
        c. Build the docker image by running this command on terminal.
            mvn clean package
            (this will use trigger jib plugin to build docker image locally).
        d. After thats complete, you can start the application by running this command below
            docker run -p 8081:8081 productmanager
        e. you can visit application via browser 
            http://localhost:8081/swagger-ui.html

```