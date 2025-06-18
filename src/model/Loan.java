package model;

import java.time.LocalDate;

public class Loan {
    private int loanId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private int readerId;

    public Loan() {
    }

    public Loan(int loanId, LocalDate loanDate, LocalDate dueDate, int readerId) {
        this.loanId = loanId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.readerId = readerId;
    }

    // Getters và setters
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public int getReaderId() { return readerId; }
    public void setReaderId(int readerId) { this.readerId = readerId; }
}
