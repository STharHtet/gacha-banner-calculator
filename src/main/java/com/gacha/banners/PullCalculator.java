// Informed based decision making
// Lookahead Simulation – Before skipping, check future banners and determine if skipping will prevent the user from pulling desired characters.
// Smart Skipping – Skip only if the character reappears and skipping does not cause another loss.
// Sacrifice Awareness – If skipping means losing a later character, the system prevents it.
// Warnings for Insufficient Pulls – If a character is skipped due to low pulls, the message "Skip (Insufficient pulls)" appears.
package com.gacha.banners;

import java.util.*;

public class PullCalculator {

    public static CalculationResult calculate(List<Banner> banners, Player player, int pityRate) {
        int availablePulls = player.calculateAvailablePulls();
        CalculationResult result = new CalculationResult();

        // Sort banners chronologically by startDate
        banners.sort(Comparator.comparing(Banner::getStartDate));

        // Track whether a desired character has been obtained
        Set<String> obtainedCharacters = new HashSet<>();

        for (int i = 0; i < banners.size(); i++) {
            Banner banner = banners.get(i);
            System.out.println("\nProcessing " + banner.toString());

            boolean isDesired = false;
            String desiredCharacter = null;

            // Check if this banner features a desired character
            for (String characterName : player.getDesiredCharacters()) {
                if (banner.getFeaturedCharacter().toLowerCase().contains(characterName.toLowerCase())) {
                    isDesired = true;
                    desiredCharacter = characterName;
                    break;
                }
            }

            String decision = "Skip";

            if (isDesired) {
                boolean appearsLater = false;
                boolean skippingCausesLoss = false;

                // Look ahead to see if this character appears again
                for (int j = i + 1; j < banners.size(); j++) {
                    if (banners.get(j).getFeaturedCharacter().toLowerCase().contains(desiredCharacter.toLowerCase())) {
                        appearsLater = true;
                        break;
                    }
                }

                // Look ahead for other desired characters and see if skipping causes issues
                int futurePulls = availablePulls;
                for (int j = i + 1; j < banners.size(); j++) {
                    Banner futureBanner = banners.get(j);
                    for (String futureCharacter : player.getDesiredCharacters()) {
                        if (futureBanner.getFeaturedCharacter().toLowerCase().contains(futureCharacter.toLowerCase())
                                && !obtainedCharacters.contains(futureCharacter)) {
                            if (futurePulls < pityRate) {
                                skippingCausesLoss = true;
                            }
                            futurePulls -= pityRate; // Simulate spending pulls
                        }
                    }
                    futurePulls += player.getGemsPerBanner() / player.getGemsPerPull();
                }

                // Decision Making
                if (appearsLater && !skippingCausesLoss) {
                    decision = "Skip (appears later)";
                } else if (!appearsLater || skippingCausesLoss) {
                    if (availablePulls > pityRate) {
                        decision = "Indulge";
                        availablePulls -= pityRate;
                        obtainedCharacters.add(desiredCharacter);
                    } else if (availablePulls >= pityRate) {
                        decision = "Pull";
                        availablePulls -= pityRate;
                        obtainedCharacters.add(desiredCharacter);
                    } else {
                        decision = "Skip (Insufficient pulls)";
                    }
                }
            }

            // Fix: Update pulls before adding to result. Fixed where first banner does not update with 
            // gems per banner. This is assuming that the banners are based on a future roadmap and not 
            // an ongoing banner which could mean the banner is over and the player has already earned 
            // the necessary gems
            int newAvailablePulls = availablePulls + (player.getGemsPerBanner() / player.getGemsPerPull());

            // Add result first so remaining pulls reflect the correct state **before** the next banner
            result.addResult(banner.getFeaturedCharacter(), decision, newAvailablePulls);

            // Update available pulls for the next iteration
            availablePulls = newAvailablePulls;
        }

        return result;
    }
}




// Original test with checking future banners to ensure skip for save
// package com.gacha.banners;

// import java.util.*;

// public class PullCalculator {

//     public static CalculationResult calculate(List<Banner> banners, Player player, int pityRate) {
//         int availablePulls = player.calculateAvailablePulls();
//         CalculationResult result = new CalculationResult();

//         // Sort banners chronologically by startDate
//         banners.sort(Comparator.comparing(Banner::getStartDate));

//         // Process banners in chronological order
//         for (int i = 0; i < banners.size(); i++) {
//             Banner banner = banners.get(i);
//             System.out.println("\nProcessing " + banner.toString());

//             boolean isDesired = false;
//             String desiredCharacter = null;

//             // Check if the banner features a desired character
//             for (String characterName : player.getDesiredCharacters()) {
//                 if (banner.getFeaturedCharacter().toLowerCase().contains(characterName.toLowerCase())) {
//                     isDesired = true;
//                     desiredCharacter = characterName;
//                     break;
//                 }
//             }

//             String decision = "Skip";

//             if (isDesired) {
//                 // Check if this character appears later in the banners
//                 boolean appearsLater = false;
//                 for (int j = i + 1; j < banners.size(); j++) {
//                     if (banners.get(j).getFeaturedCharacter().toLowerCase().contains(desiredCharacter.toLowerCase())) {
//                         appearsLater = true;
//                         break;
//                     }
//                 }

//                 if (appearsLater) {
//                     decision = "Skip (appears later)";
//                 } else {
//                     // If it doesn't appear later, Pull or Indulge based on available pulls
//                     if (availablePulls > pityRate) {
//                         decision = "Indulge";
//                         availablePulls -= pityRate;
//                     } else if (availablePulls >= pityRate) {
//                         decision = "Pull";
//                         availablePulls -= pityRate;
//                     }
//                 }
//             }

//             result.addResult(banner.getFeaturedCharacter(), decision, availablePulls);
//             availablePulls += player.getGemsPerBanner() / player.getGemsPerPull();
//         }

//         return result;
//     }
// }


// With start and end date
// package com.gacha.banners;

// import java.util.*;

// public class PullCalculator {

//     public static CalculationResult calculate(List<Banner> banners, Player player, int pityRate) {
//         int availablePulls = player.calculateAvailablePulls();
//         CalculationResult result = new CalculationResult();

//         // Sort banners chronologically by startDate
//         banners.sort(Comparator.comparing(Banner::getStartDate));

//         // Map to track whether a desired character has been skipped already
//         Map<String, Boolean> skippedCharacters = new HashMap<>();

//         // Process banners in chronological order
//         for (Banner banner : banners) {
//             System.out.println("\nProcessing " + banner.toString());

//             boolean isDesired = false;
//             String desiredCharacter = null;

//             // Check if the banner features a desired character
//             for (String characterName : player.getDesiredCharacters()) {
//                 if (banner.getFeaturedCharacter().toLowerCase().contains(characterName.toLowerCase())) {
//                     isDesired = true;
//                     desiredCharacter = characterName;
//                     break;
//                 }
//             }

//             String decision = "Skip";

//             if (isDesired) {
//                 // Check if this is the first occurrence of the desired character
//                 if (!skippedCharacters.containsKey(desiredCharacter)) {
//                     // First occurrence: Skip and mark as skipped
//                     decision = "Skip (appears later)";
//                     skippedCharacters.put(desiredCharacter, true);
//                 } else {
//                     // Subsequent occurrence: Decide to Pull or Indulge
//                     if (availablePulls > pityRate) {
//                         decision = "Indulge";
//                         availablePulls -= pityRate;
//                     } else if (availablePulls >= pityRate) {
//                         decision = "Pull";
//                         availablePulls -= pityRate;
//                     }
//                 }
//             }

//             // Add the result with startDate and endDate
//             result.addResult(
//                 banner.getFeaturedCharacter(),
//                 decision,
//                 availablePulls,
//                 banner.getStartDate(),
//                 banner.getEndDate()
//             );

//             // Update available pulls for the next banner
//             availablePulls += player.getGemsPerBanner() / player.getGemsPerPull();
//         }

//         return result;
//     }
// }