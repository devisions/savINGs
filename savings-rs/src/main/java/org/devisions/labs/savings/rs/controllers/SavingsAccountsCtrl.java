package org.devisions.labs.savings.rs.controllers;


import org.devisions.labs.savings.rs.model.SavingsAccount;
import org.devisions.labs.savings.rs.repos.SavingsAccountsRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/accounts/savings")
public class SavingsAccountsCtrl {

    private SavingsAccountsRepo savingsAccountsRepo;

    public SavingsAccountsCtrl(SavingsAccountsRepo savingsAccountsRepo) {
        this.savingsAccountsRepo = savingsAccountsRepo;
    }

    @GetMapping("/{id}")
    public Mono<SavingsAccount> getById(@PathVariable Long id) {
        return Mono.fromSupplier( () -> savingsAccountsRepo.getById(id));
    }

}
