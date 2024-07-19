package com.thelabirinto.graphics;

/**
 * Classe di tipo Enum, che permette l'associazione tra il valore di difficoltà
 * scelto dal player e il valore numerico, rendendo scalabile per modifiche future
 */
public enum Difficulty {
    EASY("Easy", 5),
    MEDIUM("Medium", 2.5),
    HARD("Hard", 2.3);

    private final String name;
    private final double value;

    Difficulty(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public static Difficulty fromName(String name) {
        for (Difficulty difficulty : values()) {
            if (difficulty.getName().equalsIgnoreCase(name)) {
                return difficulty;
            }
        }
        throw new IllegalArgumentException("Invalid difficulty name: " + name);
    }
}
