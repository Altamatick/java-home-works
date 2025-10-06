package app;

public class User {
    private String name;
    private String email;
    private Address address;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getFullInfo() {
        String info = "User: " + name + " (" + email + ")";
        if (address != null) {
            info += "\nAddress: " + address.toString();
        } else {
            info += "\nAddress: Not specified";
        }
        return info;
    }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}
