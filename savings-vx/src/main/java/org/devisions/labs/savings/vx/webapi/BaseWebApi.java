package org.devisions.labs.savings.vx.webapi;

import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import org.devisions.labs.savings.vx.commons.Errors;


/**
 * This base interface provides some common default methods that any verticle can use.
 *
 * @author devisions
 */
public interface BaseWebApi {


  /** Respond with HTTP RC 500 (Internal Server Error) and JSON content. */
  default void respondErrorAsJson(HttpServerResponse response, String error) {

    response
      .setStatusCode(500)
      .putHeader("Content-Type", "application/json")
      .end(new JsonObject().put("error", error).encode());
  }


  /** Respond with HTTP RC 404 (Not Found) and JSON message. */
  default void respondNotFoundWithMessageAsJson(HttpServerResponse response, String message) {

    response
      .setStatusCode(404)
      .putHeader("Content-Type", "application/json")
      .end(new JsonObject().put("message", message).encode());
  }


  /** Respond with HTTP RC 200 (OK) and JSON content. */
  default void respondOKWithContentAsJson(HttpServerResponse response, String json) {

    response
      .setStatusCode(200)
      .putHeader("Content-Type", "application/json")
      .end(json);
  }


  /** Respond with the provided HTTP RC and JSON content. */
  default void respondRCWithContentAsJson(HttpServerResponse response, int httpReturnCode, String json) {

    response
      .setStatusCode(httpReturnCode)
      .putHeader("Content-Type", "application/json")
      .end(json);
  }

  /** Respond back with provided content. */
  default void respond(HttpServerResponse serverResponse, String json) {

    serverResponse
      .putHeader("content-type", "application/json")
      .end(json);

  }

  /** Respond back with provided content and a specific status code. */
  default void respond(HttpServerResponse serverResponse, String json, int statusCode) {

    serverResponse
      .putHeader("content-type", "application/json")
      .setStatusCode(statusCode)
      .end(json);

  }

  /** Respond back with provided content and a specific status code. */
  default void respondError(HttpServerResponse serverResponse, String errorMessage, int statusCode) {

    serverResponse
      .putHeader("content-type", "application/json")
      .setStatusCode(statusCode)
      .end(new JsonObject().put("error", errorMessage).encode());

  }


}
