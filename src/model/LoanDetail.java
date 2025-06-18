package model;

import java.time.LocalDate;

public class LoanDetail {
    private int loanDetailId;
    private LocalDate returnDate; // có thể null nếu chưa trả
    private int loanId;
    private int copyId;

    public LoanDetail() {
    }

    public LoanDetail(int loanDetailId, LocalDate returnDate, int loanId, int copyId) {
        this.loanDetailId = loanDetailId;
        this.returnDate = returnDate;
        this.loanId = loanId;
        this.copyId = copyId;
    }

    // Getters và setters
    public int getLoanDetailId() { return loanDetailId; }
    public void setLoanDetailId(int loanDetailId) { this.loanDetailId = loanDetailId; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public int getCopyId() { return copyId; }
    public void setCopyId(int copyId) { this.copyId = copyId; }
}
