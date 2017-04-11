Cheese Shop (Android Demo Application)
======================================

This project shows examples of the MVP architecture with the Repository Model pattern.

Webservice
----------

This project includes a webservice to supply data.
It is written in [Kotlin](http://kotlinlang.org/) using the [Spring Boot](https://projects.spring.io/spring-boot/) framework.

**This service is only for demonstration purposes only.**

**DO NOT USE IN PRODUCTION.**

To run the service navigate to the `cheese-shop-ws` directory and run `./gradlew bootRun`
<br/>

Mobile Application
------------------

The application displays a list of cheeses, comments, and prices as delivered by the service.
The application is written to run in an emulator running on the same machine as the service.

To change the urls for the service, either to move the service to a different machine or to run the app on a physical device,
update the [app/build.gradle](app/build.gradle) and change the following fields to the correct base urls:

```gradle
    buildConfigField "String", "SERVICE_BASE_URL", "\"http://10.0.2.2:8080/\""
    buildConfigField "String", "IMAGE_BASE_URL", "\"http://10.0.2.2:8080\""
```

### Libraries
  Libraries this demo is built around:
  * [Dagger 2](https://google.github.io/dagger/)
  * [RxJava 2](https://github.com/ReactiveX/RxJava)
  * [DBTools](https://github.com/jeffdcamp/dbtools-android)
  * Additional libraries (see [app/build.gradle](app/build.gradle))
