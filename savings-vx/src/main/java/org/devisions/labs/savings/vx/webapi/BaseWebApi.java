package org.devisions.labs.savings.vx.webapi;

import io.vertx.core.http.HttpServerResponse;


/**
 * This base interface provides some common default methods that any verticle can use.
 *
 * @author devisions
 */
public interface BaseWebApi {

    /** Respond with the provided HTTP RC, JSON declared as content-type, and provided content. */
    default void respondCustomRCWithJsonContent(HttpServerResponse response, int httpReturnCode, String json) {

        response.setStatusCode(httpReturnCode).putHeader("Content-Type", "application/json").end(json);

    }

    /** Respond with HTTP RC 500 (Internal Server Error) and JSON content. */
    default void respondInternalServerErrorWithJsonContent(HttpServerResponse response, String json) {

        respondCustomRCWithJsonContent(response, 500, json);

    }

    /** Respond with HTTP RC 404 (Not Found) without a content. */
    default void respondNotFoundWithoutContent(HttpServerResponse response) {

        response.setStatusCode(404).putHeader("Content-Type", "application/json").end();

    }

    /** Respond with HTTP RC 401 (Forbidden) and JSON content. */
    default void respondForbiddenWithJsonContent(HttpServerResponse response, String json) {

        response.setStatusCode(401).putHeader("Content-Type", "application/json").end(json);

    }

    /** Respond with HTTP RC 201 (Created) and JSON content. */
    default void respondCreatedWithJsonContent(HttpServerResponse response, String json) {

        response.setStatusCode(201).putHeader("Content-Type", "application/json").end(json);

    }

    /** Respond with HTTP RC 200 (OK) and JSON content. */
    default void respondOKWithJsonContent(HttpServerResponse response, String json) {

        response.setStatusCode(200).putHeader("Content-Type", "application/json").end(json);

    }

}
