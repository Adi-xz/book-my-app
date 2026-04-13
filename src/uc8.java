import java.util.ArrayList;
import java.util.List;

/**
 * ====================================================================
 * CLASS - Reservation (Copied from Use Case 5 for compilation)
 * ====================================================================
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

/**
 * ====================================================================
 * CLASS - BookingHistory
 * ====================================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class maintains a record of
 * confirmed reservations.
 *
 * It provides ordered storage for
 * historical and reporting purposes.
 *
 * @version 8.0
 */
class BookingHistory {

    /**
     * List that stores confirmed reservations.
     */
    private List<Reservation> confirmedReservations;

    /**
     * Initializes an empty booking history.
     */
    public BookingHistory() { confirmedReservations = new ArrayList<>(); }

    /**
     * Adds a confirmed reservation
     * to booking history.
     *
     * @param reservation confirmed booking
     */
    public void addReservation(Reservation reservation) { confirmedReservations.add(reservation); }

    /**
     * Returns all confirmed reservations.
     *
     * @return list of reservations
     */
    public List<Reservation> getConfirmedReservations() { return confirmedReservations; }
}

/**
 * ====================================================================
 * CLASS - BookingReportService
 * ====================================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class generates reports
 * from booking history data.
 *
 * Reporting logic is separated
 * from data storage.
 *
 * @version 8.0
 */
class BookingReportService {

    /**
     * Displays a summary report
     * of all confirmed bookings.
     *
     * @param history booking history
     */
    public void generateReport(BookingHistory history) {
        List<Reservation> reservations = history.getConfirmedReservations();

        System.out.println("\n=========================================");
        System.out.println("        OFFICIAL BOOKING REPORT          ");
        System.out.println("=========================================");

        if (reservations.isEmpty()) {
            System.out.println("No confirmed bookings on record.");
        } else {
            System.out.println("Total Confirmed Bookings: " + reservations.size() + "\n");
            for (int i = 0; i < reservations.size(); i++) {
                Reservation r = reservations.get(i);
                System.out.println("  " + (i + 1) + ". Guest: " + r.getGuestName() + " | Room Type: " + r.getRoomType());
            }
        }
        System.out.println("=========================================\n");
    }
}

/**
 * ====================================================================
 * MAIN CLASS - UseCase8BookingHistoryReport
 * ====================================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class demonstrates how
 * confirmed bookings are stored
 * and reported.
 *
 * The system maintains an ordered
 * audit trail of reservations.
 *
 * @version 8.0
 */
public class UseCase8BookingHistoryReport {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Initializing Booking History System...");

        // 1. Initialize the history storage and reporting service
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // 2. Create some mock confirmed reservations
        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        // 3. Add them to the history (simulating what would happen after Use Case 6)
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // 4. Generate the report
        reportService.generateReport(history);
    }
}
