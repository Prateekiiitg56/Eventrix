import java.sql.Time;
import java.util.List;
import java.util.Scanner;

public class UserDashboard {
    private final Scanner scanner = new Scanner(System.in);
    private final EventDAO eventDAO = new EventDAO();
    private final VenueDAO venueDAO = new VenueDAO();

    private final int userId;

    public UserDashboard(int userId) {
        this.userId = userId;
    }

    public void showDashboard() {
        while (true) {
            System.out.println("\n--- User Dashboard ---");
            System.out.println("1. Register Event");
            System.out.println("2. View My Events");
            System.out.println("3. Request to Delete Event");
            System.out.println("4. Logout");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1 -> registerEvent();
                case 2 -> viewMyEvents();
                case 3 -> requestDeleteEvent();
                case 4 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void registerEvent() {
        System.out.print("Enter Event Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Event Type: ");
        String type = scanner.nextLine();

        System.out.print("Enter Event Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        List<Venue> availableVenues = venueDAO.getAvailableVenuesByType(type, date);

        if (availableVenues.isEmpty()) {
            System.out.println("No available venues found for this type and date.");
            return;
        }

        System.out.println("Available Venues:");
        for (Venue venue : availableVenues) {
            System.out.println("ID: " + venue.getVenueId() +
                               ", Name: " + venue.getVenueName());
        }

        System.out.print("Select Venue ID: ");
        int venueId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Event Time (HH:MM:SS): ");
        String timeStr = scanner.nextLine();
        Time time = Time.valueOf(timeStr);

        boolean isAvailable = venueDAO.isVenueAvailable(venueId, java.sql.Date.valueOf(date), time);

        if (!isAvailable) {
            System.out.println("Selected venue is not available at that time.");
            return;
        }

        Event event = new Event();
        event.setEventName(name);
        event.setEventType(type);
        event.setEventDate(date);
        event.setEventTime(timeStr);
        event.setVenueId(venueId);
        event.setUserId(userId);

        boolean success = eventDAO.addEvent(event);

        if (success) {
            System.out.println("Event registered successfully!");
        } else {
            System.out.println("Failed to register event.");
        }
    }

    private void viewMyEvents() {
        List<Event> events = eventDAO.getEventsByUserId(userId);

        System.out.println("\n--- My Events ---");
        for (Event event : events) {
            System.out.println("ID: " + event.getEventId() +
                               ", Name: " + event.getEventName() +
                               ", Type: " + event.getEventType() +
                               ", Date: " + event.getEventDate() +
                               ", Time: " + event.getEventTime() +
                               ", Venue ID: " + event.getVenueId());
        }
    }

    private void requestDeleteEvent() {
        viewMyEvents();

        System.out.print("Enter Event ID to request deletion: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        boolean success = eventDAO.requestEventDeletion(eventId);

        if (success) {
            System.out.println("Event deletion request sent.");
        } else {
            System.out.println("Failed to request deletion.");
        }
    }
}
