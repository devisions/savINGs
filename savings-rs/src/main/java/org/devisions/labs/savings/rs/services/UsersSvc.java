package org.devisions.labs.savings.rs.services;


import org.devisions.labs.savings.rs.model.User;
import reactor.core.publisher.Mono;


/**
 * The spec for any users service implementation.
 */
public interface UsersSvc {

    Mono<User> findByUsername(String username);

}
