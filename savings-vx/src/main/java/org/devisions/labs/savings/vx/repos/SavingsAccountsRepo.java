package org.devisions.labs.savings.vx.repos;


import org.devisions.labs.savings.vx.models.SavingsAccount;

import java.util.HashMap;
import java.util.Map;


/**
 * The savings accounts repository.<br/>
 * It is a simple store, as it should hide the persistence mechanism details, and it should not have any business logic.
 *
 * @author devisions
 */
public class SavingsAccountsRepo {

    /** a minimal store that contains one SavingsAccount per ownerId */
    private Map<String, SavingsAccount> store;

    public SavingsAccountsRepo() {
        this.store = new HashMap<>();
    }

    public SavingsAccount getAccountByOwner(String ownerId) {
        return store.get(ownerId);
    }

    public void saveAccountToOwner(String ownerId, SavingsAccount account) {
        store.put(ownerId, account);
    }

}
