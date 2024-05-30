package gui;

import model.Doctor;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoctorManagementFrame extends JFrame {
    private Doctor doctor;
    private UserService userService;

    public DoctorManagementFrame(Doctor doctor, UserService userService) {
        this.doctor = doctor;
        this.userService = userService;
        initialize();
    }

    private void initialize() {
        setTitle("Doctor Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Welcome, " + doctor.getFirstName() + " " + doctor.getLastName());
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
