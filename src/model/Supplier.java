package model;

public class Supplier {
    private int supplierId;
    private String companyName;
    private String phone;
    private String email;
    private String address;
    private String contactPerson;

    public Supplier() {}

    public Supplier(int supplierId, String companyName, String phone, String email, String address, String contactPerson) {
        this.supplierId = supplierId;
        this.companyName = companyName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.contactPerson = contactPerson;
    }

    // Getters và setters
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
}
