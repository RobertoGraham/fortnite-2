package io.github.robertograham.fortniteclienttwo.resource;

import io.github.robertograham.fortniteclienttwo.domain.Account;

import java.io.IOException;
import java.util.Optional;

public interface AccountResource {

    Optional<Account> accountFromDisplayName(String displayName) throws IOException;
}
