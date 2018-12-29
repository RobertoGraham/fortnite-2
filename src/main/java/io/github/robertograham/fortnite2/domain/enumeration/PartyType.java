package io.github.robertograham.fortnite2.domain.enumeration;

/**
 * Type of party that can play a game of Fortnite
 */
public enum PartyType {

    SOLO("p2"), DUO("p10"), SQUAD("p9");

    private final String code;

    PartyType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
