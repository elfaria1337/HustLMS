package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Fine {
    private int fineId;
    private String reason;
    private BigDecimal amount;
    private LocalDate issueDate;
    private int readerId;

    public Fine() {}

    public Fine(int fineId, String reason, BigDecimal amount, LocalDate issueDate, int readerId) {
        this.fineId = fineId;
        this.reason = reason;
        this.amount = amount;
        this.issueDate = issueDate;
        this.readerId = readerId;
    }

    // Getters và setters
    public int getFineId() { return fineId; }
    public void setFineId(int fineId) { this.fineId = fineId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getFineDate() {
        return getIssueDate();
    }

    public int getReaderId() { return readerId; }
    public void setReaderId(int readerId) { this.readerId = readerId; }
}
