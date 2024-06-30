package gui;

import model.Administrator;
import model.Doctor;
import model.Patient;
import model.User;
import model.UserRole;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserFormFrame extends JFrame {
    private UserService userService;
    private User user;
    private JFrame parentFrame;

    private JTextField idField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField jmbgField;
    private JComboBox<String> genderComboBox;
    private JTextField addressField;
    private JTextField phoneNumberField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<UserRole> roleComboBox;

    public UserFormFrame(UserService userService, User user, JFrame parentFrame) {
        this.userService = userService;
        this.user = user;
        this.parentFrame = parentFrame;

        initialize();
    }

    private void initialize() {
        setTitle(user == null ? "Dodaj korisnika" : "Izmeni korisnika");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(10, 2));

        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Ime:"));
        firstNameField = new JTextField();
        panel.add(firstNameField);

        panel.add(new JLabel("Prezime:"));
        lastNameField = new JTextField();
        panel.add(lastNameField);

        panel.add(new JLabel("JMBG:"));
        jmbgField = new JTextField();
        panel.add(jmbgField);

        panel.add(new JLabel("Pol:"));
        genderComboBox = new JComboBox<>(new String[]{"M", "F"});
        panel.add(genderComboBox);

        panel.add(new JLabel("Adresa:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Broj telefona:"));
        phoneNumberField = new JTextField();
        panel.add(phoneNumberField);

        panel.add(new JLabel("Korisničko ime:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Lozinka:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Uloga:"));
        roleComboBox = new JComboBox<>(UserRole.values());
        panel.add(roleComboBox);

        JButton saveButton = new JButton("Sačuvaj");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUser();
            }
        });
        panel.add(saveButton);

        if (user != null) {
            loadUserDetails();
        }

        add(panel);
    }

    private void loadUserDetails() {
        idField.setText(user.getId());
        idField.setEnabled(false);
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        jmbgField.setText(user.getJmbg());
        genderComboBox.setSelectedItem(user.getGender());
        addressField.setText(user.getAddress());
        phoneNumberField.setText(user.getPhoneNumber());
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        roleComboBox.setSelectedItem(user.getRole());
    }

    private void saveUser() {
        try {
            String id = idField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String jmbg = jmbgField.getText();
            String gender = (String) genderComboBox.getSelectedItem();
            String address = addressField.getText();
            String phoneNumber = phoneNumberField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            UserRole role = (UserRole) roleComboBox.getSelectedItem();

            if (firstName.isEmpty() || lastName.isEmpty() || jmbg.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || username.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("Sva polja su obavezna!");
            }

            if (user == null) {
                User newUser;
                switch (role) {
                    case ADMINISTRATOR:
                        newUser = new Administrator(id, firstName, lastName, jmbg, gender, address, phoneNumber, username, password);
                        break;
                    case DOCTOR:
                        newUser = new Doctor(id, firstName, lastName, jmbg, gender, address, phoneNumber, username, password);
                        break;
                    case PATIENT:
                        newUser = new Patient(id, firstName, lastName, jmbg, gender, address, phoneNumber, username, password);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown user role");
                }
                userService.addUser(newUser);
            } else {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setJmbg(jmbg);
                user.setGender(gender);
                user.setAddress(address);
                user.setPhoneNumber(phoneNumber);
                user.setUsername(username);
                user.setPassword(password);
                user.setRole(role);
                userService.updateUser(user);
            }

            if (parentFrame instanceof AdministratorManagementFrame) {
                ((AdministratorManagementFrame) parentFrame).loadUsers();
            }

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška pri čuvanju korisnika: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}
