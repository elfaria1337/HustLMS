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

    @FXML private TextField bookTitleField;
    @FXML private Button btnSearchCopy;

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
        loanIdField.setDisable(false);

        btnSearchCopy.setOnAction(event -> searchCopyIdByTitle());

        // Lấy mã phiếu mượn mới nhất
        Integer latestLoanId = loanRepo.getLatestLoanId();
        if (latestLoanId != null) {
            loanIdField.setText(String.valueOf(latestLoanId));
            loanIdField.setDisable(true); // Không cho sửa mã phiếu mượn khi thêm mới
        } else {
            loanIdField.setText("");
            loanIdField.setDisable(false);
        }

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

    private void searchCopyIdByTitle() {
        String titleName = bookTitleField.getText().trim();
        if (titleName.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập tên sách để tìm mã bản sao.");
            return;
        }

        // Gọi repo để lấy mã bản sao sách mới nhất còn trạng thái 'Sẵn sàng'
        Integer copyId = bookCopyRepo.findAvailableCopyIdByTitleName(titleName);
        if (copyId != null) {
            copyIdField.setText(copyId.toString());
            copyIdErrorLabel.setVisible(false);
        } else {
            copyIdErrorLabel.setText("Không tìm thấy bản sao sách sẵn sàng với tên sách đã nhập.");
            copyIdErrorLabel.setVisible(true);
            copyIdField.clear();
        }
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setLoanDetail(LoanDetail loanDetail) {
        this.loanDetail = loanDetail;

        if (loanDetail != null) {
            // Sửa chi tiết mượn: gán dữ liệu hiện có
            loanIdField.setText(String.valueOf(loanDetail.getLoanId()));
            loanIdField.setDisable(false); // cho phép sửa khi sửa
            loanIdErrorLabel.setVisible(false);

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
        } else {
            // Thêm mới: lấy loanId mới nhất từ DB
            Integer latestLoanId = loanRepo.getLatestLoanId();
            if (latestLoanId != null) {
                loanIdField.setText(String.valueOf(latestLoanId));
                // Không disable, cho phép sửa
                loanIdField.setDisable(false);
            } else {
                loanIdField.clear();
                loanIdField.setDisable(false);
            }
            copyIdField.clear();
            copyIdField.setDisable(false);
            copyIdErrorLabel.setVisible(false);
            returnDatePicker.setValue(null);
            loanIdErrorLabel.setVisible(false);
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

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Thông báo");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
