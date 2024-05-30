package gui;

import model.User;

import javax.swing.*;

public class MainFrame extends JFrame {
    private User loggedInUser;

    public MainFrame(User user) {
        this.loggedInUser = user;
        initialize();
    }

    private void initialize() {
        setTitle("Main Frame");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
        add(welcomeLabel);
    }
}

