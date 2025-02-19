package com.gacha.banners;

import java.util.List;

public class Player {
    private int currentGems;
    private int gemsPerBanner;
    private int gemsPerPull;
    private int guaranteePullCount;
    private List<String> desiredCharacters; // Change from List<Character> to List<String>

    // Add a default constructor (needed for JSON deserialization)
    public Player() {}

    public Player(int currentGems, int gemsPerBanner, int gemsPerPull, int guaranteePullCount, List<String> desiredCharacters) {
        this.currentGems = currentGems;
        this.gemsPerBanner = gemsPerBanner;
        this.gemsPerPull = gemsPerPull;
        this.guaranteePullCount = guaranteePullCount;
        this.desiredCharacters = desiredCharacters;
    }

    public int getCurrentGems() { return currentGems; }
    public int getGemsPerBanner() { return gemsPerBanner; }
    public int getGemsPerPull() { return gemsPerPull; }
    public int getGuaranteePullCount() { return guaranteePullCount; }
    public List<String> getDesiredCharacters() { return desiredCharacters; }

    public void setCurrentGems(int currentGems) { this.currentGems = currentGems; }
    public void setGemsPerBanner(int gemsPerBanner) { this.gemsPerBanner = gemsPerBanner; }
    public void setGemsPerPull(int gemsPerPull) { this.gemsPerPull = gemsPerPull; }
    public void setGuaranteePullCount(int guaranteePullCount) { this.guaranteePullCount = guaranteePullCount; }
    public void setDesiredCharacters(List<String> desiredCharacters) { this.desiredCharacters = desiredCharacters; }

    // Calculate available pulls based on current gems and pull cost
    public int calculateAvailablePulls() {
        return currentGems / gemsPerPull;
    }
}
