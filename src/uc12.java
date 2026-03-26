import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ====================================================================
 * MOCK DEPENDENCY (Included for standalone compilation)
 * ====================================================================
 */
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public Map<String, Integer> getRoomAvailability() {
        return availability;
    }

    public void setAvailability(String roomType, int count) {
        availability.put(roomType, count);
    }

    public void displayInventory() {
        System.out.println("  Current Inventory -> " + availability);
    }
}

/**
 * ====================================================================
 * CLASS - FilePersistenceService
 * ====================================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * This class is responsible for persisting
 * critical system state to a plain text file.
 *
 * It supports:
 * - Saving room inventory state
 * - Restoring inventory on system startup
 *
 * No database or serialization framework
 * is used in this use case.
 *
 * @version 12.0
 */
class FilePersistenceService {

    /**
     * Saves room inventory state to a file.
     *
     * Each line follows the format:
     * roomType=availableCount
     *
     * @param inventory centralized room inventory
     * @param filePath  path to persistence file
     */
    public void saveInventory(RoomInventory inventory, String filePath) {
        // Using try-with-resources to ensure the writer is closed automatically
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Map<String, Integer> rooms = inventory.getRoomAvailability();

            for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            System.out.println("  [SAVE SUCCESS] Inventory saved securely to: " + filePath);

        } catch (IOException e) {
            System.out.println("  [SAVE ERROR] Failed to save inventory: " + e.getMessage());
        }
    }

    /**
     * Loads room inventory state from a file.
     *
     * @param inventory centralized room inventory
     * @param filePath  path to persistence file
     */
    public void loadInventory(RoomInventory inventory, String filePath) {
        File file = new File(filePath);

        // Prevent crashing if the file doesn't exist yet (e.g., first time booting)
        if (!file.exists()) {
            System.out.println("  [LOAD INFO] No existing save file found. Starting with empty inventory.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int restoredCount = 0;

            // Read line by line until EOF
            while ((line = reader.readLine()) != null) {
                // Split the line into "roomType" and "count"
                String[] parts = line.split("=");

                if (parts.length == 2) {
                    String roomType = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());

                    inventory.setAvailability(roomType, count);
                    restoredCount++;
                }
            }
            System.out.println("  [LOAD SUCCESS] Restored " + restoredCount + " room types from: " + filePath);

        } catch (IOException | NumberFormatException e) {
            System.out.println("  [LOAD ERROR] Failed to load inventory: " + e.getMessage());
        }
    }
}

/**
 * ====================================================================
 * MAIN CLASS - UseCase12DataPersistenceRecovery
 * ====================================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * This class demonstrates how system state
 * can be restored after an application restart.
 *
 * Inventory data is loaded from a file
 * before any booking operations occur.
 *
 * @version 12.0
 */
public class UseCase12DataPersistenceRecovery {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Data Persistence & Recovery System\n----------------------------------");

        String saveFileName = "hotel_inventory.txt";
        FilePersistenceService persistenceService = new FilePersistenceService();

        // ---------------------------------------------------------
        // PHASE 1: Normal System Operation & Shutdown
        // ---------------------------------------------------------
        System.out.println("\n[PHASE 1: System Running]");
        RoomInventory activeInventory = new RoomInventory();

        // Simulate an administrator setting up the hotel inventory
        activeInventory.setAvailability("Single", 12);
        activeInventory.setAvailability("Double", 8);
        activeInventory.setAvailability("Suite", 2);
        activeInventory.displayInventory();

        // System is shutting down, trigger the save
        System.out.println("\nSystem shutting down. Triggering data persistence...");
        persistenceService.saveInventory(activeInventory, saveFileName);


        // ---------------------------------------------------------
        // PHASE 2: System Boot & Recovery
        // ---------------------------------------------------------
        System.out.println("\n[PHASE 2: System Boot Sequence Initiated]");

        // A brand new instance of inventory representing a fresh start
        RoomInventory bootingInventory = new RoomInventory();
        System.out.print("  Initial state in RAM -> ");
        bootingInventory.displayInventory(); // This will be empty

        // Recover the state from the text file
        System.out.println("\nTriggering data recovery protocol...");
        persistenceService.loadInventory(bootingInventory, saveFileName);

        // Verify recovery was successful
        System.out.println("\n[System Ready]");
        bootingInventory.displayInventory();
    }
}
