import java.util.Map;

/**
 * ====================================================================
 * CLASS - RoomSearchService
 * ====================================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * Description:
 * This class provides search functionality
 * for guests to view available rooms.
 *
 * It reads room availability from inventory
 * and room details from Room objects.
 *
 * No inventory mutation or booking logic
 * is performed in this class.
 *
 * @version 4.0
 */
public class RoomSearchService {

    /**
     * Displays available rooms along with
     * their details and pricing.
     *
     * This method performs read-only access
     * to inventory and room data.
     *
     * @param inventory  centralized room inventory
     * @param singleRoom single room definition
     * @param doubleRoom double room definition
     * @param suiteRoom  suite room definition
     */
    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        System.out.println("--- Available Rooms ---");

        // Check and display Single Room availability
        if (availability.get("Single") > 0) {
            System.out.println("\n[" + availability.get("Single") + "] Single Rooms Available:");
            singleRoom.displayDetails(); // Assuming your Room class has a display method
        }

        // Check and display Double Room availability
        if (availability.get("Double") > 0) {
            System.out.println("\n[" + availability.get("Double") + "] Double Rooms Available:");
            doubleRoom.displayDetails();
        }

        // Check and display Suite Room availability
        if (availability.get("Suite") > 0) {
            System.out.println("\n[" + availability.get("Suite") + "] Suite Rooms Available:");
            suiteRoom.displayDetails();
        }
    }
}

/**
 * ====================================================================
 * MAIN CLASS - UseCase4RoomSearch
 * ====================================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * Description:
 * This class demonstrates how guests
 * can view available rooms without
 * modifying inventory data.
 *
 * The system enforces read-only access
 * by design and usage discipline.
 *
 * @version 4.0
 */
public class UseCase4RoomSearch {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // 1. Initialize the mock inventory
        RoomInventory inventory = new RoomInventory();

        // 2. Initialize the room types (assuming a constructor with Type and Price)
        Room singleRoom = new Room("Single", 100.00);
        Room doubleRoom = new Room("Double", 150.00);
        Room suiteRoom = new Room("Suite", 300.00);

        // 3. Instantiate the search service
        RoomSearchService searchService = new RoomSearchService();

        // 4. Execute the search
        searchService.searchAvailableRooms(inventory, singleRoom, doubleRoom, suiteRoom);
    }
}
