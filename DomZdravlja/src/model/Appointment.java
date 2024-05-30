package model;

import java.time.LocalDate;
import java.util.List;

public class Appointment {
    private String id;
    private Doctor doctor;
    private Patient patient;
    private LocalDate date;
    private AppointmentStatus status;
    private String therapyDescription;

    public Appointment(String id, Doctor doctor, Patient patient, LocalDate date, AppointmentStatus status, String therapyDescription) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.status = status;
        this.therapyDescription = therapyDescription;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getTherapyDescription() {
        return therapyDescription;
    }

    public void setTherapyDescription(String therapyDescription) {
        this.therapyDescription = therapyDescription;
    }
    public String toCSV() {
        String patientId = patient != null ? patient.getId() : "";
        return String.join(",", id, doctor.getId(), patientId, date.toString(), status.name(), therapyDescription);
    }

    public static Appointment fromCSV(String csv, List<Doctor> doctors, List<Patient> patients) {
        String[] values = csv.split(",");
        if (values.length != 6) {
            throw new IllegalArgumentException("Invalid CSV format for Appointment");
        }
        String id = values[0];
        Doctor doctor = doctors.stream().filter(d -> d.getId().equals(values[1])).findFirst().orElse(null);
        Patient patient = patients.stream().filter(p -> p.getId().equals(values[2])).findFirst().orElse(null);
        LocalDate date = LocalDate.parse(values[3]);
        AppointmentStatus status = AppointmentStatus.valueOf(values[4]);
        String therapyDescription = values[5];

        return new Appointment(id, doctor, patient, date, status, therapyDescription);
    }
}