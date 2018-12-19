package io.github.robertograham.fortniteclienttwo.client;

import io.github.robertograham.fortniteclienttwo.resource.AccountResource;
import io.github.robertograham.fortniteclienttwo.resource.LeaderBoardResource;
import io.github.robertograham.fortniteclienttwo.resource.StatisticsResource;

import java.io.Closeable;

public interface Fortnite extends Closeable {

    AccountResource account();

    LeaderBoardResource leaderBoard();

    StatisticsResource statistics();
}
