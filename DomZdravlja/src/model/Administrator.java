package model;

public class Administrator extends User {

    public Administrator(String id, String firstName, String lastName, String jmbg, String gender, String address, String phoneNumber, String username, String password) {
        super(id, firstName, lastName, jmbg, gender, address, phoneNumber, username, password, UserRole.ADMINISTRATOR);
    }

   
}
