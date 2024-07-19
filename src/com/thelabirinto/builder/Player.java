package com.thelabirinto.builder;

/**
 * Classe che rappresenta l'entity della table presente nel database
 */
public class Player{
    private String name;
    private String surname;
    private int moves;
    private String difficulty;

    public Player() {

    }

    public Player(String name, String surname, int moves) {
        this.name = name;
        this.surname = surname;
        this.moves = moves;
    }

    public Player(String name, String surname, int i, String difficult) {
        this.name = name;
        this.surname = surname;
        this.moves = i;
        this.difficulty = difficult;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getMoves() {
        return moves;
    }

    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Aumenta le mosse di 1
     */
    public void addMoves() {
        this.moves++;
    }
}
