package gui;

import model.Appointment;
import model.AppointmentStatus;
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

public class PatientManagementFrame extends JFrame {
    private Patient patient;
    private UserService userService;
    private AppointmentService appointmentService;
    private JTable appointmentTable;
    private DefaultTableModel appointmentTableModel;

    public PatientManagementFrame(Patient patient, UserService userService, AppointmentService appointmentService) {
        this.patient = patient;
        this.userService = userService;
        this.appointmentService = appointmentService;
        initialize();
    }

    private void initialize() {
        setTitle("Upravljanje pacijentom");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Dobrodošli, " + patient.getFirstName() + " " + patient.getLastName());
        panel.add(label, BorderLayout.NORTH);

        // Tabela termina
        String[] columnNames = {"ID", "Doktor", "Datum", "Status", "Opis terapije"};
        appointmentTableModel = new DefaultTableModel(columnNames, 0);
        appointmentTable = new JTable(appointmentTableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        loadAppointments();

        panel.add(scrollPane, BorderLayout.CENTER);

        // Dugmad
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Zakaži termin");
        JButton cancelButton = new JButton("Otkaži termin");
        JButton backButton = new JButton("← Nazad");

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AppointmentFormFrame(appointmentService, null, getDoctors(), getPatients(), PatientManagementFrame.this).setVisible(true);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appointmentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String appointmentId = (String) appointmentTableModel.getValueAt(selectedRow, 0);
                    Appointment appointment = appointmentService.getAllAppointments().stream().filter(a -> a.getId().equals(appointmentId)).findFirst().orElse(null);
                    if (appointment != null && appointment.getStatus() == AppointmentStatus.SCHEDULED) {
                        appointment.setStatus(AppointmentStatus.CANCELLED);
                        appointmentService.updateAppointment(appointment);
                        loadAppointments();
                    } else {
                        JOptionPane.showMessageDialog(PatientManagementFrame.this, "Termin se može otkazati samo ako je zakazan.", "Greška", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(PatientManagementFrame.this, "Molimo izaberite termin za otkazivanje.", "Greška", JOptionPane.ERROR_MESSAGE);
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
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patient);
        for (Appointment appointment : appointments) {
            Object[] row = new Object[]{
                    appointment.getId(),
                    appointment.getDoctor() != null ? appointment.getDoctor().getLastName() : "",
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

        // Pronalaženje pacijenta za testiranje
        Patient patient = patients.get(0);  // Pretpostavimo da postoji bar jedan pacijent

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PatientManagementFrame(patient, userService, appointmentService).setVisible(true);
            }
        });
    }
}
