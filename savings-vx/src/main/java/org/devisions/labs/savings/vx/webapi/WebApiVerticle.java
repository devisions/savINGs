package org.devisions.labs.savings.vx.webapi;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.devisions.labs.savings.vx.config.MainConfig;
import org.devisions.labs.savings.vx.models.SavingsAccount;
import org.devisions.labs.savings.vx.repos.SavingsAccountsRepoVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebApiVerticle extends AbstractVerticle implements BaseWebApi {

    private String savingsAccountsRepoAddress;

    private EventBus eventBus;

    private static int httpPort;

    private static final JsonObject config = MainConfig.getInstance().getConfig();

    private static final Logger logger = LoggerFactory.getLogger(WebApiVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        this.savingsAccountsRepoAddress = config.getJsonObject("eventbus")
            .getString(SavingsAccountsRepoVerticle.CONFIG.EVENTBUS_ADDRESS);
        this.eventBus = vertx.eventBus();

        Future<Void> startupSteps = startupSavingsAccountsRepoCheck()
            .compose(v -> apiSetup());

        startupSteps.setHandler(startFuture.completer());

    }

    private Future<Void> startupSavingsAccountsRepoCheck() {

        Future<Void> responseFuture = Future.future();

        eventBus.send(savingsAccountsRepoAddress, new JsonObject(),
            new DeliveryOptions()
                .addHeader(SavingsAccountsRepoVerticle.IO.RQ_NAME, SavingsAccountsRepoVerticle.IO.COMM_CHECK_RQ)
                .setSendTimeout(3_000), // waiting for 3 seconds to get a reply
            event -> {
                if (event.succeeded()) {
                    logger.info("Startup comm check: {}", event.result().body());
                    responseFuture.complete();
                } else {
                    responseFuture.fail(event.cause().getMessage());
                }
            });

        return responseFuture;

    }

    /** WebApi setup (routes & http server). */
    private Future<Void> apiSetup() {

        Future<Void> responseFuture = Future.future();

        Router router = Router.router(vertx);

        // ------- API ROUTES -------
        router.get("/savings/:ownerId").handler(this::getSavingsAccountByOwnerHandler);

        httpPort = config.getJsonObject("webApi").getInteger("httpPort");
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
        eventBus.send(savingsAccountsRepoAddress, new JsonObject(),
            new DeliveryOptions()
                .addHeader(SavingsAccountsRepoVerticle.IO.RQ_NAME, SavingsAccountsRepoVerticle.IO.GET_SAVINGS_ACCOUNT_BY_OWNER_RQ)
                .addHeader(SavingsAccountsRepoVerticle.IO.OWNER_ID, request.getParam("owner"))
                .setSendTimeout(3_000), // waiting up to 3 seconds for a reply
            event -> {
                HttpServerResponse serverResponse = context.response();
                if (event.succeeded()) {
                    JsonObject accountJson = (JsonObject) event.result().body();
                    SavingsAccount account = Json.decodeValue(
                        accountJson.getString(SavingsAccountsRepoVerticle.IO.GET_SAVINGS_ACCOUNT_BY_OWNER_RS),
                        SavingsAccount.class);
                    respond(serverResponse, Json.encodePrettily(account));
                } else {
                    respondError(context.response(), event.cause().getMessage(),
                        HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                }
            });

    }

}
