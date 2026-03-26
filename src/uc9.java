import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

/**
 * ====================================================================
 * MOCK DEPENDENCIES (Included for standalone compilation)
 * ====================================================================
 */
class RoomInventory {
    private Map<String, Integer> availability;
    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 0); // Set to 0 to test the 'sold out' validation exception
    }
    public Map<String, Integer> getRoomAvailability() { return availability; }
}

class Reservation {
    private String guestName;
    private String roomType;
    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> requestQueue = new LinkedList<>();
    public void addRequest(Reservation reservation) { requestQueue.offer(reservation); }
}

/**
 * ====================================================================
 * CLASS - InvalidBookingException
 * ====================================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * This custom exception represents
 * invalid booking scenarios in the system.
 *
 * Using a domain-specific exception
 * makes error handling clearer and safer.
 *
 * @version 9.0
 */
class InvalidBookingException extends Exception {

    /**
     * Creates an exception with
     * a descriptive error message.
     *
     * @param message error description
     */
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * ====================================================================
 * CLASS - ReservationValidator
 * ====================================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * This class is responsible for validating
 * booking requests before they are processed.
 *
 * All validation rules are centralized
 * to avoid duplication and inconsistency.
 *
 * @version 9.0
 */
class ReservationValidator {

    /**
     * Validates booking input provided by the user.
     *
     * @param guestName name of the guest
     * @param roomType  requested room type
     * @param inventory centralized inventory
     * @throws InvalidBookingException if validation fails
     */
    public void validate(
            String guestName,
            String roomType,
            RoomInventory inventory
    ) throws InvalidBookingException {

        // Rule 1: Name cannot be null or empty
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Rule 2: Room type cannot be null or empty
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        Map<String, Integer> availableRooms = inventory.getRoomAvailability();

        // Rule 3: The requested room type must exist in the system
        if (!availableRooms.containsKey(roomType)) {
            throw new InvalidBookingException("Unrecognized room type: '" + roomType + "'.");
        }

        // Rule 4: The requested room type must have available inventory
        if (availableRooms.get(roomType) <= 0) {
            throw new InvalidBookingException("The requested room type '" + roomType + "' is currently sold out.");
        }
    }
}

/**
 * ====================================================================
 * MAIN CLASS - UseCase9ErrorHandlingValidation
 * ====================================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * This class demonstrates how user input
 * is validated before booking is processed.
 *
 * The system:
 * - Accepts user input
 * - Validates input centrally
 * - Handles errors gracefully
 *
 * @version 9.0
 */
public class UseCase9ErrorHandlingValidation {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Display application header
        System.out.println("Booking Validation\n------------------");

        Scanner scanner = new Scanner(System.in);

        // Initialize required components
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            // 1. Collect user input
            System.out.print("Enter Guest Name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter Room Type (Single / Double / Suite): ");
            String roomType = scanner.nextLine();

            // 2. Validate the input centrally
            validator.validate(guestName, roomType, inventory);

            // 3. If no exception is thrown, the data is safe to process
            Reservation newBooking = new Reservation(guestName, roomType);
            bookingQueue.addRequest(newBooking);

            System.out.println("\n[SUCCESS] Booking validated and added to the processing queue!");

        } catch (InvalidBookingException e) {

            // Handle domain-specific validation errors gracefully
            System.out.println("\n[ERROR] Booking failed: " + e.getMessage());

        } finally {
            // Ensure resources are released
            scanner.close();
        }
    }
}
