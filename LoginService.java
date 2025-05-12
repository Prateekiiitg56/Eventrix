import java.util.Scanner;

public class LoginService {
    private final Scanner scanner = new Scanner(System.in);
    private final UserDAO userDAO = new UserDAO();

    public User login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDAO.loginUser(email, password);
        if (user != null) {
            System.out.println("Login successful! Welcome " + user.getUsername());
            return user;
        } else {
            System.out.println("Invalid credentials. Please try again.");
            return null;
        }
    }
}
