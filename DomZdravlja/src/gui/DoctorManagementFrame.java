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
import java.util.stream.Collectors;

public class DoctorManagementFrame extends JFrame {
    private Doctor doctor;
    private UserService userService;
    private AppointmentService appointmentService;
    private JTable appointmentTable;
    private DefaultTableModel appointmentTableModel;

    public DoctorManagementFrame(Doctor doctor, UserService userService, AppointmentService appointmentService) {
        this.doctor = doctor;
        this.userService = userService;
        this.appointmentService = appointmentService;
        initialize();
    }

    private void initialize() {
        setTitle("Upravljanje doktorom");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Dobrodošli, " + doctor.getFirstName() + " " + doctor.getLastName());
        panel.add(label, BorderLayout.NORTH);

        // Tabela termina
        String[] columnNames = {"ID", "Pacijent", "Datum", "Status", "Opis terapije"};
        appointmentTableModel = new DefaultTableModel(columnNames, 0);
        appointmentTable = new JTable(appointmentTableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        loadAppointments();

        panel.add(scrollPane, BorderLayout.CENTER);

        // Dugmad
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj termin");
        JButton editButton = new JButton("Izmeni termin");
        JButton deleteButton = new JButton("Obriši termin");
        JButton backButton = new JButton("← Nazad");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AppointmentFormFrame(appointmentService, null, getDoctors(), getPatients(), DoctorManagementFrame.this).setVisible(true);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appointmentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String appointmentId = (String) appointmentTableModel.getValueAt(selectedRow, 0);
                    Appointment appointment = appointmentService.getAllAppointments().stream().filter(a -> a.getId().equals(appointmentId)).findFirst().orElse(null);
                    if (appointment != null) {
                        new AppointmentFormFrame(appointmentService, appointment, getDoctors(), getPatients(), DoctorManagementFrame.this).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(DoctorManagementFrame.this, "Molimo izaberite termin za izmenu.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appointmentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String appointmentId = (String) appointmentTableModel.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(DoctorManagementFrame.this, "Da li ste sigurni da želite da obrišete ovaj termin?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        appointmentService.getAllAppointments().removeIf(a -> a.getId().equals(appointmentId));
                        appointmentService.saveAppointments();
                        loadAppointments();
                    }
                } else {
                    JOptionPane.showMessageDialog(DoctorManagementFrame.this, "Molimo izaberite termin za brisanje.", "Greška", JOptionPane.ERROR_MESSAGE);
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

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
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

    public void loadAppointments() {
        appointmentTableModel.setRowCount(0);  // Očistiti tabelu
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(doctor);
        for (Appointment appointment : appointments) {
            Object[] row = new Object[]{
                    appointment.getId(),
                    appointment.getPatient() != null ? appointment.getPatient().getLastName() : "",
                    appointment.getDate(),
                    appointment.getStatus(),
                    appointment.getTherapyDescription()
            };
            appointmentTableModel.addRow(row);
        }
    }

    // Glavna metoda za pokretanje frejma
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

        // Pronalaženje doktora za testiranje
        Doctor doctor = doctors.get(0);  // Pretpostavimo da postoji bar jedan doktor

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DoctorManagementFrame(doctor, userService, appointmentService).setVisible(true);
            }
        });
    }
}
