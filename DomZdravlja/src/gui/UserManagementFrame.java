// UserManagementFrame.java
package gui;

import model.Administrator;
import model.User;
import service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagementFrame extends JFrame {
    private UserService userService;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagementFrame(UserService userService) {
        this.userService = userService;
        initialize();
    }

    private void initialize() {
        setTitle("Upravljanje korisnicima");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Tabela
        String[] columnNames = {"ID", "Ime", "Prezime", "JMBG", "Pol", "Adresa", "Broj telefona", "Korisničko ime", "Uloga"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Učitavanje korisnika u tabelu
        loadUsers();

        // Dugmad
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj korisnika");
        JButton editButton = new JButton("Izmeni korisnika");
        JButton deleteButton = new JButton("Obriši korisnika");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Akcije dugmadi
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserFormFrame(userService, null, UserManagementFrame.this).setVisible(true);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String userId = (String) tableModel.getValueAt(selectedRow, 0);
                    User user = userService.getUsers().stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
                    if (user != null) {
                        new UserFormFrame(userService, user, UserManagementFrame.this).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(UserManagementFrame.this, "Molimo izaberite korisnika za izmenu.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String userId = (String) tableModel.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(UserManagementFrame.this, "Da li ste sigurni da želite da obrišete ovog korisnika?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        userService.getUsers().removeIf(u -> u.getId().equals(userId));
                        userService.saveUsers();
                        loadUsers();
                    }
                } else {
                    JOptionPane.showMessageDialog(UserManagementFrame.this, "Molimo izaberite korisnika za brisanje.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    public void loadUsers() {
        tableModel.setRowCount(0);  // Očistiti tabelu
        List<User> users = userService.getUsers();
        for (User user : users) {
            Object[] row = new Object[]{
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getJmbg(),
                    user.getGender(),
                    user.getAddress(),
                    user.getPhoneNumber(),
                    user.getUsername(),
                    user.getRole()
            };
            tableModel.addRow(row);
        }
    }

    // Glavna metoda za pokretanje UserManagementFrame
    public static void main(String[] args) {
        String userFilePath = "users.txt";

        // Kreiraj UserService sa putanjom do fajla
        UserService userService = new UserService(userFilePath);

        // Dodajemo testne korisnike ako ih nema (u stvarnom slučaju, korisnici bi se učitali iz fajla)
        if (userService.getUsers().isEmpty()) {
            userService.addUser(new Administrator("1", "Admin", "Adminovic", "1234567890123", "M", "Adresa 1", "123456789", "admin", "admin"));
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserManagementFrame(userService).setVisible(true);
            }
        });
    }
}
