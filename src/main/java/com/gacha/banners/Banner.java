package com.gacha.banners;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class Banner {
    private String name;
    private String startDate;
    private String endDate;
    private String featuredCharacter;

    public Banner(String name, String startDate, String endDate, String featuredCharacter) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.featuredCharacter = featuredCharacter;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getFeaturedCharacter() {
        return featuredCharacter;
    }

    @Override
    public String toString() {
        return "Banner: " + name + " (" + startDate + " to " + endDate + ") - Featured: " + featuredCharacter;
    }

    // Load banners from JSON file
    public static List<Banner> loadBannersFromJson(String filePath) {
        List<Banner> banners = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONArray bannersArray = (JSONArray) parser.parse(reader);

            for (Object obj : bannersArray) {
                JSONObject bannerJson = (JSONObject) obj;
                String name = (String) bannerJson.get("name");
                String startDate = (String) bannerJson.get("startDate");
                String endDate = (String) bannerJson.get("endDate");
                String featuredCharacter = (String) bannerJson.get("featuredCharacter");

                banners.add(new Banner(name, startDate, endDate, featuredCharacter));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return banners;
    }

    // Save banners to JSON file
    public static void saveBannersToJson(String filePath, List<Banner> banners) {
        JSONArray bannersArray = new JSONArray();
        for (Banner banner : banners) {
            JSONObject bannerJson = new JSONObject();
            bannerJson.put("name", banner.getName());
            bannerJson.put("startDate", banner.getStartDate());
            bannerJson.put("endDate", banner.getEndDate());
            bannerJson.put("featuredCharacter", banner.getFeaturedCharacter());
            bannersArray.add(bannerJson);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(bannersArray.toJSONString());
            file.flush();
            System.out.println("\nBanner successfully saved to banners.json!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add a new banner via user input
    public static void addNewBanner(String filePath) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter Banner Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Banner Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();

        System.out.print("Enter Banner End Date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();

        System.out.print("Enter Featured Character: ");
        String featuredCharacter = scanner.nextLine();

        // Load existing banners
        List<Banner> banners = loadBannersFromJson(filePath);

        // Add new banner
        banners.add(new Banner(name, startDate, endDate, featuredCharacter));

        // Save updated banners back to JSON file
        saveBannersToJson(filePath, banners);
        System.out.println("\nNew banner added successfully!");
    }
}
