package org.devisions.labs.savings.vx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import org.devisions.labs.savings.vx.commons.DateTimeUtils;
import org.devisions.labs.savings.vx.config.MainConfig;
import org.devisions.labs.savings.vx.services.SavingsAccountsServiceVerticle;
import org.devisions.labs.savings.vx.webapi.WebVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;


/**
 * The startup verticle.
 *
 * @author devisions
 */
public class StartVerticle extends AbstractVerticle {

    private static final String SERVICE_CONFIG_FILE = "config/service-config.json";

    private static final Logger logger = LoggerFactory.getLogger(StartVerticle.class);

    @Override
    public void start(Future<Void> startFuture) {

        Instant begin = Instant.now();
        logger.info("\n\n------------------------------------\n" +
            "   savings-vx service is starting\n" +
            "------------------------------------\n");

        Future<Void> startupSteps = MainConfig.getInstance().init(SERVICE_CONFIG_FILE)
            .compose(v -> deploySavingsAccountsServiceVerticle())
            .compose(v -> deployWebApiVerticle());

        startupSteps.setHandler(v -> {
            if (v.succeeded()) {
                logger.info("Startup complete in {}\n",
                    DateTimeUtils.humanReadableDuration(Duration.between(begin, Instant.now())));
                startFuture.complete();
            } else {
                logger.error("Startup error: {}", v.cause().getMessage());
                startFuture.fail(v.cause());
            }
        });

    }

    @Override
    public void stop() {
        logger.info("Shutdown complete.");
    }

    /** Deploy the {@link SavingsAccountsServiceVerticle}. */
    @SuppressWarnings("Duplicates")
    private Future<Void> deploySavingsAccountsServiceVerticle() {

        Future<Void> resultFuture = Future.future();

        Future<String> deploymentFuture = Future.future();
        deploymentFuture.setHandler(ar -> {
            if (ar.succeeded()) {
                logger.debug("SavingsAccountsServiceVerticle deploy done.");
                resultFuture.complete();
            } else {
                logger.debug("SavingsAccountsServiceVerticle deploy error: {}", ar.cause().getMessage());
                resultFuture.fail(ar.cause());
            }
        });

        vertx.deployVerticle(SavingsAccountsServiceVerticle.class,
            new DeploymentOptions(),
            deploymentFuture.completer()
        );

        return resultFuture;
    }

    /** Deploy the {@link WebVerticle}. */
    @SuppressWarnings("Duplicates")
    private Future<Void> deployWebApiVerticle() {

        Future<Void> resultFuture = Future.future();

        Future<String> deploymentFuture = Future.future();
        deploymentFuture.setHandler(ar -> {
            if (ar.succeeded()) {
                logger.debug("WebVerticle deploy done.");
                resultFuture.complete();
            } else {
                logger.debug("WebVerticle deploy error: {}", ar.cause().getMessage());
                resultFuture.fail(ar.cause());
            }
        });

        vertx.deployVerticle(
            WebVerticle.class,
            new DeploymentOptions().setInstances(
                MainConfig.getInstance().getConfig()
                    .getJsonObject("webApi").getInteger("verticles")),
            deploymentFuture.completer()
        );

        return resultFuture;

    }

}
