package com.redhat.training.flozano;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.OAuth2AuthHandler;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth;
import io.vertx.ext.auth.oauth2.KeycloakHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void stop() {
        LOG.info("Shutting down Hello app...");
    }

    @Override
    public void start(Future<Void> future) {

        LOG.info("Starting Hello app...");

            //.put("truststore", "/home/flozano/sso.truststore")
            //.put("truststore-password", "password")
            //.put("disable-trust-manager", true)
            //.put("allow-any-hostname", true)

        JsonObject keycloakJson = new JsonObject()
            .put("realm", "vertx-hello")
            .put("auth-server-url", "https://localhost:8543/auth")
            .put("ssl-required", "external")
            .put("resource", "website")
            .put("public-client", true)
            .put("confidential-port", 0)
        ;

        LOG.info("RH-SSO realm: " + keycloakJson.getString("realm"));
        LOG.info("RH-SSO server: " + keycloakJson.getString("auth-server-url"));
        
       HttpClientOptions httpOptions = new HttpClientOptions()
           .setVerifyHost(false)
           .setTrustAll(true)
           //.setTrustStoreOptions(new JksOptions()
               //.setPath("/home/flozano/sso.truststore")
               //.setPassword("password"))
       ;
       
        Router router = Router.router(vertx);

        //OAuth2Auth oauth2 = KeycloakAuth.create(vertx, OAuth2FlowType.AUTH_CODE, keycloakJson);
        OAuth2Auth oauth2 = KeycloakAuth.create(vertx, OAuth2FlowType.AUTH_CODE, keycloakJson, httpOptions);                
        OAuth2AuthHandler oauth2Handler = OAuth2AuthHandler.create(oauth2);
        oauth2Handler.setupCallback(router.get("/callback"));
        
        router.route("/protected/*").handler(oauth2Handler);
        router.route("/privateapi/*").handler(oauth2Handler);
        
        router.route("/*").handler(StaticHandler.create("html")
            .setCachingEnabled(false)
        );
        
        router.get("/publicapi/hello/:greeting")
            .produces("application/text")
            .handler(this::hello);

        router.get("/privateapi/hello/:greeting")
            .produces("application/text")
            .handler(this::hello);

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(8080, result -> {
                if (result.succeeded()) {
                    LOG.info("Aceppting HTTP requests on port 8080");
                    future.complete();
                }
                else {
                    future.fail(result.cause());
                }
            });
    }
    
    //XXX should use a confidential instead of a public realm?
    
    private void hello(RoutingContext rc) {
    	String greeting = rc.request().getParam("greeting");
        String host = rc.request().host();

        String user = "anonymous";
        if (rc.user() != null && rc.user().principal() != null) {
            String accessToken = KeycloakHelper.rawAccessToken(rc.user().principal());
            JsonObject token = KeycloakHelper.parseToken(accessToken);
            user = token.getString("preferred_username");
        }
          
        LOG.info("Got API request with greeting = '" + greeting + "' ...");
        
      	rc.response().end(greeting + " from " + user + "@" + host + "\n");
    }

}
