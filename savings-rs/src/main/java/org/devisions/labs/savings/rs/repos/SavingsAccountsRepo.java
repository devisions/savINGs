package org.devisions.labs.savings.rs.repos;

import org.devisions.labs.savings.rs.model.SavingsAccount;
import org.devisions.labs.savings.rs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * The users repository.
 */
@Repository
public interface SavingsAccountsRepo extends JpaRepository<SavingsAccount, Long> {

    SavingsAccount getById(Long id);

    SavingsAccount getByOwner(User owner);

}
