package gui;

import model.Administrator;
import model.Doctor;
import model.Patient;
import model.User;
import service.AppointmentService;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserService userService;

    public LoginFrame(UserService userService) {
        this.userService = userService;
        initialize();
    }

    private void initialize() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        add(panel);
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            User user = userService.login(username, password);
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

            if (user instanceof Administrator) {
                new AdministratorManagementFrame((Administrator) user, userService, new AppointmentService("appointments.txt", getDoctors(), getPatients())).setVisible(true);
            } else if (user instanceof Patient) {
                new PatientManagementFrame((Patient) user, userService, new AppointmentService("appointments.txt", getDoctors(), getPatients())).setVisible(true);
            } else if (user instanceof Doctor) {
                new DoctorManagementFrame((Doctor) user, userService, new AppointmentService("appointments.txt", getDoctors(), getPatients())).setVisible(true);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private List<Doctor> getDoctors() {
        return userService.getUsers().stream()
                .filter(u -> u instanceof Doctor)
                .map(u -> (Doctor) u)
                .collect(Collectors.toList());
    }
    
    private List<Patient> getPatients() {
        return userService.getUsers().stream()
                .filter(u -> u instanceof Patient)
                .map(u -> (Patient) u)
                .collect(Collectors.toList());
    }
}
