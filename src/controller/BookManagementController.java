package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.BookCopy;
import model.BookTitle;
import model.Inventory;
import repo.BookCopyRepository;
import repo.BookCopySummaryRepository;
import repo.BookTitleRepository;
import repo.InventoryRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookManagementController {

    // ==== UI Controls ====

    // BookTitle Table & Columns
    @FXML private TableView<BookTitle> bookTitleTable;
    @FXML private TableColumn<BookTitle, Integer> colTitleId;
    @FXML private TableColumn<BookTitle, String> colTitleName;
    @FXML private TableColumn<BookTitle, String> colAuthor;
    @FXML private TableColumn<BookTitle, String> colGenre;
    @FXML private TableColumn<BookTitle, String> colPublisher;
    @FXML private TableColumn<BookTitle, Integer> colPublishYear;
    @FXML private TableColumn<BookTitle, Integer> colCopyCount;
    @FXML private TextField searchField;
    @FXML private Pagination pagination;

    // Inventory Table & Columns
    @FXML private TableView<Inventory> inventoryTable;
    @FXML private TableColumn<Inventory, Integer> colInventoryId;
    @FXML private TableColumn<Inventory, String> colLocationName;

    // BookCopy Table & Columns
    @FXML private TableView<BookCopy> bookCopyTable;
    @FXML private TableColumn<BookCopy, Integer> colCopyId;
    @FXML private TableColumn<BookCopy, String> colCopyState;
    @FXML private TableColumn<BookCopy, String> colCopyInventory;
    @FXML private TableColumn<BookCopy, String> colCopyTitle;
    @FXML private Pagination bookCopyPagination;

    // ==== Repositories ====
    private final BookTitleRepository bookTitleRepo = new BookTitleRepository();
    private final InventoryRepository inventoryRepo = new InventoryRepository();
    private final BookCopyRepository bookCopyRepo = new BookCopyRepository();
    private final BookCopySummaryRepository bookCopySummaryRepo = new BookCopySummaryRepository();

    // Cache copy count per title
    private Map<Integer, Integer> bookCopyCountMap;

    // Pagination constants
    private static final int ROWS_PER_PAGE = 15;

    // Pagination state for BookTitle
    private int totalBookTitles = 0;
    private String currentSearchKeyword = "";

    // Pagination state for BookCopy (to manage total pages)
    private int totalBookCopies = 0;

    // Observable lists for tables
    private final ObservableList<Inventory> inventoryList = FXCollections.observableArrayList();
    private final ObservableList<BookCopy> bookCopyList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup BookTitle columns
        colTitleId.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().getTitleId()));
        colTitleName.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTitleName()));
        colAuthor.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getAuthor()));
        colGenre.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getGenre()));
        colPublisher.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getPublisher()));
        colPublishYear.setCellValueFactory(cd -> {
            Integer year = cd.getValue().getPublishYear();
            return new ReadOnlyObjectWrapper<>(year != null ? year : 0);
        });
        colCopyCount.setCellValueFactory(cd -> {
            int titleId = cd.getValue().getTitleId();
            int count = bookCopyCountMap != null ? bookCopyCountMap.getOrDefault(titleId, 0) : 0;
            return new ReadOnlyObjectWrapper<>(count);
        });

        // Setup Inventory columns
        colInventoryId.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().getInventoryId()));
        colLocationName.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getLocationName()));

        // Setup BookCopy columns
        colCopyId.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().getCopyId()));
        colCopyState.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getState()));
        colCopyInventory.setCellValueFactory(cd -> {
            Inventory inv = inventoryRepo.findById(cd.getValue().getInventoryId());
            return new SimpleStringProperty(inv != null ? inv.getLocationName() : "N/A");
        });
        colCopyTitle.setCellValueFactory(cd -> {
            BookTitle book = bookTitleRepo.findById(cd.getValue().getTitleId());
            return new SimpleStringProperty(book != null ? book.getTitleName() : "N/A");
        });

        // Setup Pagination factories
        pagination.setPageFactory(this::createBookTitlePage);
        bookCopyPagination.setPageFactory(this::createBookCopyPage);

        // Search field listener to update pagination on keyword change
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            currentSearchKeyword = (newVal != null) ? newVal.trim() : "";
            updateTotalBookTitlesCountAndPagination();
            pagination.setCurrentPageIndex(0);
        });

        // We no longer filter book copies by selected title, so we remove listener on bookTitleTable selection

        // Load initial data
        updateTotalBookTitlesCountAndPagination();
        loadInventories();

        // Initialize book copy pagination by loading first page
        updateTotalBookCopiesCountAndPagination();
        bookCopyPagination.setCurrentPageIndex(0);
    }

    // BookTitle pagination page creation
    private VBox createBookTitlePage(int pageIndex) {
        int offset = pageIndex * ROWS_PER_PAGE;
        List<BookTitle> pageData;
        if (currentSearchKeyword.isEmpty()) {
            pageData = bookTitleRepo.findPage(offset, ROWS_PER_PAGE);
            totalBookTitles = bookTitleRepo.countAll();
        } else {
            pageData = bookTitleRepo.searchPageByTitleOrAuthor(currentSearchKeyword, offset, ROWS_PER_PAGE);
            totalBookTitles = bookTitleRepo.countSearchByTitleOrAuthor(currentSearchKeyword);
        }

        bookCopyCountMap = bookCopySummaryRepo.getAllCopyCounts();

        ObservableList<BookTitle> list = FXCollections.observableArrayList(pageData);
        bookTitleTable.setItems(list);

        int pageCount = (totalBookTitles + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
        pagination.setPageCount(pageCount == 0 ? 1 : pageCount);

        return new VBox(bookTitleTable);
    }

    private void updateTotalBookTitlesCountAndPagination() {
        if (currentSearchKeyword.isEmpty()) {
            totalBookTitles = bookTitleRepo.countAll();
        } else {
            totalBookTitles = bookTitleRepo.countSearchByTitleOrAuthor(currentSearchKeyword);
        }
        int pageCount = (totalBookTitles + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
        pagination.setPageCount(pageCount == 0 ? 1 : pageCount);
    }

    // BookCopy pagination page creation, load ALL copies paginated, no filter
    private VBox createBookCopyPage(int pageIndex) {
        int offset = pageIndex * ROWS_PER_PAGE;
        List<BookCopy> pageData = bookCopyRepo.findPageAll(offset, ROWS_PER_PAGE);
        bookCopyList.setAll(pageData);
        bookCopyTable.setItems(bookCopyList);
        bookCopyTable.refresh();

        int totalCopies = bookCopyRepo.countAll();
        int pageCount = (totalCopies + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
        bookCopyPagination.setPageCount(pageCount == 0 ? 1 : pageCount);

        return new VBox(bookCopyTable);
    }

    private void updateTotalBookCopiesCountAndPagination() {
        totalBookCopies = bookCopyRepo.countAll();
        int pageCount = (totalBookCopies + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
        bookCopyPagination.setPageCount(pageCount == 0 ? 1 : pageCount);
    }

    private void clearBookCopies() {
        bookCopyList.clear();
        bookCopyTable.setItems(bookCopyList);
        bookCopyPagination.setPageCount(1);
    }

    // Load inventories list for Inventory table
    private void loadInventories() {
        List<Inventory> inventories = inventoryRepo.findAll();
        inventoryList.setAll(inventories);
        inventoryTable.setItems(inventoryList);
    }

    // ===========================
    // ========== Handlers =======
    // ===========================

    @FXML
    public void handleAddBookTitle() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/book_title_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm đầu sách mới");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookTitleTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setWidth(300);

            BookTitleFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookTitle(null);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                BookTitle newBookTitle = controller.getBookTitle();
                boolean success = bookTitleRepo.insert(newBookTitle);
                if (success) {
                    updateTotalBookTitlesCountAndPagination();
                    pagination.setCurrentPageIndex(0);
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

    @FXML
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
            dialogStage.setWidth(300);

            BookTitleFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookTitle(selected);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                BookTitle updatedBookTitle = controller.getBookTitle();
                boolean success = bookTitleRepo.update(updatedBookTitle);
                if (success) {
                    updateTotalBookTitlesCountAndPagination();
                    pagination.setCurrentPageIndex(0);
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

    @FXML
    public void handleDeleteBookTitle() {
        BookTitle selected = bookTitleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn đầu sách để xóa.");
            return;
        }
        boolean confirmed = confirmDialog("Bạn có chắc muốn xóa đầu sách này?");
        if (confirmed) {
            if (bookTitleRepo.delete(selected.getTitleId())) {
                updateTotalBookTitlesCountAndPagination();
                pagination.setCurrentPageIndex(0);
                showInfo("Xóa đầu sách thành công.");
                loadInventories();
                clearBookCopies();
            } else {
                showWarning("Xóa đầu sách thất bại.");
            }
        }
    }

    @FXML
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
            dialogStage.setWidth(300);

            InventoryFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setInventory(null);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Inventory newInventory = controller.getInventory();
                boolean success = inventoryRepo.insert(newInventory);
                if (success) {
                    loadInventories();
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

    @FXML
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
            dialogStage.setWidth(300);

            InventoryFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setInventory(selected);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Inventory updatedInventory = controller.getInventory();
                boolean success = inventoryRepo.update(updatedInventory);
                if (success) {
                    loadInventories();
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

    @FXML
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
                loadInventories();
                clearBookCopies();
            } else {
                showWarning("Xóa kho thất bại.");
            }
        }
    }

    // BookCopy CRUD handlers

    @FXML
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
            dialogStage.setWidth(300);

            BookCopyFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookCopy(null);

            BookTitle selectedBook = bookTitleTable.getSelectionModel().getSelectedItem();
            controller.setSelectedBookTitle(selectedBook);

            Inventory selectedInv = inventoryTable.getSelectionModel().getSelectedItem();
            controller.setSelectedInventory(selectedInv);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                BookCopy newCopy = controller.getBookCopy();
                boolean success = bookCopyRepo.insert(newCopy);
                if (success) {
                    updateTotalBookCopiesCountAndPagination();
                    bookCopyPagination.setCurrentPageIndex(0);
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

    @FXML
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
            dialogStage.setWidth(300);

            BookCopyFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookCopy(selected);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                BookCopy updatedCopy = controller.getBookCopy();
                boolean success = bookCopyRepo.update(updatedCopy);
                if (success) {
                    updateTotalBookCopiesCountAndPagination();
                    bookCopyPagination.setCurrentPageIndex(0);
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

    @FXML
    public void handleDeleteBookCopy() {
        BookCopy selected = bookCopyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn bản sao để xóa.");
            return;
        }
        boolean confirmed = confirmDialog("Bạn có chắc muốn xóa bản sao sách này?");
        if (confirmed) {
            if (bookCopyRepo.delete(selected.getCopyId())) {
                updateTotalBookCopiesCountAndPagination();
                bookCopyPagination.setCurrentPageIndex(0);
                showInfo("Xóa bản sao thành công.");
                loadInventories();
            } else {
                showWarning("Xóa bản sao thất bại.");
            }
        }
    }

    // ===========================
    // ========== Helpers ========
    // ===========================

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText(msg);
        alert.showAndWait();
    }

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
