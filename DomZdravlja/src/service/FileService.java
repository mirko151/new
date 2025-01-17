package service;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    // Metode za rad sa korisnicima
    public static void writeUsersToFile(String filePath, List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users) {
                writer.write(user.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> readUsersFromFile(String filePath) {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    User user = User.fromCSV(line);
                    users.add(user);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error parsing user from CSV: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Metode za rad sa terminima
    public static void writeAppointmentsToFile(String filePath, List<Appointment> appointments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Appointment appointment : appointments) {
                writer.write(appointment.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Appointment> readAppointmentsFromFile(String filePath, List<Doctor> doctors, List<Patient> patients) {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Appointment appointment = Appointment.fromCSV(line, doctors, patients);
                    appointments.add(appointment);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error parsing appointment from CSV: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointments;
    }
}
