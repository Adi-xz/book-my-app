import java.util.ArrayList;
import java.util.List;

public class UseCase2TrainConsistMgmt {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" UC2 - Add Passenger Bogies to Train ");
        System.out.println("======================================\n");

        // Create an ArrayList to hold passenger bogies
        List<String> passengerBogies = new ArrayList<>();

        // ---- CREATE (Add bogies) ----
        passengerBogies.add("Bogie A1");
        passengerBogies.add("Bogie B1");
        passengerBogies.add("Bogie C1");

        System.out.println("After adding bogies:");
        System.out.println(passengerBogies);

        // ---- READ (Check availability) ----
        String searchBogie = "Bogie B1";
        if (passengerBogies.contains(searchBogie)) {
            System.out.println(searchBogie + " is available in the train.");
        } else {
            System.out.println(searchBogie + " is NOT available.");
        }

        // ---- UPDATE (Modify bogie) ----
        passengerBogies.set(1, "Bogie B2");
        System.out.println("\nAfter updating bogie:");
        System.out.println(passengerBogies);

        // ---- DELETE (Remove bogie) ----
        passengerBogies.remove("Bogie A1");
        System.out.println("\nAfter removing bogie:");
        System.out.println(passengerBogies);

        // ---- FINAL CONSIST ----
        System.out.println("\nFinal train consist:");
        for (String bogie : passengerBogies) {
            System.out.println(bogie);
        }
    }
}
