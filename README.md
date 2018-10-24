# vertx-hello
Hello, World application for Vert.x, Red Hat OpenShift Application Runtimes, and RH-SSO (Keycloak)

To compile and run locally, you need the Red Hat Maven repositories. A sample Maven settings.xml file is provided in the conf folder.

To build and deploy to openshift using a binary workflow, log in to openshift, create a project, and run:

$ mvn -Popenshift fabric8:deploy

To build and deploy to openshift using a source workflow, log in to openshift, create a project, and run:

$ oc new-app --name vertx-sso redhat-openjdk18-openshift:1.3~https://github.com/flozanorht/vertx-sso.git

(you need to expose the application after the build finishes)

The settings for the RH-SSO realm are hard-coded in MainVerticle.java

WIP: DO NOT CLONE YET!

