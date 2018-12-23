# fortnite-2

[![Maven Central](https://img.shields.io/maven-central/v/io.github.robertograham/fortnite-2.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.robertograham%22%20AND%20a:%22fortnite-2%22)

### Instantiating a client

This is the simplest way to instantiate a client:

```java
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        Fortnite fortnite = builder.build();
    }
}
```

If Epic Games ever deprecate this library's default launcher and client tokens, you may provide newer ones like this:

```java
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

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
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

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
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

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
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

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

### Statistic filtering API

Most basic filtering - by time windows

```java
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.domain.FilterableStatistic;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (Fortnite fortnite = builder.build()) {
            Account account = fortnite.account()
                    .findOneByDisplayName("RobertoGraham")
                    .orElseThrow(IllegalStateException::new);
            String accountId = account.accountId();
            // if any null then we received an empty response
            FilterableStatistic fromAccountAllTime = fortnite.statistic()
                    .findAllByAccountForAllTime(account)
                    .orElse(null);
            FilterableStatistic fromAccountIdAllTime = fortnite.statistic()
                    .findAllByAccountIdForAllTime(accountId)
                    .orElse(null);
            FilterableStatistic fromAccountCurrentSeason = fortnite.statistic()
                    .findAllByAccountForCurrentSeason(account)
                    .orElse(null);
            FilterableStatistic fromAccountIdCurrentSeason = fortnite.statistic()
                    .findAllByAccountIdForCurrentSeason(accountId)
                    .orElse(null);
        } catch (IOException exception) {
            // findOneByDisplayName unexpected response
            // OR findAllByAccountForAllTime unexpected response
            // OR findAllByAccountIdForAllTime unexpected response
            // OR findAllByAccountForCurrentSeason unexpected response
            // OR findAllByAccountIdForCurrentSeason unexpected response
        }
    }
}
```

Filtering by platform, then party type

```java
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.domain.PartyTypeFilterableStatistic;
import io.github.robertograham.fortnite2.domain.Statistic;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

import static io.github.robertograham.fortnite2.domain.enumeration.PartyType.SQUAD;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.PC;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (Fortnite fortnite = builder.build()) {
            Account account = fortnite.account()
                    .findOneByDisplayName("RobertoGraham")
                    .orElseThrow(IllegalStateException::new);
            PartyTypeFilterableStatistic onePlatformPartyFilterable = fortnite.statistic()
                    .findAllByAccountForAllTime(account)
                    .map(filterableStatistic -> filterableStatistic.byPlatform(PC))
                    .orElseThrow(IllegalStateException::new);
            Statistic onePlatformOnePartyType = onePlatformPartyFilterable.byPartyType(SQUAD);
        } catch (IOException exception) {
            // findOneByDisplayName unexpected response
            // OR findAllByAccountForAllTime unexpected response
        }
    }
}
```

Filtering by party type, then platform

```java
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.domain.PlatformFilterableStatistic;
import io.github.robertograham.fortnite2.domain.Statistic;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

import static io.github.robertograham.fortnite2.domain.enumeration.PartyType.SOLO;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.PS4;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (Fortnite fortnite = builder.build()) {
            Account account = fortnite.account()
                    .findOneByDisplayName("RobertoGraham")
                    .orElseThrow(IllegalStateException::new);
            PlatformFilterableStatistic onePartyTypePlatformFilterable = fortnite.statistic()
                    .findAllByAccountForAllTime(account)
                    .map(filterableStatistic -> filterableStatistic.byPartyType(SOLO))
                    .orElseThrow(IllegalStateException::new);
            Statistic onePartyTypeOnePlatform = onePartyTypePlatformFilterable.byPlatform(PS4);
        } catch (IOException exception) {
            // findOneByDisplayName unexpected response
            // OR findAllByAccountForAllTime unexpected response
        }
    }
}
```

Inline platform and party type chained filter

```java
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.domain.Statistic;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

import static io.github.robertograham.fortnite2.domain.enumeration.PartyType.DUO;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.XB1;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (Fortnite fortnite = builder.build()) {
            Account account = fortnite.account()
                    .findOneByDisplayName("RobertoGraham")
                    .orElseThrow(IllegalStateException::new);
            Statistic statistic = fortnite.statistic()
                    .findAllByAccountForAllTime(account)
                    .map(filterableStatistic ->
                            filterableStatistic
                                    .byPlatform(XB1)
                                    .byPartyType(DUO)
                    )
                    .orElse(null);
        } catch (IOException exception) {
            // findOneByDisplayName unexpected response
            // OR findAllByAccountForAllTime unexpected response
        }
    }
}
```

`FilterableStatistic`, `PartyTypeFilterableStatistic`, and `PlatformFilterableStatistic` all extend `Statistic`. This 
means that you can make calls like `Statistic.kills()`, `Statistic.wins()`, etc. at each filtering stage to get narrower 
and narrower scoped values

```java
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.domain.*;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;
import java.util.Optional;

import static io.github.robertograham.fortnite2.domain.enumeration.PartyType.SOLO;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.PC;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.PS4;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (Fortnite fortnite = builder.build()) {
            Account account = fortnite.account()
                    .findOneByDisplayName("RobertoGraham")
                    .orElseThrow(IllegalStateException::new);
            Optional<FilterableStatistic> filterableStatistic = fortnite.statistic()
                    .findAllByAccountForAllTime(account);
            // prints 761 at time of writing
            filterableStatistic.map(FilterableStatistic::kills)
                    .ifPresent(System.out::println);
            // prints 5 at time of writing
            filterableStatistic.map(filterable -> filterable.byPlatform(PC))
                    .map(PartyTypeFilterableStatistic::kills)
                    .ifPresent(System.out::println);
            // prints 580 at time of writing
            filterableStatistic.map(filterable -> filterable.byPartyType(SOLO))
                    .map(PlatformFilterableStatistic::kills)
                    .ifPresent(System.out::println);
            // prints 575 at time of writing
            filterableStatistic.map(filterable ->
                    filterable
                            .byPlatform(PS4)
                            .byPartyType(SOLO)
            )
                    .map(Statistic::kills)
                    .ifPresent(System.out::println);
        } catch (IOException exception) {
            // findOneByDisplayName unexpected response
            // OR findAllByAccountForAllTime unexpected response
        }
    }
}
```

### Leader board API

Fetch the current season's wins leader board and print the accounts' names and wins. The example prints the following at 
the time of writing:

```text
Name: JohnPitterTV, Wins: 350
Name: TTV SwitchUMG, Wins: 251
Name: Twitch TheBigOCE, Wins: 201
Name: WE_桃子, Wins: 197
Name: BlossoM Tsunami, Wins: 182
```

```java
import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.domain.LeaderBoardEntry;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.robertograham.fortnite2.domain.enumeration.PartyType.SOLO;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.PC;

public class Main {

    public static void main(String[] args) {
        Builder builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (Fortnite fortnite = builder.build()) {
            List<LeaderBoardEntry> entries = fortnite.leaderBoard()
                    .findHighestWinnersByPlatformAndByPartyTypeForCurrentSeason(PC, SOLO, 5)
                    .orElseThrow(IllegalStateException::new);
            Map<String, Account> accountIdToAccountMap = fortnite.account()
                    .findAllByAccountIds(
                            entries.stream()
                                    .map(LeaderBoardEntry::accountId)
                                    .map(accountId -> accountId.replaceAll("-", ""))
                                    .toArray(String[]::new)
                    )
                    .map(accounts ->
                            accounts.stream()
                                    .collect(Collectors.toMap(Account::accountId, Function.identity()))
                    )
                    .orElseThrow(IllegalStateException::new);
            entries.stream()
                    .map(leaderBoardEntry ->
                            String.format(
                                    "Name: %s, Wins: %d",
                                    accountIdToAccountMap.get(leaderBoardEntry.accountId().replaceAll("-", "")).displayName(),
                                    leaderBoardEntry.value()
                            )
                    )
                    .forEach(System.out::println);
        } catch (IOException exception) {
            exception.printStackTrace();
            // findHighestWinnersByPlatformAndByPartyTypeForCurrentSeason unexpected response
            // OR findAllByAccountIds unexpected response
        }
    }
}
```
