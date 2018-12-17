# fortnite-client-two

### Instantiating a client

fortnite-client-two provides defaults for epicGamesLauncherToken and fortniteClientToken. To use these defaults,
instantiate a DefaultFortnite in this way:

```java
import io.github.robertograham.fortniteclienttwo.client.DefaultFortnite;
import io.github.robertograham.fortniteclienttwo.client.Fortnite;

public class Main {

    public static void main(String[] args) {
        Fortnite fortnite = DefaultFortnite.Builder.newInstance("demo@example.com", "demo")
                .build();
    }
}
```

Otherwise, you can provide your own epicGamesEmailAddress and/or fortniteClientToken by instantiating DefaultFortnite in
this way:

```java
import io.github.robertograham.fortniteclienttwo.client.DefaultFortnite;
import io.github.robertograham.fortniteclienttwo.client.Fortnite;

public class Main {

    public static void main(String[] args) {
        Fortnite fortnite = DefaultFortnite.Builder.newInstance("demo@example.com", "demo")
                .setEpicGamesLauncherToken("launcherToken")
                .setFortniteClientToken("clientToken")
                .build();
    }
}
```