
package org.devisions.labs.savings.vx.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.devisions.labs.savings.vx.models.SavingsAccount;
import org.devisions.labs.savings.vx.repos.SavingsAccountsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of savings accounts service.
 *
 * @author devisions
 */
public class SavingsAccountsServiceImpl implements SavingsAccountsService {

    private SavingsAccountsRepo repo;

    private static final Logger logger = LoggerFactory.getLogger(SavingsAccountsServiceImpl.class);

    public SavingsAccountsServiceImpl(Handler<AsyncResult<SavingsAccountsService>> readyHandler) {
        this.repo = new SavingsAccountsRepo();
        readyHandler.handle(Future.succeededFuture(this));
    }

    @Override
    public void commCheck(Handler<AsyncResult<JsonObject>> resultHandler) {
        resultHandler.handle(Future.succeededFuture());
    }

    @Override
    public void getSavingsAccountByOwner(String ownerId, Handler<AsyncResult<JsonObject>> resultHandler) {

        SavingsAccount account = repo.getAccountByOwner(ownerId);
        if (account != null) {
            resultHandler.handle(Future.succeededFuture(account.toJson()));
        } else {
            resultHandler.handle(Future.succeededFuture());
        }

    }

    @Override
    public void storeSavingsAccountByOwner(String ownerId, SavingsAccount account, Handler<AsyncResult<JsonObject>> resultHandler) {

        SavingsAccount existingAccount = repo.getAccountByOwner(ownerId);
        if (account != null) {
            resultHandler.handle(Future.succeededFuture(account.toJson()));
        } else {
            resultHandler.handle(Future.failedFuture(
                String.format("Owner (id:%s) already has a savings account (id:%s).", ownerId, existingAccount.getOwnerId())));
        }

    }

}

