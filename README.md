# fortnite-client-two

### Instantiating a client

This is the simplest way to instantiate a client:

```java
import io.github.robertograham.fortniteclienttwo.client.Fortnite;
import io.github.robertograham.fortniteclienttwo.implementation.DefaultFortnite.Builder;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        Fortnite fortnite = builder.build();
    }
}
```

If Epic Games ever deprecate this library's default launcher and client tokens, you may provide newer ones like this:

```java
import io.github.robertograham.fortniteclienttwo.client.Fortnite;
import io.github.robertograham.fortniteclienttwo.implementation.DefaultFortnite.Builder;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword")
                .setEpicGamesLauncherToken("launcherToken")
                .setFortniteClientToken("clientToken");
        Fortnite fortnite = builder.build();
    }
}
```

### Cleaning up

When you no longer need your client instance, remember to terminate your Epic Games authentication session and release
the client's underlying resources with a call to `Fortnite.close()`. Usage examples further in this document will make 
this call implicitly using `try`-with-resources statements.

```java
import io.github.robertograham.fortniteclienttwo.client.Fortnite;
import io.github.robertograham.fortniteclienttwo.implementation.DefaultFortnite.Builder;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        Fortnite fortnite = builder.build();
        fortnite.close();
    }
}
```

### Getting an account using its display name

```java
import io.github.robertograham.fortniteclienttwo.client.Fortnite;
import io.github.robertograham.fortniteclienttwo.domain.Account;
import io.github.robertograham.fortniteclienttwo.implementation.DefaultFortnite.Builder;

import java.io.IOException;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (Fortnite fortnite = builder.build()) {
            Optional<Account> account = fortnite.account()
                    .findOneByDisplayName("RobertoGraham");
            // nothing printed if the response was empty
            account.map(Account::accountId)
                    .ifPresent(System.out::println);
            account.map(Account::displayName)
                    .ifPresent(System.out::println);
        } catch (IOException exception) {
            // findOneByDisplayName unexpected response
        }
    }
}
```

### Getting multiple accounts using their account IDs

```java
import io.github.robertograham.fortniteclienttwo.client.Fortnite;
import io.github.robertograham.fortniteclienttwo.domain.Account;
import io.github.robertograham.fortniteclienttwo.implementation.DefaultFortnite.Builder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (Fortnite fortnite = builder.build()) {
            String accountId1 = fortnite.account()
                    .findOneByDisplayName("RobertoGraham")
                    .map(Account::accountId)
                    .orElse("");
            String accountId2 = fortnite.account()
                    .findOneByDisplayName("Ninja")
                    .map(Account::accountId)
                    .orElse("");
            // accounts will be empty if the response was empty
            // OR if every account ID was invalid
            Set<Account> accounts = fortnite.account()
                    .findAllByAccountIds(accountId1, accountId2)
                    .orElseGet(HashSet::new);
        } catch (IOException exception) {
            // findOneByDisplayName unexpected response
            // OR findAllByAccountIds unexpected response
        }
    }
}
```

