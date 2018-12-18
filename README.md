This app is unfinished, not working at the moment. Need help.

Tested with RH-SSO (product bits) standalone 7.2.GA and 7.2.4.GA. Using Vert.x productized buts from RHOAR, but so far updates in the 3.3 and 3.5 trains made no difference.


Ignore from now on, after I make it work with standalone RH-SSO I wanna use containerized RH-SSO on OpenShift, and run this app also containerized. Ideally, I would break the the REST API requests into a different application to show how to propagate SSO tokens between apps.

# vertx-hello
Hello, World application for Vert.x and Red Hat OpenShift Application Runtimes

To compile and run locally, you need the Red Hat Maven repositories. A sample Maven settings.xml file is provided in the conf folder.

To build and deploy to openshift using a binary workflow, log in to openshift, create a project, and run:

$ mvn -Popenshift fabric8:deploy

To build and deploy to openshift using a source workflow, log in to openshift, create a project, and run:

$ oc new-app --name vertx-hello redhat-openjdk18-openshift:1.3~https://github.com/flozanorht/vertx-hello.git

(you need to expose the application after the build finishes)

The resource URL is '/api/hello/AnyName'. Prepend with either 'localhost:8080' or the OpenShift route host name.

WIP: DO NOT CLONE YET!

