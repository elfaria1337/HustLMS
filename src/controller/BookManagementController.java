package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.BookCopy;
import model.BookTitle;
import model.Inventory;
import repo.BookCopyRepository;
import repo.BookTitleRepository;
import repo.InventoryRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BookManagementController {

    // BookTitle
    @FXML private TableView<BookTitle> bookTitleTable;
    @FXML private TableColumn<BookTitle, Integer> colTitleId;
    @FXML private TableColumn<BookTitle, String> colTitleName;
    @FXML private TableColumn<BookTitle, String> colAuthor;
    @FXML private TableColumn<BookTitle, String> colGenre;
    @FXML private TableColumn<BookTitle, String> colPublisher;
    @FXML private TableColumn<BookTitle, Integer> colPublishYear;
    @FXML private TextField searchField;

    // Inventory
    @FXML private TableView<Inventory> inventoryTable;
    @FXML private TableColumn<Inventory, Integer> colInventoryId;
    @FXML private TableColumn<Inventory, String> colLocationName;

    // BookCopy
    @FXML private TableView<BookCopy> bookCopyTable;
    @FXML private TableColumn<BookCopy, Integer> colCopyId;
    @FXML private TableColumn<BookCopy, String> colCopyState;
    @FXML private TableColumn<BookCopy, String> colCopyInventory;
    @FXML private TableColumn<BookCopy, String> colCopyTitle;

    // Repositories
    private BookTitleRepository bookTitleRepo = new BookTitleRepository();
    private InventoryRepository inventoryRepo = new InventoryRepository();
    private BookCopyRepository bookCopyRepo = new BookCopyRepository();

    // ObservableLists
    private ObservableList<BookTitle> bookTitleList = FXCollections.observableArrayList();
    private ObservableList<Inventory> inventoryList = FXCollections.observableArrayList();
    private ObservableList<BookCopy> bookCopyList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup BookTitle columns
        colTitleId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTitleId()));
        colTitleName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitleName()));
        colAuthor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAuthor()));
        colGenre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGenre()));
        colPublisher.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPublisher()));
        colPublishYear.setCellValueFactory(cellData -> {
            Integer year = cellData.getValue().getPublishYear();
            return new ReadOnlyObjectWrapper<>(year != null ? year : 0);
        });

        // Setup Inventory columns
        colInventoryId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getInventoryId()));
        colLocationName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLocationName()));

        // Setup BookCopy columns
        colCopyId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCopyId()));
        colCopyState.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getState()));

        // For inventory name and book title name, we need to fetch names based on IDs
        colCopyInventory.setCellValueFactory(cellData -> {
            int invId = cellData.getValue().getInventoryId();
            Inventory inv = inventoryRepo.findById(invId);
            String name = (inv != null) ? inv.getLocationName() : "N/A";
            return new javafx.beans.property.SimpleStringProperty(name);
        });

        colCopyTitle.setCellValueFactory(cellData -> {
            int titleId = cellData.getValue().getTitleId();
            BookTitle book = bookTitleRepo.findById(titleId);
            String name = (book != null) ? book.getTitleName() : "N/A";
            return new javafx.beans.property.SimpleStringProperty(name);
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                loadBookTitles(); // load tất cả đầu sách khi ô tìm trống
            } else {
                List<BookTitle> filtered = bookTitleRepo.searchByTitleOrAuthor(newValue.trim());
                ObservableList<BookTitle> filteredList = FXCollections.observableArrayList(filtered);
                bookTitleTable.setItems(filteredList);
            }
        });

        loadData();
    }

    private void loadData() {
        List<BookTitle> books = bookTitleRepo.findAll();
        bookTitleList.setAll(books);
        bookTitleTable.setItems(bookTitleList);

        List<Inventory> inventories = inventoryRepo.findAll();
        inventoryList.setAll(inventories);
        inventoryTable.setItems(inventoryList);

        List<BookCopy> copies = bookCopyRepo.findAll();
        bookCopyList.setAll(copies);
        bookCopyTable.setItems(bookCopyList);
    }

    // ========== BookTitle handlers ==========
    @FXML
    public void handleAddBookTitle() {
        try {
            // Load form nhập liệu đầu sách
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/book_title_form.fxml"));
            Parent page = loader.load();

            // Tạo stage dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm đầu sách mới");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookTitleTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Lấy controller form và truyền stage cho nó
            BookTitleFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookTitle(null);  // null = thêm mới

            // Hiển thị dialog và chờ người dùng đóng
            dialogStage.showAndWait();

            // Nếu người dùng đã lưu
            if (controller.isSaveClicked()) {
                model.BookTitle newBookTitle = controller.getBookTitle();
                boolean success = bookTitleRepo.insert(newBookTitle);
                if (success) {
                    loadBookTitles(); // Tải lại danh sách đầu sách lên bảng
                    showInfo("Thêm đầu sách thành công.");
                } else {
                    showError("Thêm đầu sách thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi khi mở form thêm đầu sách.");
        }
    }

    public void handleEditBookTitle() {
        BookTitle selected = bookTitleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn đầu sách để sửa.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/book_title_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sửa đầu sách");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookTitleTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setWidth(400);

            BookTitleFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookTitle(selected); // truyền dữ liệu đầu sách cần sửa

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                BookTitle updatedBookTitle = controller.getBookTitle();
                boolean success = bookTitleRepo.update(updatedBookTitle);
                if (success) {
                    loadBookTitles(); // reload danh sách đầu sách
                    showInfo("Sửa đầu sách thành công.");
                } else {
                    showError("Sửa đầu sách thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi khi mở form sửa đầu sách.");
        }
    }

    public void handleDeleteBookTitle() {
        BookTitle selected = bookTitleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn đầu sách để xóa.");
            return;
        }
        boolean confirmed = confirmDialog("Bạn có chắc muốn xóa đầu sách này?");
        if (confirmed) {
            if (bookTitleRepo.delete(selected.getTitleId())) {
                showInfo("Xóa đầu sách thành công.");
                loadData();
            } else {
                showWarning("Xóa đầu sách thất bại.");
            }
        }
    }

    // ========== Inventory handlers ==========
    public void handleAddInventory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/inventory_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm kho sách mới");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(inventoryTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            InventoryFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setInventory(null); // Thêm mới

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Inventory newInventory = controller.getInventory();
                boolean success = inventoryRepo.insert(newInventory);
                if (success) {
                    loadInventories(); // Tải lại danh sách kho
                    showInfo("Thêm kho sách thành công.");
                } else {
                    showError("Thêm kho sách thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi khi mở form thêm kho sách.");
        }
    }

    public void handleEditInventory() {
        Inventory selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn kho để sửa.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/inventory_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sửa kho sách");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(inventoryTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            InventoryFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setInventory(selected); // Truyền đối tượng cần sửa

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Inventory updatedInventory = controller.getInventory();
                boolean success = inventoryRepo.update(updatedInventory);
                if (success) {
                    loadInventories(); // Tải lại danh sách kho
                    showInfo("Sửa kho sách thành công.");
                } else {
                    showError("Sửa kho sách thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi khi mở form sửa kho sách.");
        }
    }

    public void handleDeleteInventory() {
        Inventory selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn kho để xóa.");
            return;
        }
        boolean confirmed = confirmDialog("Bạn có chắc muốn xóa kho này?");
        if (confirmed) {
            if (inventoryRepo.delete(selected.getInventoryId())) {
                showInfo("Xóa kho thành công.");
                loadData();
            } else {
                showWarning("Xóa kho thất bại.");
            }
        }
    }

    private void loadInventories() {
        List<Inventory> inventories = inventoryRepo.findAll();  // inventoryRepo là repository quản lý kho sách
        ObservableList<Inventory> inventoryList = FXCollections.observableArrayList(inventories);
        inventoryTable.setItems(inventoryList);
    }

    // ========== BookCopy handlers ==========
    public void handleAddBookCopy() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/book_copy_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm bản sao sách");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookCopyTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BookCopyFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookCopy(null); // thêm mới

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                BookCopy newCopy = controller.getBookCopy();
                boolean success = bookCopyRepo.insert(newCopy);
                if (success) {
                    loadBookCopies();
                    showInfo("Thêm bản sao sách thành công.");
                } else {
                    showError("Thêm bản sao sách thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi khi mở form thêm bản sao sách.");
        }
    }

    public void handleEditBookCopy() {
        BookCopy selected = bookCopyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn bản sao để sửa.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/book_copy_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sửa bản sao sách");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookCopyTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BookCopyFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookCopy(selected); // sửa

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                BookCopy updatedCopy = controller.getBookCopy();
                boolean success = bookCopyRepo.update(updatedCopy);
                if (success) {
                    loadBookCopies();
                    showInfo("Sửa bản sao sách thành công.");
                } else {
                    showError("Sửa bản sao sách thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi khi mở form sửa bản sao sách.");
        }
    }
    public void handleDeleteBookCopy() {
        BookCopy selected = bookCopyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn bản sao để xóa.");
            return;
        }
        boolean confirmed = confirmDialog("Bạn có chắc muốn xóa bản sao sách này?");
        if (confirmed) {
            if (bookCopyRepo.delete(selected.getCopyId())) {
                showInfo("Xóa bản sao thành công.");
                loadData();
            } else {
                showWarning("Xóa bản sao thất bại.");
            }
        }
    }

    private void loadBookCopies() {
        List<BookCopy> copies = bookCopyRepo.findAll(); // gọi repo lấy dữ liệu
        ObservableList<BookCopy> copyList = FXCollections.observableArrayList(copies);
        bookCopyTable.setItems(copyList);
    }

    // === Helper dialog methods ===
    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Nạp lại danh sách đầu sách và hiển thị lên bảng
    private void loadBookTitles() {
        List<BookTitle> bookTitles = bookTitleRepo.findAll();
        ObservableList<BookTitle> list = FXCollections.observableArrayList(bookTitles);
        bookTitleTable.setItems(list);
    }

    // Hiển thị thông báo lỗi
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private boolean confirmDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setContentText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
