package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Inventory;

public class InventoryFormController {

    @FXML private TextField locationNameField;

    private Stage dialogStage;
    private Inventory inventory;
    private boolean saveClicked = false;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        if (inventory != null) {
            locationNameField.setText(inventory.getLocationName());
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (inventory == null) {
                inventory = new Inventory();
            }
            inventory.setLocationName(locationNameField.getText().trim());

            saveClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (locationNameField.getText() == null || locationNameField.getText().trim().isEmpty()) {
            errorMessage += "Tên vị trí không được để trống!\n";
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Lỗi nhập liệu");
            alert.setHeaderText("Thông tin kho sách không hợp lệ");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

        return true;
    }
}
