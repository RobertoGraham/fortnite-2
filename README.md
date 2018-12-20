# fortnite-client-two

### Instantiating a client

This is the simplest way to instantiate a client:

```java
import io.github.robertograham.fortniteclienttwo.client.Fortnite;
import io.github.robertograham.fortniteclienttwo.implementation.DefaultFortnite.Builder;

import java.io.IOException;

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

import java.io.IOException;

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

### Fetching an account

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
            // accountId will be null if the response was empty OR if the account has a null id
            String accountId = accountOptional.map(Account::id)
                    .orElse(null);
            // displayName will be null if the response was empty OR if the account has a null displayName
            String displayName = accountOptional.map(Account::displayName)
                    .orElse(null);
        } catch (IOException exception) {
            // problem fetching the account OR releasing resources
        }
    }
}
```

