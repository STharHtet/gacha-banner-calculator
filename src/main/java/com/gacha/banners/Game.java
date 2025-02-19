package com.gacha.banners;

import java.util.List;

public class Game {
    private String name;
    private int gemsPerPull;
    private int pityRate;
    private List<Banner> banners;

    public Game(String name, int gemsPerPull, int pityRate, List<Banner> banners) {
        this.name = name;
        this.gemsPerPull = gemsPerPull;
        this.pityRate = pityRate;
        this.banners = banners;
    }

    public String getName() { return name; }
    public int getGemsPerPull() { return gemsPerPull; }
    public int getPityRate() { return pityRate; }
    public List<Banner> getBanners() { return banners; }
}
