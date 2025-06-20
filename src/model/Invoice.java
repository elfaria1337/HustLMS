package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice {
    private int invoiceId;
    private BigDecimal amount;
    private String paymentMethod;  // Ví dụ: Tiền mặt, chuyển khoản, ví điện tử,...
    private LocalDate paymentDate;
    private int readerId;
    private Integer fineId;  // có thể null nếu không phải thanh toán phạt

    public Invoice() {}

    public Invoice(int invoiceId, BigDecimal amount, String paymentMethod, LocalDate paymentDate, int readerId, Integer fineId) {
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.readerId = readerId;
        this.fineId = fineId;
    }

    // Getters và setters
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public LocalDate getInvoiceDate() {
        return getPaymentDate();
    }

    public int getReaderId() { return readerId; }
    public void setReaderId(int readerId) { this.readerId = readerId; }

    public Integer getFineId() { return fineId; }
    public void setFineId(Integer fineId) { this.fineId = fineId; }
}
