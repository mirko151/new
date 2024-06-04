package service;

import model.Doctor;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users;
    private String filePath;

    public UserService(String filePath) {
        this.filePath = filePath;
        this.users = FileService.readUsersFromFile(filePath);
        // Dodajemo debag informacije
        for (User user : users) {
            if (user instanceof Doctor) {
                System.out.println("Loaded doctor: " + user.getId());
            }
        }
    }

    public void addUser(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                throw new IllegalArgumentException("Username already exists!");
            }
        }
        users.add(user);
        saveUsers();
    }

    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                saveUsers();
                return;
            }
        }
        throw new IllegalArgumentException("User not found with ID: " + updatedUser.getId());
    }

    public void saveUsers() {
        FileService.writeUsersToFile(filePath, users);
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new IllegalArgumentException("Invalid username or password!");
    }

    public List<User> getUsers() {
        return users;
    }
}
