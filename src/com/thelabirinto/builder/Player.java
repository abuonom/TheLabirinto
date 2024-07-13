package com.thelabirinto.builder;

public class Player {
    private String name;
    private String surname;
    private int moves;
    private double difficulty;

    public Player() {

    }

    public Player(String name, String surname, int moves) {
        this.name = name;
        this.surname = surname;
        this.moves = moves;
    }

    public Player(String name, String surname, int i, double difficult) {
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

    public double getDifficulty() {
        return difficulty;
    }

    public void addMoves() {
        this.moves++;
    }
}
