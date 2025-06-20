package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Supplier;

public class SupplierFormController {

    @FXML private TextField companyNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private TextField contactPersonField;

    private Stage dialogStage;
    private Supplier supplier;
    private boolean saveClicked = false;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        if (supplier != null) {
            companyNameField.setText(supplier.getCompanyName());
            phoneField.setText(supplier.getPhone());
            emailField.setText(supplier.getEmail());
            addressField.setText(supplier.getAddress());
            contactPersonField.setText(supplier.getContactPerson());
        }
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (supplier == null) {
                supplier = new Supplier();
            }
            supplier.setCompanyName(companyNameField.getText());
            supplier.setPhone(phoneField.getText());
            supplier.setEmail(emailField.getText());
            supplier.setAddress(addressField.getText());
            supplier.setContactPerson(contactPersonField.getText());

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

        if (companyNameField.getText() == null || companyNameField.getText().trim().isEmpty()) {
            errorMessage += "Tên công ty không được để trống!\n";
        }
        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            errorMessage += "Số điện thoại không được để trống!\n";
        }
        // Có thể thêm kiểm tra định dạng email, số điện thoại...

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Lỗi nhập liệu");
            alert.setHeaderText("Thông tin nhà cung cấp không hợp lệ");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
