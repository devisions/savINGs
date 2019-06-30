package org.devisions.labs.savings.vx.models;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;


/**
 * Savings account domain model.
 *
 * @author devisions
 */
@DataObject
public class SavingsAccount {

    private String name;

    private String description;

    private String ownerId;

    public SavingsAccount(String name, String description, String ownerId) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }

    public SavingsAccount(JsonObject json) {
        this.name = json.getString("name");
        this.description = json.getString("description");
        this.ownerId = json.getString("ownerId");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /** Utility method used by verticles as they talk (respond) JSON language. */
    public JsonObject toJson() {
        return new JsonObject().put("name", name).put("description", description).put("ownerId", ownerId);
    }

}
