package model;

public class Account {
    private int accountId;
    private String username;
    private String password;
    private String role;      // ví dụ: "reader", "staff", "admin"
    private String status;    // ví dụ: "active", "locked"
    private Integer readerId; // có thể null nếu tài khoản không phải reader
    private Integer staffId;  // có thể null nếu tài khoản không phải staff

    public Account() {}

    public Account(int accountId, String username, String password, String role, String status, Integer readerId, Integer staffId) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.readerId = readerId;
        this.staffId = staffId;
    }

    // Getters và setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getReaderId() { return readerId; }
    public void setReaderId(Integer readerId) { this.readerId = readerId; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }
}
