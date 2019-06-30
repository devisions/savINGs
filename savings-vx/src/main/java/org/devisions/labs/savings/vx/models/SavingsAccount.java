package org.devisions.labs.savings.vx.models;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.UUID;


/**
 * Savings Account model.
 *
 * @author devisions
 */
@DataObject
public class SavingsAccount {

    private String id;

    private String name;

    private String description;

    private String ownerId;

    public SavingsAccount(String name, String description, String ownerId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }

    public SavingsAccount(JsonObject json) {
        this.name = json.getString("name");
        this.description = json.getString("description");
        this.ownerId = json.getString("ownerId");
    }

    public SavingsAccount(JsonObject json, String id) {
        this.name = json.getString("name");
        this.description = json.getString("description");
        this.ownerId = json.getString("ownerId");
        this.id = UUID.randomUUID().toString();
    }

    /**
     * This method is used before saving it to the repository.<br/>
     * There is no need to generate and assign an id if the all BRs are not satisfied.
     */
    public SavingsAccount generateId() {
        this.id = UUID.randomUUID().toString();
        return this;
    }

    // __________ getters and (fluent) setters __________


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SavingsAccount setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SavingsAccount setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public SavingsAccount setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    // __________ utilities __________

    /**
     * Convert this instance to JsonObject.<br/>
     * This utility method is used by verticles as they talk (receive & respond) JSON.
     */
    public JsonObject toJson() {
        return new JsonObject().put("id", id).put("name", name).put("description", description).put("ownerId", ownerId);
    }

    /**
     * Create an SavingsAccount instance from the provided JsonObject.<br/>
     * This utility method is used by verticles as they talk (receive & respond) JSON.
     */
    public static SavingsAccount fromJson(JsonObject json) {
        return new SavingsAccount(json);
    }

}
