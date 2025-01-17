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
    private boolean deleted;

    public Appointment(String id, Doctor doctor, Patient patient, LocalDate date, AppointmentStatus status, String therapyDescription) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.status = status;
        this.therapyDescription = therapyDescription;
        this.deleted = false;
    }

    public Appointment(String id, Doctor doctor, Patient patient, LocalDate date, AppointmentStatus status, String therapyDescription, boolean deleted) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.status = status;
        this.therapyDescription = therapyDescription;
        this.deleted = deleted;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String toCSV() {
        String patientId = patient != null ? patient.getId() : "";
        return String.join(",", id, doctor.getId(), patientId, date.toString(), status.name(), therapyDescription, String.valueOf(deleted));
    }

    public static Appointment fromCSV(String csv, List<Doctor> doctors, List<Patient> patients) {
        String[] values = csv.split(",");
        if (values.length != 7) {
            throw new IllegalArgumentException("Invalid CSV format for Appointment: " + csv);
        }
        String id = values[0];
        Doctor doctor = doctors.stream().filter(d -> d.getId().equals(values[1])).findFirst().orElse(null);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found for ID: " + values[1]);
        }
        Patient patient = null;
        if (!values[2].isEmpty()) {
            patient = patients.stream().filter(p -> p.getId().equals(values[2])).findFirst().orElse(null);
            if (patient == null) {
                System.out.println("Patient not found for ID: " + values[2]);
            }
        }
        LocalDate date = LocalDate.parse(values[3]);
        AppointmentStatus status = AppointmentStatus.valueOf(values[4]);
        String therapyDescription = values[5];
        boolean deleted = Boolean.parseBoolean(values[6]);

        return new Appointment(id, doctor, patient, date, status, therapyDescription, deleted);
    }
}
