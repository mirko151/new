package model;

import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
    private List<Appointment> medicalRecord;

    public Patient(String id, String firstName, String lastName, String jmbg, String gender, String address, String phoneNumber, String username, String password) {
        super(id, firstName, lastName, jmbg, gender, address, phoneNumber, username, password, UserRole.PATIENT);
        this.medicalRecord = new ArrayList<>();
    }

    public List<Appointment> getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(List<Appointment> medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    // Dodatne metode specifične za pacijenta mogu se dodati ovde
}
