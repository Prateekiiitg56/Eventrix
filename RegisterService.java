import java.util.Scanner;

public class RegisterService {
    private final Scanner scanner = new Scanner(System.in);
    private final UserDAO userDAO = new UserDAO();

    public void register() {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine();

        System.out.print("Choose a password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        if (userDAO.usernameExists(username)) {
            System.out.println("Username already exists.");
            return;
        }

        if (userDAO.emailExists(email)) {
            System.out.println("Email already registered.");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        boolean success = userDAO.registerUser(user);
        if (success) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed.");
        }
    }
}
