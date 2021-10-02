package com.newwares.hypixelstats.utils;

public enum ChatColour {
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GREY('7'),
    DARK_GREY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),
    MAGIC('k'),
    BOLD('l'),
    STRIKETHROUGH('m'),
    UNDERLINE('n'),
    ITALIC('o'),
    RESET('r');

    private final char colourCode;

    ChatColour(char colourCode) {
        this.colourCode = colourCode;
    }

    @Override
    public String toString() {
        char colourChar = '§';
        return String.valueOf(colourChar) + this.colourCode;
    }
}
