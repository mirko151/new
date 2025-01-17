// AppointmentFormFrame.java
package gui;

import model.Appointment;
import model.AppointmentStatus;
import model.Doctor;
import model.Patient;
import service.AppointmentService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentFormFrame extends JFrame {
    private AppointmentService appointmentService;
    private Appointment appointment;
    private List<Doctor> doctors;
    private List<Patient> patients;
    private JFrame parentFrame;

    private JTextField idField;
    private JComboBox<Doctor> doctorComboBox;
    private JComboBox<Patient> patientComboBox;
    private JTextField dateField;
    private JComboBox<AppointmentStatus> statusComboBox;
    private JTextArea therapyDescriptionArea;

    public AppointmentFormFrame(AppointmentService appointmentService, Appointment appointment, List<Doctor> doctors, List<Patient> patients, JFrame parentFrame) {
        this.appointmentService = appointmentService;
        this.appointment = appointment;
        this.doctors = doctors;
        this.patients = patients;
        this.parentFrame = parentFrame;

        initialize();
    }

    private void initialize() {
        setTitle(appointment == null ? "Dodaj termin" : "Izmeni termin");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Doktor:"));
        doctorComboBox = new JComboBox<>(doctors.toArray(new Doctor[0]));
        panel.add(doctorComboBox);

        panel.add(new JLabel("Pacijent:"));
        patientComboBox = new JComboBox<>(patients.toArray(new Patient[0]));
        panel.add(patientComboBox);

        panel.add(new JLabel("Datum (YYYY-MM-DD):"));
        dateField = new JTextField();
        panel.add(dateField);

        panel.add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(AppointmentStatus.values());
        panel.add(statusComboBox);

        panel.add(new JLabel("Opis terapije:"));
        therapyDescriptionArea = new JTextArea();
        panel.add(new JScrollPane(therapyDescriptionArea));

        JButton saveButton = new JButton("Sačuvaj");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAppointment();
            }
        });
        panel.add(saveButton);

        if (appointment != null) {
            loadAppointmentDetails();
        }

        add(panel);
    }

    private void loadAppointmentDetails() {
        idField.setText(appointment.getId());
        idField.setEnabled(false);
        doctorComboBox.setSelectedItem(appointment.getDoctor());
        patientComboBox.setSelectedItem(appointment.getPatient());
        dateField.setText(appointment.getDate().toString());
        statusComboBox.setSelectedItem(appointment.getStatus());
        therapyDescriptionArea.setText(appointment.getTherapyDescription());
    }

    private void saveAppointment() {
        try {
            String id = idField.getText();
            Doctor doctor = (Doctor) doctorComboBox.getSelectedItem();
            Patient patient = (Patient) patientComboBox.getSelectedItem();
            LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
            AppointmentStatus status = (AppointmentStatus) statusComboBox.getSelectedItem();
            String therapyDescription = therapyDescriptionArea.getText();

            if (appointment == null) {
                Appointment newAppointment = new Appointment(id, doctor, patient, date, status, therapyDescription);
                appointmentService.addAppointment(newAppointment);
            } else {
                appointment.setDoctor(doctor);
                appointment.setPatient(patient);
                appointment.setDate(date);
                appointment.setStatus(status);
                appointment.setTherapyDescription(therapyDescription);
                appointmentService.updateAppointment(appointment);
            }

            if (parentFrame instanceof DoctorManagementFrame) {
                ((DoctorManagementFrame) parentFrame).loadAppointments();
            } else if (parentFrame instanceof PatientManagementFrame) {
                ((PatientManagementFrame) parentFrame).loadAppointments();
            } else if (parentFrame instanceof AppointmentManagementFrame) {
                ((AppointmentManagementFrame) parentFrame).loadAppointments();
            }

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška pri čuvanju termina: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}
