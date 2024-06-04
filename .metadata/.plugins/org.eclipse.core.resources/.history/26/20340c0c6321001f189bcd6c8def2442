package gui;

import model.Administrator;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdministratorManagementFrame extends JFrame {
    private Administrator administrator;
    private UserService userService;

    public AdministratorManagementFrame(Administrator administrator, UserService userService) {
        this.administrator = administrator;
        this.userService = userService;
        initialize();
    }

    private void initialize() {
        setTitle("Administrator Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Welcome, " + administrator.getFirstName() + " " + administrator.getLastName());
        panel.add(label, BorderLayout.CENTER);

        JButton backButton = new JButton("← Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame(userService).setVisible(true);
                dispose();
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        add(panel);
    }
}


