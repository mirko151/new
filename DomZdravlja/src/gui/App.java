package gui;

import model.Administrator;
import service.UserService;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        String userFilePath = "users.txt";

        UserService userService = new UserService(userFilePath);

        if (userService.getUsers().isEmpty()) {
            userService.addUser(new Administrator("1", "Admin", "Adminovic", "1234567890123", "M", "Adresa 1", "123456789", "admin", "admin"));
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame(userService).setVisible(true);
            }
        });
    }
}
