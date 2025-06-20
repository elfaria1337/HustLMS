package model;

import java.math.BigDecimal;

public class BookImportDetail {
    private int importDetailId;
    private int quantity;
    private BigDecimal pricePerUnit;
    private int titleId;
    private int importId;

    public BookImportDetail() {}

    public BookImportDetail(int importDetailId, int quantity, BigDecimal pricePerUnit, int titleId, int importId) {
        this.importDetailId = importDetailId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.titleId = titleId;
        this.importId = importId;
    }

    // Getters và setters
    public int getImportDetailId() { return importDetailId; }
    public void setImportDetailId(int importDetailId) { this.importDetailId = importDetailId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }

    public int getTitleId() { return titleId; }
    public void setTitleId(int titleId) { this.titleId = titleId; }

    public int getImportId() { return importId; }
    public void setImportId(int importId) { this.importId = importId; }
}
