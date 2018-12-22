package io.github.robertograham.fortniteclienttwo.domain.enumeration;

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
