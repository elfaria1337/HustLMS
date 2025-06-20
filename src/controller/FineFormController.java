package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Fine;
import repo.ReaderRepository;

import java.math.BigDecimal;

public class FineFormController {

    @FXML private TextField reasonField;
    @FXML private TextField amountField;
    @FXML private DatePicker issueDatePicker;
    @FXML private TextField readerIdField;

    @FXML private Label readerIdErrorLabel;

    private Stage dialogStage;
    private Fine fine;
    private boolean saveClicked = false;

    private ReaderRepository readerRepo = new ReaderRepository();

    @FXML
    public void initialize() {
        readerIdErrorLabel.setVisible(false);
        readerIdField.setText("");

        // Listener kiểm tra readerId nhập vào
        readerIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                readerIdErrorLabel.setVisible(false);
                return;
            }
            try {
                int id = Integer.parseInt(newVal.trim());
                boolean exists = readerRepo.findById(id) != null;
                if (!exists) {
                    readerIdErrorLabel.setText("Mã độc giả không tồn tại");
                    readerIdErrorLabel.setVisible(true);
                } else {
                    readerIdErrorLabel.setVisible(false);
                }
            } catch (NumberFormatException e) {
                readerIdErrorLabel.setText("Mã độc giả phải là số nguyên");
                readerIdErrorLabel.setVisible(true);
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
            readerIdField.setText(fine.getReaderId() != 0 ? String.valueOf(fine.getReaderId()) : "");
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
            fine.setReaderId(Integer.parseInt(readerIdField.getText().trim()));

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
        if (readerIdField.getText() == null || readerIdField.getText().trim().isEmpty()) {
            errorMessage.append("Mã độc giả không được để trống!\n");
        } else {
            try {
                int id = Integer.parseInt(readerIdField.getText().trim());
                if (readerRepo.findById(id) == null) {
                    errorMessage.append("Mã độc giả không tồn tại!\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Mã độc giả phải là số nguyên!\n");
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
