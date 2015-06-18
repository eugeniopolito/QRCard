package it.ep.qrsafe;

/**
 * Created by eugenio on 05/06/15.
 */
public class Contact {

    private String name;
    private String address;
    private String organization;
    private String phone;
    private String email;
    private String notes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Name: " + name + "; Address: " + address + "; Org: " + organization + "; Tel.: " + phone + "; Email: " + email + "; Notes: " + notes;
    }


}
