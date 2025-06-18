package model;

public class BookCopy {
    private int copyId;
    private String state;         // trạng thái: sẵn sàng, mượn, hỏng, ...
    private int inventoryId;      // kho chứa sách
    private int titleId;          // đầu sách liên kết

    public BookCopy() {
    }

    public BookCopy(int copyId, String state, int inventoryId, int titleId) {
        this.copyId = copyId;
        this.state = state;
        this.inventoryId = inventoryId;
        this.titleId = titleId;
    }

    // Getters và setters
    public int getCopyId() { return copyId; }
    public void setCopyId(int copyId) { this.copyId = copyId; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }

    public int getTitleId() { return titleId; }
    public void setTitleId(int titleId) { this.titleId = titleId; }
}
