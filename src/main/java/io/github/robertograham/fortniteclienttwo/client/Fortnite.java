package io.github.robertograham.fortniteclienttwo.client;

import io.github.robertograham.fortniteclienttwo.resource.Account;
import io.github.robertograham.fortniteclienttwo.resource.LeaderBoard;
import io.github.robertograham.fortniteclienttwo.resource.Statistics;

import java.io.Closeable;

public interface Fortnite extends Closeable {

    Account account();

    LeaderBoard leaderBoard();

    Statistics statistics();
}
