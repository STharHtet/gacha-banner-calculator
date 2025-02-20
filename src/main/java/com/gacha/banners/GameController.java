package com.gacha.banners;

import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

//@CrossOrigin(origins = "http://localhost:5173") // allow frontend to access backend; remember this setting
//@CrossOrigin(origins = "https://gacha-banner-calculator.netlify.app")  // allow Netlify frontend
// @CrossOrigin(origins = { "https://gacha-banner-calculator.netlify.app", "http://localhost:5173" })
@CrossOrigin(origins = "*") // allow all; this is a security risk
@RestController
@RequestMapping("/games")
public class GameController {
    private static final List<Game> games = new ArrayList<>();
    // private static final String BANNERS_JSON_PATH = "banners.json"; // use the JSON outside to save local and for persistence. Uncomment here if you are not using docker

    // Load games from JSON when the application starts
    static {
        loadBannersFromJson();
    }

    @GetMapping
    public List<Game> getGames() {
        return games;
    }

    @GetMapping("/{gameName}")
    public Game getGame(@PathVariable String gameName) {
        return games.stream()
                .filter(game -> game.getName().equalsIgnoreCase(gameName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }

    @GetMapping("/{gameName}/banners")
    public List<Banner> getGameBanners(@PathVariable String gameName) {
        return getGame(gameName).getBanners();
    }

    @PostMapping("/{gameName}/banners")
    public String addBanner(@PathVariable String gameName, @RequestBody Banner banner) {
        Game game = getGame(gameName);
        game.getBanners().add(banner);
        saveBannersToJson();
        return "✅ Banner added successfully!";
    }

    @PostMapping("/{gameName}/calculate")
    public CalculationResult calculatePulls(@PathVariable String gameName, @RequestBody Player player) {
        Game game = getGame(gameName);
        if (game.getBanners() == null || game.getBanners().isEmpty()) {
            throw new RuntimeException("No banners found for " + gameName);
        }
        return PullCalculator.calculate(game.getBanners(), player, game.getPityRate());
    }

    // Load banners from banners.json (This is for local testing)
    // private static void loadBannersFromJson() {
    //     JSONParser parser = new JSONParser();
    //     try {
    //         FileReader reader = new FileReader(BANNERS_JSON_PATH);
    //         JSONObject bannersJson = (JSONObject) parser.parse(reader);

    //         // Add games with banners loaded from JSON
    //         games.add(new Game("Genshin-Impact", 160, 90, parseBanners(bannersJson, "Genshin-Impact")));
    //         games.add(new Game("Wuthering-Waves", 160, 80, parseBanners(bannersJson, "Wuthering-Waves")));
    //         games.add(new Game("Honkai-Star-Rail", 160, 90, parseBanners(bannersJson, "Honkai-Star-Rail")));

    //         reader.close();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    // Load banners.json for docker
    private static final String BANNERS_JSON_PATH = "/app/banners.json";

private static void loadBannersFromJson() {
    JSONParser parser = new JSONParser();
    try (FileReader reader = new FileReader(BANNERS_JSON_PATH)) {
        JSONObject bannersJson = (JSONObject) parser.parse(reader);
        
        // Load banners from JSON
        games.add(new Game("Genshin-Impact", 160, 90, parseBanners(bannersJson, "Genshin-Impact")));
        games.add(new Game("Wuthering-Waves", 160, 80, parseBanners(bannersJson, "Wuthering-Waves")));
        games.add(new Game("Honkai-Star-Rail", 160, 90, parseBanners(bannersJson, "Honkai-Star-Rail")));
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // Save banners to banners.json when a new banner is added
    private static void saveBannersToJson() {
        JSONObject bannersJson = new JSONObject();
        for (Game game : games) {
            JSONArray bannersArray = new JSONArray();
            for (Banner banner : game.getBanners()) {
                JSONObject bannerJson = new JSONObject();
                bannerJson.put("name", banner.getName());
                bannerJson.put("startDate", banner.getStartDate());
                bannerJson.put("endDate", banner.getEndDate());
                bannerJson.put("featuredCharacter", banner.getFeaturedCharacter());
                bannersArray.add(bannerJson);
            }
            bannersJson.put(game.getName(), bannersArray);
        }

        try (FileWriter file = new FileWriter(BANNERS_JSON_PATH)) {
            file.write(bannersJson.toJSONString());
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Parse banners for a specific game
    private static List<Banner> parseBanners(JSONObject bannersJson, String gameName) {
        List<Banner> banners = new ArrayList<>();
        JSONArray bannersArray = (JSONArray) bannersJson.get(gameName);
        if (bannersArray != null) {
            for (Object obj : bannersArray) {
                JSONObject bannerJson = (JSONObject) obj;
                banners.add(new Banner(
                        (String) bannerJson.get("name"),
                        (String) bannerJson.get("startDate"),
                        (String) bannerJson.get("endDate"),
                        (String) bannerJson.get("featuredCharacter")
                ));
            }
        }
        return banners;
    }
}
