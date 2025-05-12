import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final LoginService loginService = new LoginService();
    private static final RegisterService registerService = new RegisterService();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Welcome to the Event Booking System ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  
            switch (choice) {
                case 1 -> handleLogin();
                case 2 -> registerService.register();
                case 3 -> {
                    System.out.println("Exiting system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private static void handleLogin() {
        User user = loginService.login();
        if (user == null) return;

        if (user.getUsername().equalsIgnoreCase("admin")) {
            new AdminDashboard().showDashboard();
        } else {
            new UserDashboard(user.getId()).showDashboard();
        }
    }
}
