package com.gacha.banners;

public class Character {
    private String name;

    public Character(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
