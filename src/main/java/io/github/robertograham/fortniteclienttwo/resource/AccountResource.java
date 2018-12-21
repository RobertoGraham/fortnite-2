package io.github.robertograham.fortniteclienttwo.resource;

import io.github.robertograham.fortniteclienttwo.domain.Account;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public interface AccountResource {

    Optional<Account> findOneByDisplayName(String displayName) throws IOException;

    Optional<Set<Account>> findAllByAccountIds(String... accountIds) throws IOException;
}
