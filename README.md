# Vert.x SSO (vertx-hello-sso)

This app is unfinished, not working at the moment. Need help.

Tested with RH-SSO (product bits) standalone 7.2.GA and 7.2.4.GA. Using Vert.x productized bits from RHOAR, but so far updates in the 3.3 and 3.5 trains made no difference.

To export the self-signed certificate from RH-SSO for use by the app, first log in to the RH-SSO admin console and then:

keytool -export -storepass password -alias server \
-keystore ../standalone/configuration/application.keystore \
-rfc -file /home/flozano/keycloack-ca.crt

keytool -importcert -keystore /home/flozano/keycloack.truststore -alias keycloack \
-storepass password -file /home/flozano/keycloack-ca.crt -rfc -noprompt

After I make it work with standalone RH-SSO I plan to use containerized RH-SSO on OpenShift, and run this app also containerized. Ideally, I would break the the REST API requests into a different application to show how to propagate SSO tokens between apps.

WIP: DO NOT CLONE YET!

