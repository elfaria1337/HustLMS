package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.BookCopy;
import model.BookTitle;
import model.Inventory;
import repo.BookTitleRepository;
import repo.InventoryRepository;

import java.util.List;

public class BookCopyFormController {

    @FXML private ComboBox<String> stateComboBox;

    @FXML private TextField inventoryInput;
    @FXML private Label inventoryStatusLabel;

    @FXML private TextField titleInput;
    @FXML private Label titleStatusLabel;

    private Stage dialogStage;
    private BookCopy bookCopy;
    private boolean saveClicked = false;

    private InventoryRepository inventoryRepo = new InventoryRepository();
    private BookTitleRepository bookTitleRepo = new BookTitleRepository();

    private Inventory selectedInventory;
    private BookTitle selectedBookTitle;

    @FXML
    public void initialize() {
        stateComboBox.setItems(FXCollections.observableArrayList("Sẵn sàng", "Đang mượn", "Hỏng"));
        stateComboBox.setValue("Sẵn sàng");

        inventoryStatusLabel.setText("");
        titleStatusLabel.setText("");

        // Thêm listener để kiểm tra tồn tại kho sách theo tên nhập
        inventoryInput.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                inventoryStatusLabel.setText("");
                selectedInventory = null;
                return;
            }
            List<Inventory> found = inventoryRepo.searchByLocationName(newVal.trim());
            if (!found.isEmpty()) {
                selectedInventory = found.get(0); // lấy đầu tiên khớp
                inventoryStatusLabel.setText("");
            } else {
                selectedInventory = null;
                inventoryStatusLabel.setText("Kho sách không tồn tại!");
            }
        });

        // Thêm listener để kiểm tra tồn tại đầu sách theo tên nhập
        titleInput.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                titleStatusLabel.setText("");
                selectedBookTitle = null;
                return;
            }
            List<BookTitle> found = bookTitleRepo.searchByTitleName(newVal.trim());
            if (!found.isEmpty()) {
                selectedBookTitle = found.get(0);
                titleStatusLabel.setText("");
            } else {
                selectedBookTitle = null;
                titleStatusLabel.setText("Đầu sách không tồn tại!");
            }
        });
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
        if (bookCopy != null) {
            stateComboBox.setValue(bookCopy.getState());

            selectedInventory = inventoryRepo.findById(bookCopy.getInventoryId());
            if (selectedInventory != null) {
                inventoryInput.setText(selectedInventory.getLocationName());
            }

            selectedBookTitle = bookTitleRepo.findById(bookCopy.getTitleId());
            if (selectedBookTitle != null) {
                titleInput.setText(selectedBookTitle.getTitleName());
            }
        }
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        String error = "";

        if (stateComboBox.getValue() == null || stateComboBox.getValue().trim().isEmpty()) {
            error += "Trạng thái không được để trống!\n";
        }
        if (selectedInventory == null) {
            error += "Kho sách chưa hợp lệ hoặc không tồn tại!\n";
        }
        if (selectedBookTitle == null) {
            error += "Đầu sách chưa hợp lệ hoặc không tồn tại!\n";
        }

        if (!error.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Lỗi nhập liệu");
            alert.setHeaderText("Thông tin bản sao sách không hợp lệ");
            alert.setContentText(error);
            alert.showAndWait();
            return;
        }

        if (bookCopy == null) {
            bookCopy = new BookCopy();
        }
        bookCopy.setState(stateComboBox.getValue());
        bookCopy.setInventoryId(selectedInventory.getInventoryId());
        bookCopy.setTitleId(selectedBookTitle.getTitleId());

        saveClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
