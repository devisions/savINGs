package org.devisions.labs.savings.vx.repos;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.devisions.labs.savings.vx.commons.Errors;
import org.devisions.labs.savings.vx.config.MainConfig;
import org.devisions.labs.savings.vx.models.SavingsAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * The verticle for Saving accounts repository.
 *
 * @author devisions
 */
public class SavingsAccountsRepoVerticle extends AbstractVerticle implements BaseRepo {

    /** a minimal store that contains one SavingsAccount per ownerId */
    private Map<String, SavingsAccount> store;

    private static final Logger logger = LoggerFactory.getLogger(SavingsAccountsRepoVerticle.class);

    @Override
    public void start(Future<Void> startFuture) {

        setupEventBusCommunication().setHandler(startFuture.completer());

    }

    /** Register itself as an event bus consumer. */
    private Future<Void> setupEventBusCommunication() {

        return Future.future(event -> {
            String savingsRepoAddress = MainConfig.getInstance()
                .getConfig()
                .getJsonObject("eventbus")
                .getString(CONFIG.EVENTBUS_ADDRESS);
            // Incoming requests to this verticle always come as messages.
            vertx.eventBus().consumer(savingsRepoAddress, this::processMessage);
            logger.info("Listening for request messages on address \"{}\".", savingsRepoAddress);
            event.complete();
        });

    }

    /** Process the message coming to this verticle through the event bus. */
    private void processMessage(Message<JsonObject> message) {

        String request = message.headers().get(IO.RQ_NAME);
        logger.debug("processMessage > '{}' request received.", request);
        if (request == null) {
            respondWithError(message, Errors.UNKNOWN_REQUEST, " Received: 'null'");
        } else {
            switch (request) {
                case IO.COMM_CHECK_RQ:
                    commCheck(message);
                    break;
                case IO.GET_SAVINGS_ACCOUNT_BY_OWNER_RQ:
                    getSavingsAccountByOwner(message);
                    break;
                default:
                    respondWithError(message, Errors.UNKNOWN_REQUEST, String.format("Received '%s'.", request));
            }
        }

    }

    /** Getting the existing (if any) Savings account by owner. */
    private void getSavingsAccountByOwner(Message<JsonObject> message) {

        String ownerId = message.headers().get(IO.OWNER_ID);
        SavingsAccount account = store.get(ownerId);
        if (account != null) {
            message.reply(new JsonObject().put(IO.GET_SAVINGS_ACCOUNT_BY_OWNER_RS, Json.encode(account)));
        } else {
            message.fail(IO.GET_SAVINGS_ACCOUNT_BY_OWNER_ERROR_RS, "Exchange rate is unknown.");
        }

    }

    /** Persist a Savings account to an existing user. */
    private void storeSavingsAccount(Message<JsonObject> message) {


    }

    // ____________________________________________________________________________________
    //

    public static final class CONFIG {

        public static final String EVENTBUS_ADDRESS = "savingsAccountsRepoAddress";

    }

    /**
     * The standard requests known by the <code>FxRepositoryVerticle</code>.<br/>
     * It receives and responds to them through the event bus.
     */
    public static final class IO {

        /** The name of the header that is expected to be included in the incoming messages */
        public static final String RQ_NAME = "rq-name";

        /** This is a communication check test performed at the startup. */
        public static final String COMM_CHECK_RQ = "rq-comm-check";

        /** "getSavingsAccountByOwner" request id as message header */
        public static final String GET_SAVINGS_ACCOUNT_BY_OWNER_RQ = "get-savings-account-by-owner-rq";

        /** "getSavingsAccountByOwner" request id as message header */
        public static final String GET_SAVINGS_ACCOUNT_BY_OWNER_RS = "get-savings-account-by-owner-rs";

        /** "getSavingsAccountByOwner" request id as message header */
        public static final int GET_SAVINGS_ACCOUNT_BY_OWNER_ERROR_RS = 0;

        /** Message header used in "getSavingsAccountByOwner" request. */
        public static final String OWNER_ID = "owner-id";

    }

}
