package org.devisions.labs.savings.vx.webapi;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.devisions.labs.savings.vx.config.MainConfig;
import org.devisions.labs.savings.vx.models.SavingsAccount;
import org.devisions.labs.savings.vx.services.SavingsAccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Web API verticle.
 *
 * @author devisions
 */
public class WebApiVerticle extends AbstractVerticle implements BaseWebApi {

    private SavingsAccountsService savingsAccountsService;

    private static final JsonObject config = MainConfig.getInstance().getConfig();

    private static final Logger logger = LoggerFactory.getLogger(WebApiVerticle.class);

    @Override
    public void start(Future<Void> startFuture) {

        String savingsAccountsServiceAddress = config.getJsonObject("eventbus")
            .getString(SavingsAccountsService.Config.EB_ADDRESS);
        this.savingsAccountsService = SavingsAccountsService.createProxy(vertx, savingsAccountsServiceAddress);

        Future<Void> startupSteps = startupCommCheck()
            .compose(v -> apiSetup());

        startupSteps.setHandler(startFuture.completer());

    }

    /** Communication checks, performed at startup (verticle deployment) time. */
    private Future<Void> startupCommCheck() {

        Future<Void> responseFuture = Future.future();
        savingsAccountsService.commCheck(event -> {
            if (event.succeeded()) {
                logger.debug("Startup comm check ok.");
                responseFuture.complete();
            } else {
                logger.debug("Startup comm check failed! Error: {}", event.cause().getMessage());
                responseFuture.fail(event.cause());
            }
        });

        return responseFuture;

    }

    /** Web API setup (routes & http server). */
    private Future<Void> apiSetup() {

        Future<Void> responseFuture = Future.future();

        Router router = Router.router(vertx);

        // __________ API ROUTES __________

        router.get("/savings/byowner/:ownerId").handler(this::getSavingsAccountByOwnerHandler);
        router.post().handler(BodyHandler.create());
        router.post("/savings/byowner/:ownerId").handler(this::storeSavingsAccountByOwnerHandler);

        // __________ HTTP Server __________

        int httpPort = config.getJsonObject("webApi").getInteger("httpPort");
        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(httpPort, ar -> {
                if (ar.succeeded()) {
                    logger.info("WebApi server is running on port {}.", httpPort);
                    responseFuture.complete();
                } else {
                    logger.error("Failed to start WebApi server. Reason: {}", ar.cause().getMessage());
                    responseFuture.fail(ar.cause());
                }
            });

        return responseFuture;

    }

    /** The handler of "getSavingsAccountByOwner" requests. */
    private void getSavingsAccountByOwnerHandler(RoutingContext context) {

        HttpServerRequest request = context.request();
        String ownerId = request.getParam("ownerId");
        logger.debug("getSavingsAccountByOwnerHandler > Starting with ownerId '{}'.", ownerId);
        savingsAccountsService.getSavingsAccountByOwner(ownerId, reply -> {
            HttpServerResponse response = context.response();
            response.putHeader("content-type", "application/json");
            JsonObject accountJson = reply.result();
            if (accountJson != null) {
                respondOKWithJsonContent(response, accountJson.encodePrettily());
            } else {
                respondNotFoundWithoutContent(response);
            }
        });

    }

    /** The handler of "storeSavingsAccountByOwner" requests. */
    private void storeSavingsAccountByOwnerHandler(RoutingContext context) {

        HttpServerRequest request = context.request();
        String ownerId = request.getParam("ownerId");
        JsonObject accountJson = context.getBodyAsJson();
        logger.debug("storeSavingsAccountByOwnerHandler > Starting with ownerId '{}' and accountJson '{}'.", ownerId, accountJson);
        savingsAccountsService.storeSavingsAccountByOwner(ownerId, SavingsAccount.fromJson(accountJson), reply -> {
            HttpServerResponse response = context.response();
            if (reply.succeeded()) {
                respondCreatedWithJsonContent(response, reply.result().encodePrettily());
            } else {
                respondForbiddenWithJsonContent(response, reply.cause().getMessage());
            }
        });

    }

}
