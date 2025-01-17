package model;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends User {
    private List<Appointment> appointments;

    public Doctor(String id, String firstName, String lastName, String jmbg, String gender, String address, String phoneNumber, String username, String password) {
        super(id, firstName, lastName, jmbg, gender, address, phoneNumber, username, password, UserRole.DOCTOR);
        this.appointments = new ArrayList<>();
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}