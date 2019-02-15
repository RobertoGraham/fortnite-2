package io.github.robertograham.fortnite2.domain.enumeration;

/**
 * Type of input that can be used to play a game of Fortnite
 */
public enum InputType {

    KEYBOARD_AND_MOUSE("keyboardmouse"), GAMEPAD("gamepad"), TOUCH("touch");

    private final String code;

    InputType(final String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
