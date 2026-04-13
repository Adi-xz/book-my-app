import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * ====================================================================
 * MOCK DEPENDENCY (Included for standalone compilation)
 * ====================================================================
 */
class RoomInventory {
    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        // Mocking an inventory where some rooms are already taken
        availability.put("Single", 0);
        availability.put("Double", 2);
    }

    public void increaseAvailability(String roomType, int count) {
        availability.put(roomType, availability.getOrDefault(roomType, 0) + count);
    }

    public void displayInventory() {
        System.out.println("  Current Inventory -> " + availability);
    }
}

/**
 * ====================================================================
 * CLASS - CancellationService
 * ====================================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * This class handles the cancellation of
 * confirmed bookings and ensures inventory
 * is rolled back safely.
 *
 * @version 10.0
 */
class CancellationService {

    /** Stack that stores recently released room IDs. */
    private Stack<String> releasedRoomIds;

    /** Maps reservation ID to room type. */
    private Map<String, String> reservationRoomTypeMap;

    /** Initializes cancellation tracking structures. */
    public CancellationService() {
        this.releasedRoomIds = new Stack<>();
        this.reservationRoomTypeMap = new HashMap<>();
    }

    /**
     * Registers a confirmed booking.
     *
     * This method simulates storing confirmation
     * data that will later be required for cancellation.
     *
     * @param reservationId confirmed reservation ID
     * @param roomType      allocated room type
     */
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
        System.out.println("  [REGISTERED] Reservation " + reservationId + " (" + roomType + ")");
    }

    /**
     * Cancels a confirmed booking and
     * restores inventory safely.
     *
     * @param reservationId reservation to cancel
     * @param inventory     centralized room inventory
     */
    public void cancelBooking(String reservationId, RoomInventory inventory) {
        if (reservationRoomTypeMap.containsKey(reservationId)) {
            // 1. Identify the room type being released
            String roomType = reservationRoomTypeMap.get(reservationId);

            // 2. Remove from active reservations
            reservationRoomTypeMap.remove(reservationId);

            // 3. Push to the rollback history stack
            releasedRoomIds.push(reservationId);

            // 4. Restore the inventory
            inventory.increaseAvailability(roomType, 1);

            System.out.println("  [CANCELLED] Reservation " + reservationId + " cancelled. " + roomType + " inventory restored.");
        } else {
            System.out.println("  [ERROR] Cannot cancel. Reservation " + reservationId + " not found.");
        }
    }

    /**
     * Displays recently cancelled reservations.
     *
     * This method helps visualize rollback order.
     */
    public void showRollbackHistory() {
        System.out.println("\n--- Rollback History (LIFO Order) ---");
        if (releasedRoomIds.isEmpty()) {
            System.out.println("  No cancellations on record.");
        } else {
            // We clone the stack here just to print it without destroying the actual history data
            @SuppressWarnings("unchecked")
            Stack<String> displayStack = (Stack<String>) releasedRoomIds.clone();

            int step = 1;
            while (!displayStack.isEmpty()) {
                System.out.println("  " + step + ". Rolled back Room ID: " + displayStack.pop());
                step++;
            }
        }
    }
}

/**
 * ====================================================================
 * MAIN CLASS - UseCase10BookingCancellation
 * ====================================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * This class demonstrates how confirmed
 * bookings can be cancelled safely.
 *
 * Inventory is restored and rollback
 * history is maintained.
 *
 * @version 10.0
 */
public class UseCase10BookingCancellation {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Booking Cancellation System\n---------------------------");

        // 1. Initialize components
        RoomInventory inventory = new RoomInventory();
        CancellationService cancelService = new CancellationService();

        // Show starting inventory
        System.out.println("\n[Initial State]");
        inventory.displayInventory();

        // 2. Mocking some existing confirmed bookings (from Use Case 6)
        System.out.println("\n[Active Bookings]");
        cancelService.registerBooking("SIN-101", "Single");
        cancelService.registerBooking("DBL-201", "Double");
        cancelService.registerBooking("SIN-102", "Single");

        // 3. Process Cancellations
        System.out.println("\n[Processing Cancellations]");
        cancelService.cancelBooking("SIN-101", inventory);
        cancelService.cancelBooking("DBL-201", inventory);

        // Try to cancel a booking that doesn't exist
        cancelService.cancelBooking("SUI-999", inventory);

        // 4. Show ending inventory to prove rollback worked
        System.out.println("\n[Final State]");
        inventory.displayInventory();

        // 5. Show the stack history
        cancelService.showRollbackHistory();
    }
}
