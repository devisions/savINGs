package org.devisions.labs.savings.rs.services;

import org.devisions.labs.savings.rs.model.User;
import org.devisions.labs.savings.rs.repos.UsersRepo;
import reactor.core.publisher.Mono;


/**
 * Standard mplementation of the users service.
 */
public class UsersSvcImpl implements UsersSvc {

    private UsersRepo usersRepo;

    public UsersSvcImpl(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return Mono.just(usersRepo.getByUsername(username));
    }

}
