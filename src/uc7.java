import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ====================================================================
 * CLASS - AddOnService
 * ====================================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * Description:
 * This class represents an optional service
 * that can be added to a confirmed reservation.
 *
 * Examples:
 * - Breakfast
 * - Spa
 * - Airport Pickup
 *
 * @version 7.0
 */
class AddOnService {

    /**
     * Name of the service.
     */
    private String serviceName;

    /**
     * Cost of the service.
     */
    private double cost;

    /**
     * Creates a new add-on service.
     *
     * @param serviceName name of the service
     * @param cost        cost of the service
     */
    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    /**
     * @return service name
     */
    public String getServiceName() { return serviceName; }

    /**
     * @return service cost
     */
    public double getCost() { return cost; }
}

/**
 * ====================================================================
 * CLASS - AddOnServiceManager
 * ====================================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * Description:
 * This class manages optional services
 * associated with confirmed reservations.
 *
 * It supports attaching multiple services
 * to a single reservation.
 *
 * @version 7.0
 */
class AddOnServiceManager {

    /**
     * Maps reservation ID to selected services.
     *
     * Key   -> Reservation ID
     * Value -> List of selected services
     */
    private Map<String, List<AddOnService>> servicesByReservation;

    /**
     * Initializes the service manager.
     */
    public AddOnServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    /**
     * Attaches a service to a reservation.
     *
     * @param reservationId confirmed reservation ID
     * @param service       add-on service
     */
    public void addService(String reservationId, AddOnService service) {
        // If the reservation doesn't have a list yet, create one
        servicesByReservation.putIfAbsent(reservationId, new ArrayList<>());

        // Add the service to the reservation's list
        servicesByReservation.get(reservationId).add(service);

        System.out.println("  [ADDED] " + service.getServiceName() + " ($" + service.getCost() + ") to Reservation " + reservationId);
    }

    /**
     * Calculates total add-on cost
     * for a reservation.
     *
     * @param reservationId reservation ID
     * @return total service cost
     */
    public double calculateTotalServiceCost(String reservationId) {
        double totalCost = 0.0;

        // Fetch the list of services, default to an empty list if none exist
        List<AddOnService> services = servicesByReservation.getOrDefault(reservationId, new ArrayList<>());

        for (AddOnService service : services) {
            totalCost += service.getCost();
        }

        return totalCost;
    }
}

/**
 * ====================================================================
 * MAIN CLASS - UseCase7AddOnServiceSelection
 * ====================================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * Description:
 * This class demonstrates how optional
 * services can be attached to a confirmed
 * booking.
 *
 * Services are added after room allocation
 * and do not affect inventory.
 *
 * @version 7.0
 */
public class UseCase7AddOnServiceSelection {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Add-On Service Selection\n------------------------");

        // 1. Initialize the service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // 2. Define some available mock services
        AddOnService breakfast = new AddOnService("Breakfast Buffet", 25.00);
        AddOnService spa = new AddOnService("Spa Massage", 120.00);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 50.00);

        // 3. Mock a confirmed reservation ID (simulating output from Use Case 6)
        String reservationId = "SIN-101";
        System.out.println("Guest for Reservation " + reservationId + " is selecting services...");

        // 4. Attach services to the reservation
        serviceManager.addService(reservationId, breakfast);
        serviceManager.addService(reservationId, airportPickup);

        // 5. Calculate and display the total cost for these add-ons
        double totalAddOnCost = serviceManager.calculateTotalServiceCost(reservationId);
        System.out.println("\nTotal Add-On Cost for " + reservationId + ": $" + String.format("%.2f", totalAddOnCost));
    }
}
