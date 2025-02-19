// Original
package com.gacha.banners;
import java.util.ArrayList;
import java.util.List;

public class CalculationResult {
    private List<String> results = new ArrayList<>();

    public void addResult(String character, String decision, int remainingPulls) {
        results.add("Character: " + character + " | Decision: " + decision + " | Remaining Pulls: " + remainingPulls);
    }

    public List<String> getResults() {
        return results;
    }
}

// Added start and end date
// package com.gacha.banners;

// import java.util.ArrayList;
// import java.util.List;

// public class CalculationResult {
//     private List<String> results = new ArrayList<>();

//     public void addResult(String character, String decision, int remainingPulls, String startDate, String endDate) {
//         String result = String.format(
//             "Character: %s | Decision: %s | Remaining Pulls: %d | Start Date: %s | End Date: %s",
//             character, decision, remainingPulls, startDate, endDate
//         );
//         results.add(result);
//     }

//     public List<String> getResults() {
//         return results;
//     }
// }
