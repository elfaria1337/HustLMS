package model;

public class Inventory {
    private int inventoryId;
    private String locationName;

    public Inventory() {
    }

    public Inventory(int inventoryId, String locationName) {
        this.inventoryId = inventoryId;
        this.locationName = locationName;
    }

    // Getters và setters
    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
}
