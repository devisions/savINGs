package org.devisions.labs.savings.vx.services;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The verticle that is setting up the savings accounts service at startup.
 *
 * @author devisions
 */
public class SavingsAccountsServiceVerticle extends AbstractVerticle implements BaseSvcVerticle {

    private static final Logger logger = LoggerFactory.getLogger(SavingsAccountsServiceVerticle.class);

    @Override
    public void start(Future<Void> startFuture) {

        SavingsAccountsService.create(ready -> {
            if (ready.succeeded()) {
                SavingsAccountsService sas = ready.result();
                new ServiceBinder(vertx)
                    .setAddress(SavingsAccountsServiceConfig.EB_ADDRESS)
                    .register(SavingsAccountsService.class, sas);
                logger.debug("A SavingsAccountsService implementation is registered to event bus.");
                startFuture.complete();
            } else {
                startFuture.fail(ready.cause());
            }
        });

    }

}
