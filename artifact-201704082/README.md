Project Name: Smart Photo Classifier
Team Members: Tejal Sabnis, Tinu Tomson

Project URL: https://project20140410.appspot.com/

This project might not run on the local dev app engine because it uses Blobstore API. (upload functionality will fail)
Please deploy it to Google App Engine in order to use it.

appengine-standard-archetype
============================

This is a generated App Engine Standard Java application from the appengine-standard-archetype archetype.


* Java 7
* [Maven](https://maven.apache.org/download.cgi) (at least 3.3.9)
* [Gradle](https://gradle.org/gradle-download/) (optional)
* [Google Cloud SDK](https://cloud.google.com/sdk/) (aka gcloud)

Initialize the Google Cloud SDK using:

    gcloud init

This project is ready to run.



    mvn appengine:run


    mvn appengine:deploy


    mvn test



    gradle appengineRun


    gradle appengineDeploy


    gradle test

