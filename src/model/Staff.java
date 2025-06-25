package model;

import java.time.LocalDate;

public class Staff {
    private int staffId;
    private String fullName;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    private LocalDate hireDate;

    public Staff() {}

    public Staff(int staffId, String fullName, LocalDate birthDate, String address,
                 String phone, String email, LocalDate hireDate) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.hireDate = hireDate;
    }

    // Getters và setters
    public int getStaffId() {
        return staffId;
    }
    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
    public LocalDate getHireDate() {
        return hireDate;
    }
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
}
