
package org.devisions.labs.savings.vx.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import org.devisions.labs.savings.vx.models.SavingsAccount;
import org.devisions.labs.savings.vx.repos.SavingsAccountsRepo;
import org.devisions.labs.savings.vx.webapi.WebApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;


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

        logger.debug("storeSavingsAccountByOwner > Starting with ownerId '{}' and account '{}'", ownerId, account.toJson().encode());

        // Check BR-1: "The account can only be opened between 8 AM and 5 PM".
        LocalTime currentTime = LocalTime.now();
        if (!isWorkingTime(currentTime)) {
            resultHandler.handle(Future.failedFuture(
                new WebApiError(1, String.format(
                    "Sorry! Request cannot be processed outside of working hours (8 AM to 5 PM). Now the time is %s",
                    currentTime)).toJsonString()));
        }

        // Check BR-2: "the user can have only one savings account".
        if (hasUserAccount(ownerId)) {
            resultHandler.handle(Future.failedFuture(
                new WebApiError(2, String.format("Sorry! The user (id:'%s') already has a savings account.", ownerId)
                ).toJsonString()));
        }

        account.setOwnerId(ownerId);
        repo.saveAccountToOwner(ownerId, account);
        resultHandler.handle(Future.succeededFuture(account.toJson()));

    }

    /** Tell if we are in the working time (8 AM to 5 PM). */
    private boolean isWorkingTime(LocalTime currentTime) {
        int currentHour = currentTime.getHour();
        return 7 < currentHour && currentHour < 17;
    }

    /** Tell if the user has a savings account. */
    private boolean hasUserAccount(String ownerId) {
        return repo.getAccountByOwner(ownerId) != null;
    }

}
