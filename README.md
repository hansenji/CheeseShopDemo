Cheese Shop (Android Demo Application)
======================================

This project shows examples of the MVP architecture with the Repository Model pattern.
It is written in Kotlin. For a Java version of the application check the [Java Version](https://github.com/hansenji/CheeseShopDemo/tree/JAVA) release.

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
  * [Retrofit](http://square.github.io/retrofit/)
  * [Jackson-Databind](https://github.com/FasterXML/jackson-databind)
  * [DBTools](https://github.com/jeffdcamp/dbtools-android)
  * [Android Job](https://github.com/evernote/android-job)
  * Additional libraries (see [app/build.gradle](app/build.gradle))
