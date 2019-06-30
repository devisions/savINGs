package org.devisions.labs.savings.vx.webapi;

import io.vertx.core.json.JsonObject;


/**
 * Standard error codes dictionary.
 *
 * @author devisions
 */
public class WebApiError {

    private final int code;

    private final String message;

    public WebApiError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String toJsonString() {
        return new JsonObject().put("error", new JsonObject().put("code", code).put("message", message)).encodePrettily();
    }

}
