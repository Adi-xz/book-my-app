import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * ====================================================================
 * MOCK DEPENDENCIES (Included for standalone compilation)
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

class BookingRequestQueue {
    private Queue<Reservation> requestQueue = new LinkedList<>();

    public synchronized void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public synchronized Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public synchronized boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 5); // 5 rooms available to test concurrency
    }

    public Map<String, Integer> getRoomAvailability() {
        return availability;
    }

    public void reduceAvailability(String roomType, int count) {
        availability.put(roomType, availability.get(roomType) - count);
    }
}

class RoomAllocationService {
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();
        Map<String, Integer> currentAvailability = inventory.getRoomAvailability();

        if (currentAvailability.getOrDefault(roomType, 0) > 0) {
            // Simulate processing time to make thread switching more visible
            try { Thread.sleep(50); } catch (InterruptedException e) {}

            inventory.reduceAvailability(roomType, 1);
            System.out.println("  [SUCCESS] " + Thread.currentThread().getName() +
                    " allocated " + roomType + " to " + reservation.getGuestName() +
                    ". Remaining: " + inventory.getRoomAvailability().get(roomType));
        } else {
            System.out.println("  [FAILED] " + Thread.currentThread().getName() +
                    " could not allocate for " + reservation.getGuestName() + ". Sold Out!");
        }
    }
}

/**
 * ====================================================================
 * CLASS - ConcurrentBookingProcessor
 * ====================================================================
 *
 * Use Case 11: Concurrent Booking Simulation
 *
 * Description:
 * This class represents a booking processor
 * that can be executed by multiple threads.
 *
 * It demonstrates how shared resources
 * such as booking queues and inventory
 * must be accessed in a thread-safe manner.
 *
 * @version 11.0
 */
class ConcurrentBookingProcessor implements Runnable {

    /** Shared booking request queue. */
    private BookingRequestQueue bookingQueue;

    /** Shared room inventory. */
    private RoomInventory inventory;

    /** Shared room allocation service. */
    private RoomAllocationService allocationService;

    /**
     * Creates a new booking processor.
     *
     * @param bookingQueue      shared booking queue
     * @param inventory         shared inventory
     * @param allocationService shared allocation service
     */
    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService
    ) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    /**
     * Executes booking processing logic.
     *
     * This method is called when the thread starts.
     */
    @Override
    public void run() {
        while (true) {
            Reservation reservation;

            /*
             * Synchronize on the booking queue to ensure
             * that only one thread can retrieve a request
             * at a time.
             */
            synchronized (bookingQueue) {
                if (!bookingQueue.hasPendingRequests()) {
                    break; // Exit the loop if the queue is empty
                }
                reservation = bookingQueue.getNextRequest();
            }

            /*
             * Allocation also mutates shared inventory.
             * Synchronization ensures atomic allocation.
             */
            if (reservation != null) {
                synchronized (inventory) {
                    allocationService.allocateRoom(reservation, inventory);
                }
            }
        }
    }
}

/**
 * ====================================================================
 * MAIN CLASS - UseCase11ConcurrentBookingSimulation
 * ====================================================================
 *
 * Use Case 11: Concurrent Booking Simulation
 *
 * Description:
 * This class simulates multiple users
 * attempting to book rooms at the same time.
 *
 * It highlights race conditions and
 * demonstrates how synchronization
 * prevents inconsistent allocations.
 *
 * @version 11.0
 */
public class UseCase11ConcurrentBookingSimulation {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Concurrent Booking Simulation\n-----------------------------");

        // 1. Initialize shared resources
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService();

        // 2. Flood the queue with mock requests (7 guests fighting for 5 rooms)
        for (int i = 1; i <= 7; i++) {
            bookingQueue.addRequest(new Reservation("Guest_" + i, "Single"));
        }

        System.out.println("Queue populated with 7 requests. Starting 2 processor threads...\n");

        // 3. Create booking processor tasks
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService),
                "Thread-1"
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService),
                "Thread-2"
        );

        // 4. Start concurrent processing
        t1.start();
        t2.start();

        // 5. Wait for both threads to finish processing the queue
        try {
            t1.join();
            t2.join();
            System.out.println("\nAll processing complete.");
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }
    }
}
