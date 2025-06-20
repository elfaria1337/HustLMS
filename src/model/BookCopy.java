package model;

public class BookCopy {
    private int copyId;
    private String state;        
    private int inventoryId;     
    private int titleId;         
    private String locationName; 

    public BookCopy() {
    }

    public BookCopy(int copyId, String state, int inventoryId, int titleId, String locationName) {
        this.copyId = copyId;
        this.state = state;
        this.inventoryId = inventoryId;
        this.titleId = titleId;
        this.locationName = locationName;
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

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
}
