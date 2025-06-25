package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Loan;
import model.Reader;
import repo.LoanRepository;
import repo.ReaderRepository;

public class LoanFormController {

    @FXML
    private DatePicker loanDatePicker;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private TextField phoneField;

    @FXML
    private Label phoneErrorLabel;

    private Stage dialogStage;
    private Loan loan;
    private boolean saveClicked = false;

    private LoanRepository loanRepo = new LoanRepository();
    private ReaderRepository readerRepo = new ReaderRepository();

    @FXML
    public void initialize() {
        phoneErrorLabel.setVisible(false);
        phoneField.setText("");
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                phoneErrorLabel.setVisible(false);
                return;
            }
            boolean exists = false;
            // Kiểm tra Reader tồn tại theo số điện thoại
            exists = readerRepo.searchByNameOrPhone(newVal.trim()).stream()
                    .anyMatch(r -> newVal.trim().equals(r.getPhone()));
            phoneErrorLabel.setVisible(!exists);
        });
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
        if (loan != null) {
            loanDatePicker.setValue(loan.getLoanDate());
            dueDatePicker.setValue(loan.getDueDate());

            // Lấy số điện thoại từ readerId nếu có
            if (loan.getReaderId() > 0) {
                Reader reader = readerRepo.findById(loan.getReaderId());
                if (reader != null && reader.getPhone() != null) {
                    phoneField.setText(reader.getPhone());
                } else {
                    phoneField.setText("");
                }
            } else {
                phoneField.setText("");
            }
        }
    }

    public Loan getLoan() {
        return loan;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (loan == null) {
                loan = new Loan();
            }
            loan.setLoanDate(loanDatePicker.getValue());
            loan.setDueDate(dueDatePicker.getValue());

            // Tìm readerId theo số điện thoại nhập
            String phone = phoneField.getText().trim();
            Reader reader = readerRepo.searchByNameOrPhone(phone).stream()
                    .filter(r -> phone.equals(r.getPhone()))
                    .findFirst().orElse(null);
            if (reader == null) {
                phoneErrorLabel.setVisible(true);
                showAlert("Lỗi", "Số điện thoại không tồn tại trong hệ thống.");
                return;
            }

            loan.setReaderId(reader.getReaderId());

            loanRepo.save(loan);

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

        if (loanDatePicker.getValue() == null) {
            errorMessage += "Ngày mượn không được để trống!\n";
        }
        if (dueDatePicker.getValue() == null) {
            errorMessage += "Ngày trả hạn không được để trống!\n";
        }
        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            errorMessage += "Số điện thoại không được để trống!\n";
        } else {
            boolean exists = readerRepo.searchByNameOrPhone(phoneField.getText().trim()).stream()
                    .anyMatch(r -> phoneField.getText().trim().equals(r.getPhone()));
            if (!exists) {
                errorMessage += "Số điện thoại không tồn tại trong hệ thống!\n";
            }
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Lỗi nhập liệu");
            alert.setHeaderText("Thông tin phiếu mượn không hợp lệ");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);  
        alert.initOwner(dialogStage);                     
        alert.setTitle("Thông báo");
        alert.setHeaderText(header);                     
        alert.setContentText(content);                    
        alert.showAndWait();                        
    }
}
