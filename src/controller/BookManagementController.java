package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.BookCopy;
import model.BookTitle;
import model.Inventory;
import repo.BookCopyRepository;
import repo.BookTitleRepository;
import repo.InventoryRepository;

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
    public void handleAddBookTitle() {
        // TODO: Hiện dialog nhập liệu, lấy dữ liệu và gọi repo.insert()
        showInfo("Chức năng thêm đầu sách chưa triển khai.");
    }

    public void handleEditBookTitle() {
        BookTitle selected = bookTitleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn đầu sách để sửa.");
            return;
        }
        // TODO: Hiện dialog sửa
        showInfo("Chức năng sửa đầu sách chưa triển khai.");
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
        showInfo("Chức năng thêm kho sách chưa triển khai.");
    }

    public void handleEditInventory() {
        Inventory selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn kho để sửa.");
            return;
        }
        showInfo("Chức năng sửa kho chưa triển khai.");
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

    // ========== BookCopy handlers ==========
    public void handleAddBookCopy() {
        showInfo("Chức năng thêm bản sao sách chưa triển khai.");
    }

    public void handleEditBookCopy() {
        BookCopy selected = bookCopyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn bản sao để sửa.");
            return;
        }
        showInfo("Chức năng sửa bản sao chưa triển khai.");
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

    // === Helper dialog methods ===
    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText(msg);
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
