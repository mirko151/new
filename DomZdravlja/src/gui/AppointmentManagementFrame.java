package gui;

import model.Appointment;
import model.Doctor;
import model.Patient;
import service.AppointmentService;
import service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AppointmentManagementFrame extends JFrame {
    private AppointmentService appointmentService;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private List<Doctor> doctors;
    private List<Patient> patients;

    public AppointmentManagementFrame(AppointmentService appointmentService, List<Doctor> doctors, List<Patient> patients) {
        this.appointmentService = appointmentService;
        this.doctors = doctors;
        this.patients = patients;
        initialize();
    }

    private void initialize() {
        setTitle("Upravljanje terminima");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Tabela
        String[] columnNames = {"ID", "Doktor", "Pacijent", "Datum", "Status", "Opis terapije"};
        tableModel = new DefaultTableModel(columnNames, 0);
        appointmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        // Učitavanje termina u tabelu
        loadAppointments();

        // Dugmad
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj termin");
        JButton editButton = new JButton("Izmeni termin");
        JButton deleteButton = new JButton("Obriši termin");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Akcije dugmadi
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AppointmentFormFrame(appointmentService, null, doctors, patients, AppointmentManagementFrame.this).setVisible(true);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appointmentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String appointmentId = (String) tableModel.getValueAt(selectedRow, 0);
                    Appointment appointment = appointmentService.getAllAppointments().stream().filter(a -> a.getId().equals(appointmentId)).findFirst().orElse(null);
                    if (appointment != null) {
                        new AppointmentFormFrame(appointmentService, appointment, doctors, patients, AppointmentManagementFrame.this).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(AppointmentManagementFrame.this, "Molimo izaberite termin za izmenu.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appointmentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String appointmentId = (String) tableModel.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(AppointmentManagementFrame.this, "Da li ste sigurni da želite da obrišete ovaj termin?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        appointmentService.getAllAppointments().removeIf(a -> a.getId().equals(appointmentId));
                        appointmentService.saveAppointments();
                        loadAppointments();
                    }
                } else {
                    JOptionPane.showMessageDialog(AppointmentManagementFrame.this, "Molimo izaberite termin za brisanje.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    public void loadAppointments() {
        tableModel.setRowCount(0);  // Očistiti tabelu
        List<Appointment> appointments = appointmentService.getAllAppointments();
        for (Appointment appointment : appointments) {
            Object[] row = new Object[]{
                    appointment.getId(),
                    appointment.getDoctor().getLastName(),
                    appointment.getPatient() != null ? appointment.getPatient().getLastName() : "",
                    appointment.getDate(),
                    appointment.getStatus(),
                    appointment.getTherapyDescription()
            };
            tableModel.addRow(row);
        }
    }

    // Glavna metoda za pokretanje AppointmentManagementFrame
    public static void main(String[] args) {
        String userFilePath = "users.txt";
        String appointmentFilePath = "appointments.txt";

        UserService userService = new UserService(userFilePath);

        // Dobijanje doktora i pacijenata
        List<Doctor> doctors = userService.getUsers().stream()
                .filter(u -> u instanceof Doctor)
                .map(u -> (Doctor) u)
                .toList();
        List<Patient> patients = userService.getUsers().stream()
                .filter(u -> u instanceof Patient)
                .map(u -> (Patient) u)
                .toList();

        AppointmentService appointmentService = new AppointmentService(appointmentFilePath, doctors, patients);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AppointmentManagementFrame(appointmentService, doctors, patients).setVisible(true);
            }
        });
    }
}