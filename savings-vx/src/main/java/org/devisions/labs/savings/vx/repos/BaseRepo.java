package org.devisions.labs.savings.vx.repos;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.devisions.labs.savings.vx.commons.Errors;


/**
 * This base interface provides common methods used by all repos.
 *
 * @author devisions
 */
public interface BaseRepo {

  /** This is a standard communication check handler. */
  default void commCheck(Message<JsonObject> message) {
    message.reply("ok");
  }

  /** Generic error responding approach. */
  default void respondWithError(Message<JsonObject> message, Errors error, String additionalMessage) {

    String errorMessage = additionalMessage == null ?
      error.message : error.message + additionalMessage;
    message.fail(error.code, errorMessage);

  }

}
