package service;

import model.Appointment;
import model.Doctor;
import model.Patient;

import java.util.ArrayList;
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
                throw new IllegalArgumentException("Appointment ID already exists!");
            }
        }
        appointments.add(appointment);
        FileService.writeAppointmentsToFile(filePath, appointments);
    }

    public void updateAppointment(Appointment appointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId().equals(appointment.getId())) {
                appointments.set(i, appointment);
                FileService.writeAppointmentsToFile(filePath, appointments);
                return;
            }
        }
        throw new IllegalArgumentException("Appointment not found!");
    }

    public List<Appointment> getAppointmentsForDoctor(Doctor doctor) {
        List<Appointment> doctorAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().equals(doctor)) {
                doctorAppointments.add(appointment);
            }
        }
        return doctorAppointments;
    }

    public List<Appointment> getAppointmentsForPatient(Patient patient) {
        List<Appointment> patientAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().equals(patient)) {
                patientAppointments.add(appointment);
            }
        }
        return patientAppointments;
    }

    public List<Appointment> getAllAppointments() {
        return appointments;
    }
}
