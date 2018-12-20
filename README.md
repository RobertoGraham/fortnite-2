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

### Releasing resources

When you are finished with your client instance you may release its underlying HttpClient's resources like this:

```java
import io.github.robertograham.fortniteclienttwo.client.Fortnite;
import io.github.robertograham.fortniteclienttwo.implementation.DefaultFortnite.Builder;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        Fortnite fortnite = builder.build();
        try {
            fortnite.close();
        } catch (IOException exception) {
            // problem occurred when releasing the underlying HttpClient's resources
        }
    }
}
```

### Fetching a single account using its display name

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
            Optional<Account> accountOptional = fortnite.account()
                    .accountFromDisplayName("RobertoGraham");
            // accountId will be blank if the response was empty
            String accountId = accountOptional.map(Account::accountId)
                    .orElse("");
            // displayName will be blank if the response was empty
            String displayName = accountOptional.map(Account::displayName)
                    .orElse("");
        } catch (IOException exception) {
            // problem fetching the account 
            // OR releasing resources
        }
    }
}
```

### Fetching multiple accounts using their account IDs

```java
import io.github.robertograham.fortniteclienttwo.client.Fortnite;
import io.github.robertograham.fortniteclienttwo.domain.Account;
import io.github.robertograham.fortniteclienttwo.implementation.DefaultFortnite.Builder;

import java.io.IOException;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (Fortnite fortnite = builder.build()) {
            String accountId1 = fortnite.account()
                    .accountFromDisplayName("RobertoGraham")
                    .map(Account::accountId)
                    .orElse("");
            String accountId2 = fortnite.account()
                    .accountFromDisplayName("Ninja")
                    .map(Account::accountId)
                    .orElse("");
            // accounts will be empty if the response was empty 
            // OR if every account ID was invalid
            Set<Account> accounts = fortnite.account()
                    .accountsFromAccountIds(accountId1, accountId2)
                    .orElse(new HashSet<>());
            System.out.println(accounts);
        } catch (IOException exception) {
            // problem fetching the individual accounts
            // OR fetching all the accounts
            // OR releasing resources
        }
    }
}
```

