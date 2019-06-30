package org.devisions.labs.savings.vx.services;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.devisions.labs.savings.vx.models.SavingsAccount;


/**
 * The spec of savings accounts service.
 *
 * @author devisions
 */
@ProxyGen   // for generating the service proxy
@VertxGen   // for generating the clients
public interface SavingsAccountsService {

    // __________ the factory methods __________

    @GenIgnore
    static SavingsAccountsService create(Handler<AsyncResult<SavingsAccountsService>> readyHandler) {
        return new SavingsAccountsServiceImpl(readyHandler);
    }

    @GenIgnore
    static SavingsAccountsService createProxy(Vertx vertx, String address) {
        return new SavingsAccountsServiceVertxEBProxy(vertx, address);
    }

    /** Config part of the class. */
    @GenIgnore
    class Config {

        /** event bus address of this verticle */
        public static final String EB_ADDRESS = "savingsAccountsServiceAddress";

    }

    // __________ features methods __________

    void commCheck(Handler<AsyncResult<JsonObject>> resultHandler);

    void getSavingsAccountByOwner(String ownerId, Handler<AsyncResult<JsonObject>> resultHandler);

    void storeSavingsAccountByOwner(String ownerId, SavingsAccount account, Handler<AsyncResult<JsonObject>> resultHandler);

}
