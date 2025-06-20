package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Loan;
import repo.LoanRepository;
import repo.ReaderRepository;

public class LoanFormController {

    @FXML
    private DatePicker loanDatePicker;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private TextField readerIdField;

    @FXML
    private Label readerIdErrorLabel;

    private Stage dialogStage;
    private Loan loan;
    private boolean saveClicked = false;

    private LoanRepository loanRepo = new LoanRepository();
    private ReaderRepository readerRepo = new ReaderRepository();

    @FXML
    public void initialize() {
        readerIdErrorLabel.setVisible(false);
        readerIdField.setText("");
        readerIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                readerIdErrorLabel.setVisible(false);  // để trống thì không báo lỗi
                return;
            }
            try {
                int id = Integer.parseInt(newVal.trim());
                boolean exists = readerRepo.findById(id) != null; // Kiểm tra có tồn tại không
                readerIdErrorLabel.setVisible(!exists);
            } catch (NumberFormatException e) {
                readerIdErrorLabel.setText("ID độc giả phải là số nguyên!");
                readerIdErrorLabel.setVisible(true);
            }
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

            if (loan.getReaderId() > 0) {
                readerIdField.setText(String.valueOf(loan.getReaderId()));
            } else {
                readerIdField.setText("");
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
            // loanId thường tự sinh DB, nên không set trong form
            loan.setLoanDate(loanDatePicker.getValue());
            loan.setDueDate(dueDatePicker.getValue());
            loan.setReaderId(Integer.parseInt(readerIdField.getText().trim()));

            // Lưu xuống DB (hoặc gọi service)
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
        if (readerIdField.getText() == null || readerIdField.getText().trim().isEmpty()) {
            errorMessage += "Mã độc giả không được để trống!\n";
        } else {
            try {
                int id = Integer.parseInt(readerIdField.getText().trim());
                if (readerRepo.findById(id) == null) {
                    errorMessage += "ID độc giả không tồn tại!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Mã độc giả phải là số nguyên!\n";
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
}
