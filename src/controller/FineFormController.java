package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Fine;
import model.Reader;
import repo.ReaderRepository;

import java.math.BigDecimal;

public class FineFormController {

    @FXML private TextField reasonField;
    @FXML private TextField amountField;
    @FXML private DatePicker issueDatePicker;
    @FXML private TextField phoneField;

    @FXML private Label phoneErrorLabel;

    private Stage dialogStage;
    private Fine fine;
    private boolean saveClicked = false;

    private ReaderRepository readerRepo = new ReaderRepository();

    @FXML
    public void initialize() {
        phoneErrorLabel.setVisible(false);
        phoneField.setText("");

        // Listener kiểm tra phone nhập vào
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                phoneErrorLabel.setVisible(false);
                return;
            }
            boolean exists = readerRepo.findByPhone(newVal.trim()) != null;
            if (!exists) {
                phoneErrorLabel.setText("Số điện thoại không tồn tại");
                phoneErrorLabel.setVisible(true);
            } else {
                phoneErrorLabel.setVisible(false);
            }
        });

        issueDatePicker.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) return;
            try {
                issueDatePicker.getConverter().fromString(newVal);
            } catch (Exception e) {
                // Sai định dạng -> reset về oldVal
                issueDatePicker.getEditor().setText(oldVal);
            }
        });
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setFine(Fine fine) {
        this.fine = fine;
        if (fine != null) {
            reasonField.setText(fine.getReason());
            amountField.setText(fine.getAmount() != null ? fine.getAmount().toString() : "");
            if (fine.getIssueDate() != null) {
                issueDatePicker.setValue(fine.getIssueDate());
            } else {
                issueDatePicker.setValue(null);
            }
            // Lấy số điện thoại của độc giả theo readerId
            Reader reader = readerRepo.findById(fine.getReaderId());
            if (reader != null) {
                phoneField.setText(reader.getPhone());
            } else {
                phoneField.setText("");
            }
            phoneErrorLabel.setVisible(false);
        }
    }

    public Fine getFine() {
        return fine;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (fine == null) {
                fine = new Fine();
            }
            fine.setReason(reasonField.getText().trim());
            fine.setAmount(new BigDecimal(amountField.getText().trim()));
            fine.setIssueDate(issueDatePicker.getValue());

            // Lấy readerId từ số điện thoại
            String phone = phoneField.getText().trim();
            Reader reader = readerRepo.findByPhone(phone);
            if (reader != null) {
                fine.setReaderId(reader.getReaderId());
            } else {
                fine.setReaderId(0); // hoặc xử lý lỗi
            }

            saveClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();

        if (reasonField.getText() == null || reasonField.getText().trim().isEmpty()) {
            errorMessage.append("Lý do không được để trống!\n");
        }
        if (amountField.getText() == null || amountField.getText().trim().isEmpty()) {
            errorMessage.append("Số tiền không được để trống!\n");
        } else {
            try {
                new BigDecimal(amountField.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage.append("Số tiền phải là số hợp lệ!\n");
            }
        }
        if (issueDatePicker.getValue() == null) {
            errorMessage.append("Ngày phạt không được để trống!\n");
        }
        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            errorMessage.append("Số điện thoại không được để trống!\n");
        } else {
            boolean exists = readerRepo.findByPhone(phoneField.getText().trim()) != null;
            if (!exists) {
                errorMessage.append("Số điện thoại không tồn tại!\n");
            }
        }

        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Lỗi nhập liệu");
            alert.setHeaderText("Thông tin phạt không hợp lệ");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }
}
