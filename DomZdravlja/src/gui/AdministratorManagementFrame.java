package gui;

import model.Administrator;
import model.Appointment;
import model.Doctor;
import model.Patient;
import model.User;
import service.AppointmentService;
import service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class AdministratorManagementFrame extends JFrame {
    private Administrator administrator;
    private UserService userService;
    private AppointmentService appointmentService;
    private JTable userTable;
    private JTable appointmentTable;
    private DefaultTableModel userTableModel;
    private DefaultTableModel appointmentTableModel;

    public AdministratorManagementFrame(Administrator administrator, UserService userService, AppointmentService appointmentService) {
        this.administrator = administrator;
        this.userService = userService;
        this.appointmentService = appointmentService;
        initialize();
    }

    private void initialize() {
        setTitle("Upravljanje korisnicima i terminima - Administrator");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab za korisnike
        JPanel userPanel = new JPanel(new BorderLayout());

        String[] userColumnNames = {"ID", "Ime", "Prezime", "JMBG", "Pol", "Adresa", "Broj telefona", "Korisničko ime", "Uloga"};
        userTableModel = new DefaultTableModel(userColumnNames, 0);
        userTable = new JTable(userTableModel);
        JScrollPane userScrollPane = new JScrollPane(userTable);

        loadUsers();

        JPanel userButtonPanel = new JPanel();
        JButton addUserButton = new JButton("Dodaj korisnika");
        JButton editUserButton = new JButton("Izmeni korisnika");
        JButton deleteUserButton = new JButton("Obriši korisnika");
        JButton backButton = new JButton("← Nazad");

        userButtonPanel.add(addUserButton);
        userButtonPanel.add(editUserButton);
        userButtonPanel.add(deleteUserButton);
        userButtonPanel.add(backButton);

        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserFormFrame(userService, null, AdministratorManagementFrame.this).setVisible(true);
            }
        });

        editUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String userId = (String) userTableModel.getValueAt(selectedRow, 0);
                    User user = userService.getUsers().stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
                    if (user != null) {
                        new UserFormFrame(userService, user, AdministratorManagementFrame.this).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdministratorManagementFrame.this, "Molimo izaberite korisnika za izmenu.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String userId = (String) userTableModel.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(AdministratorManagementFrame.this, "Da li ste sigurni da želite da obrišete ovog korisnika?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        userService.getUsers().removeIf(u -> u.getId().equals(userId));
                        userService.saveUsers();
                        loadUsers();
                    }
                } else {
                    JOptionPane.showMessageDialog(AdministratorManagementFrame.this, "Molimo izaberite korisnika za brisanje.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame(userService).setVisible(true);
                dispose();
            }
        });

        userPanel.add(userScrollPane, BorderLayout.CENTER);
        userPanel.add(userButtonPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Korisnici", userPanel);

        // Tab za termine
        JPanel appointmentPanel = new JPanel(new BorderLayout());

        String[] appointmentColumnNames = {"ID", "Doktor", "Pacijent", "Datum", "Status", "Opis terapije"};
        appointmentTableModel = new DefaultTableModel(appointmentColumnNames, 0);
        appointmentTable = new JTable(appointmentTableModel);
        JScrollPane appointmentScrollPane = new JScrollPane(appointmentTable);

        loadAppointments();

        JPanel appointmentButtonPanel = new JPanel();
        JButton addAppointmentButton = new JButton("Dodaj termin");
        JButton editAppointmentButton = new JButton("Izmeni termin");
        JButton deleteAppointmentButton = new JButton("Obriši termin");
        JButton appointmentBackButton = new JButton("← Nazad");

        appointmentButtonPanel.add(addAppointmentButton);
        appointmentButtonPanel.add(editAppointmentButton);
        appointmentButtonPanel.add(deleteAppointmentButton);
        appointmentButtonPanel.add(appointmentBackButton);

        addAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Doctor> doctors = userService.getUsers().stream()
                        .filter(u -> u instanceof Doctor)
                        .map(u -> (Doctor) u)
                        .collect(Collectors.toList());
                List<Patient> patients = userService.getUsers().stream()
                        .filter(u -> u instanceof Patient)
                        .map(u -> (Patient) u)
                        .collect(Collectors.toList());
                new AppointmentFormFrame(appointmentService, null, doctors, patients, AdministratorManagementFrame.this).setVisible(true);
            }
        });

        editAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appointmentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String appointmentId = (String) appointmentTableModel.getValueAt(selectedRow, 0);
                    Appointment appointment = appointmentService.getAllAppointments().stream().filter(a -> a.getId().equals(appointmentId)).findFirst().orElse(null);
                    if (appointment != null) {
                        List<Doctor> doctors = userService.getUsers().stream()
                                .filter(u -> u instanceof Doctor)
                                .map(u -> (Doctor) u)
                                .collect(Collectors.toList());
                        List<Patient> patients = userService.getUsers().stream()
                                .filter(u -> u instanceof Patient)
                                .map(u -> (Patient) u)
                                .collect(Collectors.toList());
                        new AppointmentFormFrame(appointmentService, appointment, doctors, patients, AdministratorManagementFrame.this).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdministratorManagementFrame.this, "Molimo izaberite termin za izmenu.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appointmentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String appointmentId = (String) appointmentTableModel.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(AdministratorManagementFrame.this, "Da li ste sigurni da želite da obrišete ovaj termin?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        appointmentService.getAllAppointments().removeIf(a -> a.getId().equals(appointmentId));
                        appointmentService.saveAppointments();
                        loadAppointments();
                    }
                } else {
                    JOptionPane.showMessageDialog(AdministratorManagementFrame.this, "Molimo izaberite termin za brisanje.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        appointmentBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame(userService).setVisible(true);
                dispose();
            }
        });

        appointmentPanel.add(appointmentScrollPane, BorderLayout.CENTER);
        appointmentPanel.add(appointmentButtonPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Termini", appointmentPanel);

        add(tabbedPane);
    }

    public void loadUsers() {
        userTableModel.setRowCount(0);  // Očistiti tabelu
        List<User> users = userService.getUsers();
        for (User user : users) {
            Object[] row = new Object[]{
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getJmbg(),
                    user.getGender(),
                    user.getAddress(),
                    user.getPhoneNumber(),
                    user.getUsername(),
                    user.getRole()
            };
            userTableModel.addRow(row);
        }
    }

    public void loadAppointments() {
        appointmentTableModel.setRowCount(0);  // Očistiti tabelu
        List<Appointment> appointments = appointmentService.getAllAppointments();
        for (Appointment appointment : appointments) {
            Object[] row = new Object[]{
                    appointment.getId(),
                    appointment.getDoctor() != null ? appointment.getDoctor().getLastName() : "",
                    appointment.getPatient() != null ? appointment.getPatient().getLastName() : "",
                    appointment.getDate(),
                    appointment.getStatus(),
                    appointment.getTherapyDescription()
            };
            appointmentTableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        String userFilePath = "users.txt";
        String appointmentFilePath = "appointments.txt";

        UserService userService = new UserService(userFilePath);

        // Dobijanje doktora i pacijenata
        List<Doctor> doctors = userService.getUsers().stream()
                .filter(u -> u instanceof Doctor)
                .map(u -> (Doctor) u)
                .collect(Collectors.toList());
        List<Patient> patients = userService.getUsers().stream()
                .filter(u -> u instanceof Patient)
                .map(u -> (Patient) u)
                .collect(Collectors.toList());

        AppointmentService appointmentService = new AppointmentService(appointmentFilePath, doctors, patients);

        // Dodajemo testne korisnike ako ih nema ()
        if (userService.getUsers().isEmpty()) {
            userService.addUser(new Administrator("1", "Admin", "Adminovic", "1234567890123", "M", "Adresa 1", "123456789", "admin", "admin"));
            userService.addUser(new Doctor("2", "Doctor", "Doctorovic", "1234567890124", "F", "Adresa 2", "123456780", "doctor", "doctor"));
            userService.addUser(new Patient("3", "Patient", "Patientovic", "1234567890125", "F", "Adresa 3", "123456781", "patient", "patient"));
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame(userService).setVisible(true);
            }
        });
    }
}
