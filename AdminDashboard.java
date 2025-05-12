import java.util.List;
import java.util.Scanner;

public class AdminDashboard {
    private final EventDAO eventDAO = new EventDAO();
    private final VenueDAO venueDAO = new VenueDAO();
    private final Scanner scanner = new Scanner(System.in);

    public void showDashboard() {
        while (true) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. View All Events");
            System.out.println("2. View Available Venues");
            System.out.println("3. Delete Requested Events");
            System.out.println("4. Logout");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1 -> viewAllEvents();
                case 2 -> viewAvailableVenues();
                case 3 -> deleteRequestedEvents();
                case 4 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void viewAllEvents() {
        List<Event> events = eventDAO.getAllEvents();
        System.out.println("\n--- All Events ---");
        for (Event event : events) {
            System.out.println("ID: " + event.getEventId() +
                               ", Name: " + event.getEventName() +
                               ", Type: " + event.getEventType() +
                               ", Date: " + event.getEventDate() +
                               ", Time: " + event.getEventTime() +
                               ", Venue ID: " + event.getVenueId() +
                               ", User ID: " + event.getUserId());
        }
    }

    private void viewAvailableVenues() {
        List<Venue> venues = venueDAO.getAllVenues();
        System.out.println("\n--- Available Venues ---");
        for (Venue venue : venues) {
            System.out.println("ID: " + venue.getVenueId() +
                               ", Name: " + venue.getVenueName() +
                               ", Type: " + venue.getVenueType() +
                               ", Capacity: " + venue.getCapacity());
        }
    }

    private void deleteRequestedEvents() {
        List<Event> events = eventDAO.getEventsRequestedForDeletion();

        if (events.isEmpty()) {
            System.out.println("No delete requests found.");
            return;
        }

        System.out.println("\n--- Events Requested for Deletion ---");
        for (Event event : events) {
            System.out.println("ID: " + event.getEventId() +
                               ", Name: " + event.getEventName() +
                               ", Date: " + event.getEventDate() +
                               ", Time: " + event.getEventTime());
        }

        System.out.print("Enter Event ID to delete (or 0 to cancel): ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        if (eventId != 0) {
            boolean success = eventDAO.deleteEvent(eventId);
            if (success) {
                System.out.println("Event deleted successfully.");
            } else {
                System.out.println("Failed to delete event.");
            }
        }
    }
}
