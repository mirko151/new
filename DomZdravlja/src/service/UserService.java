package service;

import model.User;
import java.util.List;

public class UserService {
    private List<User> users;
    private String filePath;

    public UserService(String filePath) {
        this.filePath = filePath;
        this.users = FileService.readUsersFromFile(filePath);
    }

    public void addUser(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                throw new IllegalArgumentException("Username already exists!");
            }
        }
        users.add(user);
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