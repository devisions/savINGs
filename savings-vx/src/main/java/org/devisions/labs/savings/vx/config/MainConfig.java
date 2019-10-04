package org.devisions.labs.savings.vx.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.devisions.labs.savings.vx.commons.testing.FixedClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;


/**
 * Simplistic main service configuration component.
 *
 * @author devisions
 */
public class MainConfig {

    private static MainConfig instance = new MainConfig();

    private static final Logger logger = LoggerFactory.getLogger(MainConfig.class);

    private JsonObject config;

    private boolean inited;

    /**
     * This flag is used for setting up a fixed clock during time-sensitive unit tests.<br/>
     * Used values: 0 = not explicitly set, 1 = standard mode, 2 = testing mode<br/>
     * These are also used to prevent subsequent (and wrong) setRunningMode(_) calls.
     */
    private byte runningMode = 0;

    private Clock clock;

    /** Get the instance of MainConfig. */
    public static MainConfig getInstance() {
        return instance;
    }

    /** Initialize the configuration based on a config file. */
    public Future<Void> init(String configFilePath) {
        return init(Vertx.currentContext().owner(), configFilePath);
    }

    /**
     * Initialize the configuration based on a config file.<br/>
     * This version expects a provided vertx instance and it is used within the tests
     * as in that context Vertx.currentContext() does not exist.
     */
    private Future<Void> init(Vertx vertx, String configFilePath) {

        Future<Void> initConfigFuture = Future.future();

        ConfigStoreOptions configStoreOptions = new ConfigStoreOptions();
        if (configFilePath != null) {
            configStoreOptions.setType("file").setConfig(new JsonObject().put("path", configFilePath));
        }

        ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(configStoreOptions));

        retriever.getConfig(ar -> {
            if (ar.failed()) {
                logger.error("Error loading config from '{}' file: {}", configFilePath, ar.cause().getMessage());
                initConfigFuture.fail(ar.cause());
            } else {
                this.config = ar.result();
                logger.info("Loaded config from '{}' file.", configFilePath);
                initClock();
                initConfigFuture.complete();
            }
        });

        return initConfigFuture;

    }

    /** Init the Clock based on a (possible) previously <code>setTestingMode()</code> call. */
    private void initClock() {
        if (isTestingMode()) {
            logger.info("RUNNING IN TESTING MODE!!! Using a fixed clock.");
            this.clock = new FixedClock(
                MainConfig.getInstance().getConfig().getJsonObject("testing").getString("mockWorkingTimeUTC"));
        } else {
            logger.info("Using a standard clock.");
            this.clock = Clock.systemDefaultZone();
        }
    }


    /** Get the config object. */
    public JsonObject getConfig() {
        return config;
    }


    /** It tells if this component is initialized. */
    public boolean isInited() {
        return inited;
    }


    /** Get the system Clock. */
    public Clock getClock() {
        return clock;
    }

    /** Tell if running in 'testing mode'. */
    private boolean isTestingMode() {
        return runningMode == 2;
    }

    /**
     * Use it to enable 'testing mode'. It should be called before <code>init(configFilePath)</code><br/>
     * as it sets - as part of the MainConfig bootstrap - a fixed clock used within the time-sensitive unit tests.
     */
    public void enableTestingMode() {
        if (runningMode == 0) {
            runningMode = 2;
        } else {
            logger.warn("Skipped executing enableTestingMode(_) again! Already having runningMode={}", runningMode);
        }
    }

}

