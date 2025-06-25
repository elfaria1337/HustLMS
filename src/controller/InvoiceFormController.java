package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Invoice;
import model.Reader;
import repo.FineRepository;
import repo.ReaderRepository;

import java.math.BigDecimal;

public class InvoiceFormController {

    @FXML private TextField amountField;
    @FXML private TextField paymentMethodField;
    @FXML private DatePicker paymentDatePicker;
    @FXML private TextField phoneField;
    @FXML private TextField fineIdField;

    @FXML private Label phoneErrorLabel;
    @FXML private Label fineIdErrorLabel;

    private Stage dialogStage;
    private Invoice invoice;
    private boolean saveClicked = false;

    private ReaderRepository readerRepo = new ReaderRepository();
    private FineRepository fineRepo = new FineRepository();

    @FXML
    public void initialize() {
        phoneErrorLabel.setVisible(false);
        fineIdErrorLabel.setVisible(false);

        fineIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                fineIdErrorLabel.setVisible(false);
                return;
            }
            try {
                int id = Integer.parseInt(newVal.trim());
                boolean exists = fineRepo.findById(id) != null;
                fineIdErrorLabel.setVisible(!exists);
            } catch (NumberFormatException e) {
                fineIdErrorLabel.setText("Mã phạt phải là số nguyên");
                fineIdErrorLabel.setVisible(true);
            }
        });
        
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                phoneErrorLabel.setVisible(false);
                return;
            }
            Reader reader = readerRepo.findByPhone(newVal.trim());
            phoneErrorLabel.setVisible(reader == null);
        });

        paymentDatePicker.getEditor().setEditable(false);
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
        if (invoice != null) {
            amountField.setText(invoice.getAmount() != null ? invoice.getAmount().toString() : "");
            paymentMethodField.setText(invoice.getPaymentMethod());
            paymentDatePicker.setValue(invoice.getPaymentDate());
            fineIdField.setText(invoice.getFineId() != null ? String.valueOf(invoice.getFineId()) : "");
            // Tìm số điện thoại của độc giả để hiện lên trường phoneField (nếu cần)
            if (invoice.getReaderId() != 0) {
                Reader reader = readerRepo.findById(invoice.getReaderId());
                if (reader != null) {
                    phoneField.setText(reader.getPhone());
                }
            }
            phoneErrorLabel.setVisible(false);
            fineIdErrorLabel.setVisible(false);
        }
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (invoice == null) {
                invoice = new Invoice();
            }

            // Tìm readerId theo số điện thoại
            Reader reader = readerRepo.findByPhone(phoneField.getText().trim());
            if (reader == null) {
                // Nên kiểm tra trước khi save để đảm bảo số điện thoại hợp lệ
                showAlert("Số điện thoại độc giả không tồn tại.");
                return;
            }

            invoice.setAmount(new BigDecimal(amountField.getText().trim()));
            invoice.setPaymentMethod(paymentMethodField.getText().trim());
            invoice.setPaymentDate(paymentDatePicker.getValue());
            invoice.setReaderId(reader.getReaderId()); // dùng readerId tìm được

            String fineIdText = fineIdField.getText().trim();
            if (fineIdText.isEmpty()) {
                invoice.setFineId(null);
            } else {
                invoice.setFineId(Integer.parseInt(fineIdText));
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

        if (amountField.getText() == null || amountField.getText().trim().isEmpty()) {
            errorMessage.append("Số tiền không được để trống!\n");
        } else {
            try {
                new BigDecimal(amountField.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage.append("Số tiền phải là số hợp lệ!\n");
            }
        }

        if (paymentMethodField.getText() == null || paymentMethodField.getText().trim().isEmpty()) {
            errorMessage.append("Phương thức thanh toán không được để trống!\n");
        }

        if (paymentDatePicker.getValue() == null) {
            errorMessage.append("Ngày thanh toán không được để trống!\n");
        }

        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            errorMessage.append("Số điện thoại độc giả không được để trống!\n");
        } else {
            Reader reader = readerRepo.findByPhone(phoneField.getText().trim());
            if (reader == null) {
                errorMessage.append("Số điện thoại độc giả không tồn tại!\n");
            }
        }

        String fineIdText = fineIdField.getText();
        if (fineIdText != null && !fineIdText.trim().isEmpty()) {
            try {
                int fineId = Integer.parseInt(fineIdText.trim());
                if (fineRepo.findById(fineId) == null) {
                    errorMessage.append("Mã phạt không tồn tại!\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Mã phạt phải là số nguyên!\n");
            }
        }

        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Lỗi nhập liệu");
            alert.setHeaderText("Thông tin hóa đơn không hợp lệ");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
