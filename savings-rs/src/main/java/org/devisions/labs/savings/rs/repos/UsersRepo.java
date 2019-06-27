package org.devisions.labs.savings.rs.repos;

import org.devisions.labs.savings.rs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/** Users repository. */
@Repository
public interface UsersRepo extends JpaRepository<User, Long> {

    User getByUsername(String username);

}
