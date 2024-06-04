// AppointmentService.java
package service;

import model.Appointment;
import model.Doctor;
import model.Patient;
import java.util.List;

public class AppointmentService {
    private List<Appointment> appointments;
    private String filePath;
    private List<Doctor> doctors;
    private List<Patient> patients;

    public AppointmentService(String filePath, List<Doctor> doctors, List<Patient> patients) {
        this.filePath = filePath;
        this.doctors = doctors;
        this.patients = patients;
        this.appointments = FileService.readAppointmentsFromFile(filePath, doctors, patients);
    }

    public void addAppointment(Appointment appointment) {
        for (Appointment a : appointments) {
            if (a.getId().equals(appointment.getId())) {
                throw new IllegalArgumentException("ID termina već postoji!");
            }
        }
        appointments.add(appointment);
        saveAppointments();
    }

    public void updateAppointment(Appointment appointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId().equals(appointment.getId())) {
                appointments.set(i, appointment);
                saveAppointments();
                return;
            }
        }
        throw new IllegalArgumentException("Termin nije pronađen!");
    }

    public List<Appointment> getAppointmentsForDoctor(Doctor doctor) {
        return appointments.stream().filter(a -> a.getDoctor().equals(doctor)).toList();
    }

    public List<Appointment> getAppointmentsForPatient(Patient patient) {
        return appointments.stream().filter(a -> a.getPatient().equals(patient)).toList();
    }

    public List<Appointment> getAllAppointments() {
        return appointments;
    }

    // Dodavanje metode saveAppointments
    public void saveAppointments() {
        FileService.writeAppointmentsToFile(filePath, appointments);
    }
}
