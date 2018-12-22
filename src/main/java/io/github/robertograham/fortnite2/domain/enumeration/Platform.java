package io.github.robertograham.fortnite2.domain.enumeration;

public enum Platform {

    PC("pc"), PS4("ps4"), XB1("xb1");

    private final String code;

    Platform(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
