package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Invoice;
import repo.FineRepository;
import repo.ReaderRepository;

import java.math.BigDecimal;

public class InvoiceFormController {

    @FXML private TextField amountField;
    @FXML private TextField paymentMethodField;
    @FXML private DatePicker paymentDatePicker;
    @FXML private TextField readerIdField;
    @FXML private TextField fineIdField;

    @FXML private Label readerIdErrorLabel;
    @FXML private Label fineIdErrorLabel;

    private Stage dialogStage;
    private Invoice invoice;
    private boolean saveClicked = false;

    private ReaderRepository readerRepo = new ReaderRepository();
    private FineRepository fineRepo = new FineRepository();

    @FXML
    public void initialize() {
        readerIdErrorLabel.setVisible(false);
        fineIdErrorLabel.setVisible(false);

        readerIdField.setText("");
        fineIdField.setText("");

        readerIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                readerIdErrorLabel.setVisible(false);
                return;
            }
            try {
                int id = Integer.parseInt(newVal.trim());
                boolean exists = readerRepo.findById(id) != null;
                readerIdErrorLabel.setVisible(!exists);
            } catch (NumberFormatException e) {
                readerIdErrorLabel.setText("Mã độc giả phải là số nguyên");
                readerIdErrorLabel.setVisible(true);
            }
        });

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

        // Optional: disable typing on DatePicker editor to prevent invalid input
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
            readerIdField.setText(invoice.getReaderId() != 0 ? String.valueOf(invoice.getReaderId()) : "");
            fineIdField.setText(invoice.getFineId() != null ? String.valueOf(invoice.getFineId()) : "");
            readerIdErrorLabel.setVisible(false);
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

            invoice.setAmount(new BigDecimal(amountField.getText().trim()));
            invoice.setPaymentMethod(paymentMethodField.getText().trim());
            invoice.setPaymentDate(paymentDatePicker.getValue());
            invoice.setReaderId(Integer.parseInt(readerIdField.getText().trim()));

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
}
