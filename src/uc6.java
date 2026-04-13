import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ====================================================================
 * CLASS - RoomAllocationService
 * ====================================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class is responsible for confirming
 * booking requests and assigning rooms.
 *
 * It ensures:
 * - Each room ID is unique
 * - Inventory is updated immediately
 * - No room is double-booked
 *
 * @version 6.0
 */
class RoomAllocationService {

    /**
     * Stores all allocated room IDs to
     * prevent duplicate assignments.
     */
    private Set<String> allocatedRoomIds;

    /**
     * Stores assigned room IDs by room type.
     *
     * Key   -> Room type
     * Value -> Set of assigned room IDs
     */
    private Map<String, Set<String>> assignedRoomsByType;

    /**
     * Initializes allocation tracking structures.
     */
    public RoomAllocationService() {
        this.allocatedRoomIds = new HashSet<>();
        this.assignedRoomsByType = new HashMap<>();
    }

    /**
     * Confirms a booking request by assigning
     * a unique room ID and updating inventory.
     *
     * @param reservation booking request
     * @param inventory   centralized room inventory
     */
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();
        Map<String, Integer> currentAvailability = inventory.getRoomAvailability();

        // 1. Check if the requested room type is available
        if (currentAvailability.getOrDefault(roomType, 0) > 0) {

            // 2. Generate unique room ID
            String roomId = generateRoomId(roomType);

            // 3. Register the allocation to prevent double-booking
            allocatedRoomIds.add(roomId);
            assignedRoomsByType.putIfAbsent(roomType, new HashSet<>());
            assignedRoomsByType.get(roomType).add(roomId);

            // 4. Update the inventory (Assumes RoomInventory has this method)
            inventory.reduceAvailability(roomType, 1);

            System.out.println("  [SUCCESS] Allocated Room " + roomId + " to " + reservation.getGuestName());
        } else {
            System.out.println("  [FAILED] No available rooms of type '" + roomType + "' for " + reservation.getGuestName());
        }
    }

    /**
     * Generates a unique room ID
     * for the given room type.
     *
     * @param roomType type of room
     * @return unique room ID
     */
    private String generateRoomId(String roomType) {
        // Creates a prefix from the first 3 letters of the room type (e.g., "SIN" for Single)
        String prefix = roomType.substring(0, Math.min(3, roomType.length())).toUpperCase();

        // Calculates the next room number based on how many are already assigned
        int nextRoomNumber = 101 + assignedRoomsByType.getOrDefault(roomType, new HashSet<>()).size();

        String generatedId = prefix + "-" + nextRoomNumber;

        // Failsafe: Ensure it is truly unique against the master set
        while (allocatedRoomIds.contains(generatedId)) {
            nextRoomNumber++;
            generatedId = prefix + "-" + nextRoomNumber;
        }

        return generatedId;
    }
}

/**
 * ====================================================================
 * MAIN CLASS - UseCase6RoomAllocation
 * ====================================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class demonstrates how booking
 * requests are confirmed and rooms
 * are allocated safely.
 *
 * It consumes booking requests in FIFO
 * order and updates inventory immediately.
 *
 * @version 6.0
 */
public class UseCase6RoomAllocation {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Room Allocation System\n----------------------");

        // 1. Initialize dependencies
        // (Assuming RoomInventory starts with 1 Single, 1 Double, 1 Suite)
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();
        BookingRequestQueue queue = new BookingRequestQueue();

        // 2. Create mock booking requests (including a test for an overbooked room)
        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Double"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));
        queue.addRequest(new Reservation("LateGuest", "Single")); // This should fail if only 1 Single exists

        // 3. Process the queue in FIFO order
        System.out.println("Processing Pending Requests...");
        while (queue.hasPendingRequests()) {
            Reservation nextRequest = queue.getNextRequest();
            System.out.println("\nProcessing: " + nextRequest.getGuestName() + " (" + nextRequest.getRoomType() + ")");

            // Attempt to allocate the room
            allocationService.allocateRoom(nextRequest, inventory);
        }
    }
}