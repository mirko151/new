package model;

public abstract class User {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String jmbg;
    protected String gender;
    protected String address;
    protected String phoneNumber;
    protected String username;
    protected String password;
    protected UserRole role;
    protected boolean deleted;

    public User(String id, String firstName, String lastName, String jmbg, String gender, String address, String phoneNumber, String username, String password, UserRole role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jmbg = jmbg;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.role = role;
        this.deleted = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String toCSV() {
        return String.join(",", id, firstName, lastName, jmbg, gender, address, phoneNumber, username, password, role.name(), String.valueOf(deleted));
    }

    public static User fromCSV(String csv) {
        String[] values = csv.split(",");
        if (values.length != 10 && values.length != 11) {
            throw new IllegalArgumentException("Invalid CSV format for User");
        }
        String id = values[0];
        String firstName = values[1];
        String lastName = values[2];
        String jmbg = values[3];
        String gender = values[4];
        String address = values[5];
        String phoneNumber = values[6];
        String username = values[7];
        String password = values[8];
        UserRole role = UserRole.valueOf(values[9]);
        boolean deleted = values.length == 11 ? Boolean.parseBoolean(values[10]) : false;

        User user;
        switch (role) {
            case ADMINISTRATOR:
                user = new Administrator(id, firstName, lastName, jmbg, gender, address, phoneNumber, username, password);
                break;
            case PATIENT:
                user = new Patient(id, firstName, lastName, jmbg, gender, address, phoneNumber, username, password);
                break;
            case DOCTOR:
                user = new Doctor(id, firstName, lastName, jmbg, gender, address, phoneNumber, username, password);
                break;
            default:
                throw new IllegalArgumentException("Unknown user role");
        }
        user.setDeleted(deleted);
        return user;
    }
}
