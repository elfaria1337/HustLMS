package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.BookImport;
import model.BookImportDetail;
import model.BookTitle;
import repo.BookImportRepository;
import repo.BookTitleRepository;

import java.math.BigDecimal;

public class BookImportDetailFormController {

    @FXML private ComboBox<String> bookImportComboBox;
    @FXML private ComboBox<String> bookTitleComboBox;
    @FXML private TextField bookImportTextField;
    @FXML private TextField bookTitleTextField;
    @FXML private Label bookImportErrorLabel;
    @FXML private Label bookTitleErrorLabel;

    @FXML private TextField quantityField;
    @FXML private TextField pricePerUnitField;

    private Stage dialogStage;
    private BookImportDetail bookImportDetail;
    private boolean saveClicked = false;

    private BookImportRepository bookImportRepo = new BookImportRepository();
    private BookTitleRepository bookTitleRepo = new BookTitleRepository();

    @FXML
    public void initialize() {
        bookImportErrorLabel.setVisible(false);
        bookTitleErrorLabel.setVisible(false);

        // Listener kiểm tra phiếu nhập có tồn tại không
        bookImportTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                bookImportErrorLabel.setVisible(false);
                return;
            }
            try {
                int id = Integer.parseInt(newVal.trim());
                BookImport b = bookImportRepo.findById(id);
                bookImportErrorLabel.setVisible(b == null);
            } catch (NumberFormatException e) {
                bookImportErrorLabel.setText("ID phiếu nhập không tồn tại");
                bookImportErrorLabel.setVisible(true);
            }
        });

        // Listener kiểm tra đầu sách có tồn tại không
        bookTitleTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                bookTitleErrorLabel.setVisible(false);
                return;
            }
            // Kiểm tra đầu sách có tên chính xác bằng newVal (không phân biệt hoa thường)
            boolean exactMatch = bookTitleRepo.findAll().stream()
                .anyMatch(bt -> bt.getTitleName().equalsIgnoreCase(newVal.trim()));

            if (!exactMatch) {
                bookTitleErrorLabel.setText("Đầu sách không tồn tại!");
                bookTitleErrorLabel.setVisible(true);
            } else {
                bookTitleErrorLabel.setVisible(false);
            }
        });
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setBookImportDetail(BookImportDetail detail) {
        this.bookImportDetail = detail;
        if (detail != null) {
            bookImportTextField.setText(String.valueOf(detail.getImportId()));
            bookImportErrorLabel.setVisible(false);

            BookTitle bookTitle = bookTitleRepo.findById(detail.getTitleId());
            if (bookTitle != null) {
                bookTitleTextField.setText(bookTitle.getTitleName());
                bookTitleErrorLabel.setVisible(false);
            }

            quantityField.setText(String.valueOf(detail.getQuantity()));
            pricePerUnitField.setText(detail.getPricePerUnit().toString());
        }
    }

    public BookImportDetail getBookImportDetail() {
        return this.bookImportDetail;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (bookImportDetail == null) {
                bookImportDetail = new BookImportDetail();
            }
            int importId = Integer.parseInt(bookImportTextField.getText().trim());
            BookImport b = bookImportRepo.findById(importId);
            bookImportDetail.setImportId(b.getImportId());

            String titleName = bookTitleTextField.getText().trim();
            BookTitle bt = bookTitleRepo.findAll().stream()
                .filter(bookTitle -> bookTitle.getTitleName().equalsIgnoreCase(titleName))
                .findFirst().orElse(null);
            bookImportDetail.setTitleId(bt.getTitleId());

            bookImportDetail.setQuantity(Integer.parseInt(quantityField.getText()));
            bookImportDetail.setPricePerUnit(new BigDecimal(pricePerUnitField.getText()));

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

        // Kiểm tra phiếu nhập
        if (bookImportTextField.getText() == null || bookImportTextField.getText().trim().isEmpty()) {
            errorMessage += "Phiếu nhập không được để trống!\n";
        } else {
            try {
                int id = Integer.parseInt(bookImportTextField.getText().trim());
                if (bookImportRepo.findById(id) == null) {
                    errorMessage += "Phiếu nhập không tồn tại!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Phiếu nhập phải là số nguyên!\n";
            }
        }

        // Kiểm tra đầu sách
        if (bookTitleTextField.getText() == null || bookTitleTextField.getText().trim().isEmpty()) {
            errorMessage += "Đầu sách không được để trống!\n";
        } else {
            boolean found = bookTitleRepo.findAll().stream()
                .anyMatch(bt -> bt.getTitleName().equalsIgnoreCase(bookTitleTextField.getText().trim()));
            if (!found) {
                errorMessage += "Đầu sách không tồn tại!\n";
            }
        }

        // Kiểm tra số lượng
        if (quantityField.getText() == null || quantityField.getText().trim().isEmpty()) {
            errorMessage += "Số lượng không được để trống!\n";
        } else {
            try {
                int q = Integer.parseInt(quantityField.getText().trim());
                if (q <= 0) {
                    errorMessage += "Số lượng phải lớn hơn 0!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Số lượng phải là số nguyên!\n";
            }
        }

        // Kiểm tra giá/unit
        if (pricePerUnitField.getText() == null || pricePerUnitField.getText().trim().isEmpty()) {
            errorMessage += "Giá/unit không được để trống!\n";
        } else {
            try {
                double p = Double.parseDouble(pricePerUnitField.getText().trim());
                if (p < 0) {
                    errorMessage += "Giá/unit phải lớn hơn hoặc bằng 0!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Giá/unit phải là số!\n";
            }
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Lỗi nhập liệu");
            alert.setHeaderText("Thông tin chi tiết nhập không hợp lệ");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

        return true;
    }
}
