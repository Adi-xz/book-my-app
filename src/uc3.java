import java.util.HashSet;
import java.util.Set;

public class UseCase3TrainConsistMgmt {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" UC3 - Track Unique Bogie IDs ");
        System.out.println("======================================\n");

        // Create a Set to store unique bogie IDs
        // HashSet stores only unique values
        Set<String> bogies = new HashSet<>();

        // ---- ADD IDs (including duplicates) ----
        bogies.add("BG101");
        bogies.add("BG102");
        bogies.add("BG103");
        bogies.add("BG104");

        // Duplicate entries (will be ignored automatically)
        bogies.add("BG101");
        bogies.add("BG102");

        // ---- DISPLAY UNIQUE BOGIES ----
        System.out.println("Unique Bogie IDs:");
        for (String bogie : bogies) {
            System.out.println(bogie);
        }

        // ---- CHECK EXISTENCE ----
        String checkId = "BG103";
        if (bogies.contains(checkId)) {
            System.out.println("\n" + checkId + " exists in the set.");
        } else {
            System.out.println("\n" + checkId + " does not exist.");
        }
    }
}
