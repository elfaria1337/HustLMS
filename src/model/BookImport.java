package model;

import java.time.LocalDate;

public class BookImport {
    private int importId;
    private LocalDate orderDate;
    private LocalDate receiveDate;
    private int supplierId;

    public BookImport() {}

    public BookImport(int importId, LocalDate orderDate, LocalDate receiveDate, int supplierId) {
        this.importId = importId;
        this.orderDate = orderDate;
        this.receiveDate = receiveDate;
        this.supplierId = supplierId;
    }

    // Getters và setters
    public int getImportId() { return importId; }
    public void setImportId(int importId) { this.importId = importId; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public LocalDate getReceiveDate() { return receiveDate; }
    public void setReceiveDate(LocalDate receiveDate) { this.receiveDate = receiveDate; }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
}
