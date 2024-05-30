package gui;

import model.Patient;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PatientManagementFrame extends JFrame {
    private Patient patient;
    private UserService userService;

    public PatientManagementFrame(Patient patient, UserService userService) {
        this.patient = patient;
        this.userService = userService;
        initialize();
    }

    private void initialize() {
        setTitle("Patient Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Welcome, " + patient.getFirstName() + " " + patient.getLastName());
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

