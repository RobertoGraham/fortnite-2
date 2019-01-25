[![Maven Central](https://img.shields.io/maven-central/v/io.github.robertograham/fortnite-2.svg?label=Maven%20Central&style=flat-square)](https://search.maven.org/search?q=g:%22io.github.robertograham%22%20AND%20a:%22fortnite-2%22)

# fortnite-2

A Java 11+ client for the APIs used by Epic Games Launcher and the Fortnite client

## Features

* Authentication with Epic's APIs is managed internally
* Fetching and filtering (by platform, party type, and time window) of an account's Battle Royale statistics
* Fetching of all-time win leader boards using different combinations of platform and party type
* Fetching of a single account via its username or many accounts via their IDs

## Installation

### Maven

```xml
<properties>
  ...
  <!-- Use the latest version whenever possible. -->
  <fortnite-2.version>2.0.0</fortnite-2.version>
  ...
</properties>

<dependencies>
  ...
  <dependency>
    <groupId>io.github.robertograham</groupId>
    <artifactId>fortnite-2</artifactId>
    <version>${fortnite-2.version}</version>
  </dependency>
  ...
</dependencies>
```

## Usage

### Instantiating a client

This is the simplest way to instantiate a client:

```java
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        final var fortnite = builder.build();
    }
}
```

If Epic Games ever deprecate this library's default launcher and client tokens, you may provide your own like this:

```java
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword")
            .setEpicGamesLauncherToken("launcherToken")
            .setFortniteClientToken("clientToken");
        final var fortnite = builder.build();
    }
}
```

### Cleaning up

When you no longer need your client instance, remember to terminate your Epic Games authentication session and release
the client's underlying resources with a call to `Fortnite.close()`. Usage examples further in this document will make 
this call implicitly using `try`-with-resources statements.

```java
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        final var fortnite = builder.build();
        fortnite.close();
    }
}
```

### Fetching an account using its username

```java
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            final var optionalAccount = fortnite.account()
                .findOneByDisplayName("RobertoGraham");
            // nothing printed if the response was empty
            optionalAccount.map(Account::accountId)
                .ifPresent(System.out::println);
            optionalAccount.map(Account::displayName)
                .ifPresent(System.out::println);
        } catch (final IOException exception) {
            // findOneByDisplayName unexpected response
        }
    }
}
```

### Fetching an account using the session's account ID

```java
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            final var optionalAccount = fortnite.account()
                .findOneBySessionAccountId();
            // nothing printed if the response was empty
            optionalAccount.map(Account::accountId)
                .ifPresent(System.out::println);
            optionalAccount.map(Account::displayName)
                .ifPresent(System.out::println);
        } catch (final IOException exception) {
            // findOneBySessionAccountId unexpected response
        }
    }
}
```

### Fetching many accounts using their IDs

```java
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;
import java.util.Collections;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            final var accountId1String = fortnite.account()
                .findOneByDisplayName("RobertoGraham")
                .map(Account::accountId)
                .orElse("");
            final var accountId2String = fortnite.account()
                .findOneByDisplayName("Ninja")
                .map(Account::accountId)
                .orElse("");
            // accountSet will be empty if the response was empty
            // OR if every account ID was invalid
            final var accountSet = fortnite.account()
                .findAllByAccountIds(accountId1String, accountId2String)
                .orElseGet(Collections::emptySet);
        } catch (final IOException exception) {
            // findOneByDisplayName unexpected response
            // OR findAllByAccountIds unexpected response
        }
    }
}
```

### Statistic filtering API

Most basic filtering - by time windows

```java
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            final var account = fortnite.account()
                .findOneByDisplayName("RobertoGraham")
                .orElseThrow();
            final var accountIdString = account.accountId();
            // if any null then we received an empty response
            final var fromAccountAllTimeFilterableStatistic = fortnite.statistic()
                .findAllByAccountForAllTime(account)
                .orElse(null);
            final var fromAccountIdAllTimeFilterableStatistic = fortnite.statistic()
                .findAllByAccountIdForAllTime(accountIdString)
                .orElse(null);
            final var fromAccountCurrentSeasonFilterableStatistic = fortnite.statistic()
                .findAllByAccountForCurrentSeason(account)
                .orElse(null);
            final var fromAccountIdCurrentSeasonFilterableStatistic = fortnite.statistic()
                .findAllByAccountIdForCurrentSeason(accountIdString)
                .orElse(null);
            final var fromSessionAccountIdAllTimeFilterableStatistic = fortnite.statistic()
                .findAllBySessionAccountIdForAllTime()
                .orElse(null);
            final var fromSessionAccountIdCurrentSeasonFilterableStatistic = fortnite.statistic()
                .findAllBySessionAccountIdForCurrentSeason()
                .orElse(null);
        } catch (final IOException exception) {
            // findOneByDisplayName unexpected response
            // OR findAllByAccountForAllTime unexpected response
            // OR findAllByAccountIdForAllTime unexpected response
            // OR findAllByAccountForCurrentSeason unexpected response
            // OR findAllByAccountIdForCurrentSeason unexpected response
            // OR findAllBySessionAccountIdForAllTime unexpected response
            // OR findAllBySessionAccountIdForCurrentSeason unexpected response
        }
    }
}
```

Filtering by platform and then party type

```java
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

import static io.github.robertograham.fortnite2.domain.enumeration.PartyType.SQUAD;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.PC;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            final var account = fortnite.account()
                .findOneByDisplayName("RobertoGraham")
                .orElseThrow();
            final var partyTypeFilterableStatistic = fortnite.statistic()
                .findAllByAccountForAllTime(account)
                .map(filterableStatistic -> filterableStatistic.byPlatform(PC))
                .orElseThrow();
            final var statistic = partyTypeFilterableStatistic.byPartyType(SQUAD);
        } catch (final IOException exception) {
            // findOneByDisplayName unexpected response
            // OR findAllByAccountForAllTime unexpected response
        }
    }
}
```

Filtering by party type and then platform

```java
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

import static io.github.robertograham.fortnite2.domain.enumeration.PartyType.SOLO;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.PS4;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            final var account = fortnite.account()
                .findOneByDisplayName("RobertoGraham")
                .orElseThrow();
            final var platformFilterableStatistic = fortnite.statistic()
                .findAllByAccountForAllTime(account)
                .map(filterableStatistic -> filterableStatistic.byPartyType(SOLO))
                .orElseThrow();
            final var statistic = platformFilterableStatistic.byPlatform(PS4);
        } catch (final IOException exception) {
            // findOneByDisplayName unexpected response
            // OR findAllByAccountForAllTime unexpected response
        }
    }
}
```

Inline platform and party type chained filter

```java
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

import static io.github.robertograham.fortnite2.domain.enumeration.PartyType.DUO;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.XB1;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            final var account = fortnite.account()
                .findOneByDisplayName("RobertoGraham")
                .orElseThrow();
            final var statistic = fortnite.statistic()
                .findAllByAccountForAllTime(account)
                .map((final var filterableStatistic) ->
                    filterableStatistic
                        .byPlatform(XB1)
                        .byPartyType(DUO)
                )
                .orElse(null);
        } catch (final IOException exception) {
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
import io.github.robertograham.fortnite2.domain.FilterableStatistic;
import io.github.robertograham.fortnite2.domain.PartyTypeFilterableStatistic;
import io.github.robertograham.fortnite2.domain.PlatformFilterableStatistic;
import io.github.robertograham.fortnite2.domain.Statistic;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

import static io.github.robertograham.fortnite2.domain.enumeration.PartyType.SOLO;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.PC;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.PS4;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            final var account = fortnite.account()
                .findOneByDisplayName("RobertoGraham")
                .orElseThrow();
            final var filterableStatisticOptional = fortnite.statistic()
                .findAllByAccountForAllTime(account);
            // prints 761 at time of writing
            filterableStatisticOptional.map(FilterableStatistic::kills)
                .ifPresent(System.out::println);
            // prints 5 at time of writing
            filterableStatisticOptional.map((final var filterableStatistic) -> filterableStatistic.byPlatform(PC))
                .map(PartyTypeFilterableStatistic::kills)
                .ifPresent(System.out::println);
            // prints 580 at time of writing
            filterableStatisticOptional.map((final var filterableStatistic) -> filterableStatistic.byPartyType(SOLO))
                .map(PlatformFilterableStatistic::kills)
                .ifPresent(System.out::println);
            // prints 575 at time of writing
            filterableStatisticOptional.map((final var filterableStatistic) ->
                filterableStatistic
                    .byPlatform(PS4)
                    .byPartyType(SOLO)
            )
                .map(Statistic::kills)
                .ifPresent(System.out::println);
        } catch (final IOException exception) {
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
import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.domain.LeaderBoardEntry;
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.robertograham.fortnite2.domain.enumeration.PartyType.SOLO;
import static io.github.robertograham.fortnite2.domain.enumeration.Platform.PC;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            final var leaderBoardEntryList = fortnite.leaderBoard()
                .findHighestWinnersByPlatformAndByPartyTypeForCurrentSeason(PC, SOLO, 5)
                .orElseThrow();
            final var accountIdStringToAccountMap = fortnite.account()
                .findAllByAccountIds(leaderBoardEntryList.stream()
                    .map(LeaderBoardEntry::accountId)
                    .map((final var accountIdString) -> accountIdString.replaceAll("-", ""))
                    .toArray(String[]::new))
                .map((final var accountSet) ->
                    accountSet.stream()
                        .collect(Collectors.toMap(Account::accountId, Function.identity()))
                )
                .orElseThrow();
            leaderBoardEntryList.stream()
                .map((final var leaderBoardEntry) ->
                    String.format(
                        "Name: %s, Wins: %d",
                        accountIdStringToAccountMap.get(leaderBoardEntry.accountId().replaceAll("-", "")).displayName(),
                        leaderBoardEntry.value()
                    )
                )
                .forEach(System.out::println);
        } catch (final IOException exception) {
            // findHighestWinnersByPlatformAndByPartyTypeForCurrentSeason unexpected response
            // OR findAllByAccountIds unexpected response
        }
    }
}
```

### Friend API

Fetch both accepted and pending friend requests for the authenticated user

```java
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            fortnite.friend()
                .findAllRequestsBySessionAccountId()
                .ifPresent(System.out::println);
        } catch (final IOException exception) {
            // findAllRequestsBySessionAccountId unexpected response
        }
    }
}
```

Fetch only accepted friend requests for the authenticated user

```java
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            fortnite.friend()
                .findAllNonPendingRequestsBySessionAccountId()
                .ifPresent(System.out::println);
        } catch (final IOException exception) {
            // findAllNonPendingRequestsBySessionAccountId unexpected response
        }
    }
}
```

Delete a friend or a friend request using an account or an account ID

```java
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            final var account = fortnite.account()
                .findOneByDisplayName("RobertoGraham")
                .orElseThrow();
            fortnite.friend()
                .deleteOneByAccount(account);
            fortnite.friend()
                .deleteOneByAccountId(account.accountId());
        } catch (final IOException exception) {
            // findOneByDisplayName unexpected response
            // OR deleteOneByAccount unexpected response
            // OR deleteOneByAccountId unexpected response
        }
    }
}
```

Add a friend or accept a friend request using an account or an account ID

```java
import io.github.robertograham.fortnite2.implementation.DefaultFortnite.Builder;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) {
        final var builder = Builder.newInstance("epicGamesEmailAddress", "epicGamesPassword");
        try (final var fortnite = builder.build()) {
            final var account = fortnite.account()
                .findOneByDisplayName("RobertoGraham")
                .orElseThrow();
            fortnite.friend()
                .addOneByAccount(account);
            fortnite.friend()
                .addOneByAccountId(account.accountId());
        } catch (final IOException exception) {
            // findOneByDisplayName unexpected response
            // OR addOneByAccount unexpected response
            // OR addOneByAccountId unexpected response
        }
    }
}
```
