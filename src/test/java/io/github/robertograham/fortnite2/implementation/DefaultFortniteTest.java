package io.github.robertograham.fortnite2.implementation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultFortniteTest {

    @Test
    @DisplayName("DefaultFortnite.Builder throws NPEs")
    void defaultFortniteBuilderThrowsNPE() {
        assertThrows(
                NullPointerException.class,
                () -> DefaultFortnite.Builder.newInstance(null, ""),
                "epicGamesEmailAddress cannot be null"
        );
        assertThrows(
                NullPointerException.class,
                () -> DefaultFortnite.Builder.newInstance("", null),
                "epicGamesPassword cannot be null"
        );
        assertThrows(
                NullPointerException.class,
                () ->
                        DefaultFortnite.Builder.newInstance("", "")
                                .setEpicGamesLauncherToken(null),
                "epicGamesLauncherToken cannot be null"
        );
        assertThrows(
                NullPointerException.class,
                () ->
                        DefaultFortnite.Builder.newInstance("", "")
                                .setFortniteClientToken(null),
                "fortniteClientToken cannot be null"
        );
    }
}
