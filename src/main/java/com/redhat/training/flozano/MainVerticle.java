package com.redhat.training.flozano;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
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

        JsonObject keycloakJson = new JsonObject()
            .put("realm", "vertx-hello")
            .put("auth-server-url", "http://localhost:8180/auth")
            .put("ssl-required", "external")
            .put("resource", "website")
            .put("public-client", true)
            .put("confidential-port", 0)
        ;

        LOG.info("RH-SSO realm: " + keycloakJson.getString("realm"));
        LOG.info("RH-SSO server: " + keycloakJson.getString("auth-server-url"));
        
        Router router = Router.router(vertx);

        OAuth2Auth oauth2 = KeycloakAuth.create(vertx, OAuth2FlowType.AUTH_CODE, keycloakJson);        
        OAuth2AuthHandler oauth2Handler = OAuth2AuthHandler.create(oauth2);
        oauth2Handler.setupCallback(router.get("/callback"));
        
        router.route("/protected/*").handler(oauth2Handler);
        
        router.route("/*").handler(StaticHandler.create("html")
            .setCachingEnabled(false)
        );
        
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
    
}
