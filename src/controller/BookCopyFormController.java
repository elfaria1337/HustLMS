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
        // Thiết lập các trạng thái cho ComboBox trạng thái
        stateComboBox.setItems(FXCollections.observableArrayList("Sẵn sàng", "Đang mượn", "Hỏng"));
        stateComboBox.setValue("Sẵn sàng");

        inventoryStatusLabel.setText("");
        titleStatusLabel.setText("");

        // Listener kiểm tra tồn tại kho sách khi nhập tên kho
        inventoryInput.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                inventoryStatusLabel.setText("");
                selectedInventory = null;
                return;
            }
            List<Inventory> found = inventoryRepo.searchByLocationName(newVal.trim());
            if (!found.isEmpty()) {
                selectedInventory = found.get(0); // lấy kho đầu tiên phù hợp
                inventoryStatusLabel.setText("");
            } else {
                selectedInventory = null;
                inventoryStatusLabel.setText("Kho sách không tồn tại!");
            }
        });

        // Listener kiểm tra tồn tại đầu sách khi nhập tên đầu sách
        titleInput.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                titleStatusLabel.setText("");
                selectedBookTitle = null;
                return;
            }
            List<BookTitle> found = bookTitleRepo.searchByTitleName(newVal.trim());
            if (!found.isEmpty()) {
                selectedBookTitle = found.get(0); // lấy đầu sách đầu tiên phù hợp
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
            } else {
                inventoryInput.setText("");
            }

            selectedBookTitle = bookTitleRepo.findById(bookCopy.getTitleId());
            if (selectedBookTitle != null) {
                titleInput.setText(selectedBookTitle.getTitleName());
            } else {
                titleInput.setText("");
            }
        }
    }

    public void setSelectedBookTitle(BookTitle bookTitle) {
        this.selectedBookTitle = bookTitle;
        if (bookTitle != null) {
            // Nếu bạn có một TextField hoặc Label hiển thị id hoặc tên đầu sách, gán tại đây
            titleInput.setText(bookTitle.getTitleName());
            // Nếu bạn muốn khóa trường nhập tên đầu sách khi đã chọn:
            titleInput.setDisable(true);
        } else {
            titleInput.clear();
            titleInput.setDisable(false);
        }
    }

    public void setSelectedInventory(Inventory inventory) {
        this.selectedInventory = inventory;
        if (inventory != null) {
            inventoryInput.setText(inventory.getLocationName());
            inventoryInput.setDisable(true);  // khóa không cho sửa tên kho sách
            inventoryStatusLabel.setText("");
        } else {
            inventoryInput.clear();
            inventoryInput.setDisable(false); // mở lại nếu không có kho sách được chọn
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
        StringBuilder errorBuilder = new StringBuilder();

        if (stateComboBox.getValue() == null || stateComboBox.getValue().trim().isEmpty()) {
            errorBuilder.append("Trạng thái không được để trống!\n");
        }
        if (selectedInventory == null) {
            errorBuilder.append("Kho sách chưa hợp lệ hoặc không tồn tại!\n");
        }
        if (selectedBookTitle == null) {
            errorBuilder.append("Đầu sách chưa hợp lệ hoặc không tồn tại!\n");
        }

        String error = errorBuilder.toString();

        if (!error.isEmpty()) {
            // Sử dụng Platform.runLater với biến final
            final String errMsg = error;
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Lỗi nhập liệu");
                alert.setHeaderText("Thông tin bản sao sách không hợp lệ");
                alert.setContentText(errMsg);
                alert.showAndWait();
            });
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
