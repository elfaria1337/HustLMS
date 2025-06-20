package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.LoanDetail;
import repo.LoanDetailRepository;
import repo.LoanRepository;
import repo.BookCopyRepository;

public class LoanDetailFormController {

    @FXML private TextField loanIdField;
    @FXML private Label loanIdErrorLabel;

    @FXML private TextField copyIdField;
    @FXML private Label copyIdErrorLabel;

    @FXML private DatePicker returnDatePicker;

    private Stage dialogStage;
    private LoanDetail loanDetail;
    private boolean saveClicked = false;

    private LoanDetailRepository loanDetailRepo = new LoanDetailRepository();
    private LoanRepository loanRepo = new LoanRepository();
    private BookCopyRepository bookCopyRepo = new BookCopyRepository();

    @FXML
    public void initialize() {
        loanIdErrorLabel.setVisible(false);
        copyIdErrorLabel.setVisible(false);

        // Kiểm tra loanIdField có tồn tại loan_id trong DB không
        loanIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                loanIdErrorLabel.setVisible(false);
                return;
            }
            try {
                int loanId = Integer.parseInt(newVal.trim());
                boolean exists = loanRepo.findById(loanId) != null;
                loanIdErrorLabel.setVisible(!exists);
                loanIdErrorLabel.setText("Mã phiếu mượn không tồn tại!");
            } catch (NumberFormatException e) {
                loanIdErrorLabel.setVisible(true);
                loanIdErrorLabel.setText("Mã phiếu mượn phải là số nguyên!");
            }
        });

        // Kiểm tra copyIdField có tồn tại copy_id trong DB không
        copyIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                copyIdErrorLabel.setVisible(false);
                return;
            }
            try {
                int copyId = Integer.parseInt(newVal.trim());
                boolean exists = bookCopyRepo.findById(copyId) != null;
                copyIdErrorLabel.setVisible(!exists);
                copyIdErrorLabel.setText("Mã bản sao sách không tồn tại!");
            } catch (NumberFormatException e) {
                copyIdErrorLabel.setVisible(true);
                copyIdErrorLabel.setText("Mã bản sao sách phải là số nguyên!");
            }
        });
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setLoanDetail(LoanDetail loanDetail) {
        this.loanDetail = loanDetail;
        if (loanDetail != null) {
            if (loanDetail.getLoanId() != 0) {
                loanIdField.setText(String.valueOf(loanDetail.getLoanId()));
                loanIdErrorLabel.setVisible(false);
            } else {
                loanIdField.clear();
                loanIdErrorLabel.setVisible(false);
            }
            if (loanDetail.getCopyId() != 0) {
                copyIdField.setText(String.valueOf(loanDetail.getCopyId()));
                copyIdErrorLabel.setVisible(false);
            } else {
                copyIdField.clear();
                copyIdErrorLabel.setVisible(false);
            }
            if (loanDetail.getReturnDate() != null) {
                returnDatePicker.setValue(loanDetail.getReturnDate());
            } else {
                returnDatePicker.setValue(null);
            }
        }
    }

    public LoanDetail getLoanDetail() {
        return loanDetail;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (loanDetail == null) {
                loanDetail = new LoanDetail();
            }
            loanDetail.setLoanId(Integer.parseInt(loanIdField.getText().trim()));
            loanDetail.setCopyId(Integer.parseInt(copyIdField.getText().trim()));
            loanDetail.setReturnDate(returnDatePicker.getValue());

            loanDetailRepo.save(loanDetail);

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

        // Kiểm tra loanId
        if (loanIdField.getText() == null || loanIdField.getText().trim().isEmpty()) {
            errorMessage += "Mã phiếu mượn không được để trống!\n";
        } else {
            try {
                int loanId = Integer.parseInt(loanIdField.getText().trim());
                if (loanRepo.findById(loanId) == null) {
                    errorMessage += "Mã phiếu mượn không tồn tại!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Mã phiếu mượn phải là số nguyên!\n";
            }
        }

        // Kiểm tra copyId
        if (copyIdField.getText() == null || copyIdField.getText().trim().isEmpty()) {
            errorMessage += "Mã bản sao sách không được để trống!\n";
        } else {
            try {
                int copyId = Integer.parseInt(copyIdField.getText().trim());
                if (bookCopyRepo.findById(copyId) == null) {
                    errorMessage += "Mã bản sao sách không tồn tại!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Mã bản sao sách phải là số nguyên!\n";
            }
        }

        // Ngày trả có thể để trống

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Lỗi nhập liệu");
            alert.setHeaderText("Thông tin chi tiết mượn không hợp lệ");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
