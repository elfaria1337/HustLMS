package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.BookImport;
import model.BookImportDetail;
import model.BookTitle;
import model.Supplier;
import repo.BookImportDetailRepository;
import repo.BookImportRepository;
import repo.BookTitleRepository;
import repo.SupplierRepository;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class BookImportManagementController {

    // Supplier
    @FXML private TableView<Supplier> supplierTable;
    @FXML private TableColumn<Supplier, Integer> colSupplierId;
    @FXML private TableColumn<Supplier, String> colCompanyName;
    @FXML private TableColumn<Supplier, String> colPhone;
    @FXML private TableColumn<Supplier, String> colEmail;
    @FXML private TableColumn<Supplier, String> colAddress;
    @FXML private TableColumn<Supplier, String> colContactPerson;
    @FXML private TextField searchSupplierField;
    private FilteredList<Supplier> filteredSuppliers;

    // BookImport
    @FXML private TableView<BookImport> bookImportTable;
    @FXML private TableColumn<BookImport, Integer> colImportId;
    @FXML private TableColumn<BookImport, String> colOrderDate;
    @FXML private TableColumn<BookImport, String> colReceiveDate;
    @FXML private TableColumn<BookImport, String> colSupplierName;

    // BookImportDetail
    @FXML private TableView<BookImportDetail> bookImportDetailTable;
    @FXML private TableColumn<BookImportDetail, Integer> colImportDetailId;
    @FXML private TableColumn<BookImportDetail, Integer> colQuantity;
    @FXML private TableColumn<BookImportDetail, String> colPricePerUnit;
    @FXML private TableColumn<BookImportDetail, String> colTitleName;
    @FXML private TableColumn<BookImportDetail, String> colImportRef;

    // Phân trang cho BookImport
    @FXML private Button btnPrevBookImport;
    @FXML private Button btnNextBookImport;
    @FXML private Label lblPageBookImport;

    // Phân trang cho BookImportDetail
    @FXML private Button btnPrevBookImportDetail;
    @FXML private Button btnNextBookImportDetail;
    @FXML private Label lblPageBookImportDetail;

    // Repositories
    private SupplierRepository supplierRepo = new SupplierRepository();
    private BookImportRepository bookImportRepo = new BookImportRepository();
    private BookImportDetailRepository bookImportDetailRepo = new BookImportDetailRepository();
    private BookTitleRepository bookTitleRepo = new BookTitleRepository();

    private ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
    private ObservableList<BookImport> bookImportList = FXCollections.observableArrayList();
    private ObservableList<BookImportDetail> bookImportDetailList = FXCollections.observableArrayList();

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Phân trang
    private static final int PAGE_SIZE = 15;

    private int currentBookImportPage = 1;
    private int totalBookImportPages = 1;

    private int currentBookImportDetailPage = 1;
    private int totalBookImportDetailPages = 1;

    @FXML
    public void initialize() {
        // Supplier columns
        colSupplierId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getSupplierId()));
        colCompanyName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCompanyName()));
        colPhone.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPhone()));
        colEmail.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getEmail()));
        colAddress.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAddress()));
        colContactPerson.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getContactPerson()));

        // BookImport columns
        colImportId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getImportId()));
        colOrderDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getOrderDate().format(dateFormatter)));
        colReceiveDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReceiveDate().format(dateFormatter)));
        colSupplierName.setCellValueFactory(cellData -> {
            int supId = cellData.getValue().getSupplierId();
            Supplier s = supplierRepo.findById(supId);
            return new ReadOnlyObjectWrapper<>(s != null ? s.getCompanyName() : "N/A");
        });

        // BookImportDetail columns
        colImportDetailId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getImportDetailId()));
        colQuantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantity()));
        colPricePerUnit.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPricePerUnit().toString()));
        colTitleName.setCellValueFactory(cellData -> {
            int titleId = cellData.getValue().getTitleId();
            BookTitle bookTitle = bookTitleRepo.findById(titleId);
            String titleName = bookTitle != null ? bookTitle.getTitleName() : "Chưa có";
            return new ReadOnlyObjectWrapper<>(titleName);
        });
        colImportRef.setCellValueFactory(cellData -> {
            int importId = cellData.getValue().getImportId();
            BookImport b = bookImportRepo.findById(importId);
            return new ReadOnlyObjectWrapper<>(b != null ? String.valueOf(b.getImportId()) : "N/A");
        });

        // Load suppliers
        supplierList.setAll(supplierRepo.findAll());
        filteredSuppliers = new FilteredList<>(supplierList, p -> true);
        supplierTable.setItems(filteredSuppliers);

        searchSupplierField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredSuppliers.setPredicate(supplier -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplier.getCompanyName() != null && supplier.getCompanyName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (supplier.getContactPerson() != null && supplier.getContactPerson().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        // Load dữ liệu BookImport và BookImportDetail
        loadData();
    }

    private void loadData() {
        // Load toàn bộ danh sách
        List<BookImport> imports = bookImportRepo.findAll();
        bookImportList.setAll(imports);

        List<BookImportDetail> details = bookImportDetailRepo.findAll();
        bookImportDetailList.setAll(details);

        // Reset trang hiện tại
        currentBookImportPage = 1;
        currentBookImportDetailPage = 1;

        updateBookImportTable();
        updateBookImportDetailTable();
    }

    // ===== Phân trang BookImport =====
    private void updateBookImportTable() {
        int totalRecords = bookImportList.size();
        totalBookImportPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
        if (totalBookImportPages == 0) totalBookImportPages = 1;
        if (currentBookImportPage > totalBookImportPages) {
            currentBookImportPage = totalBookImportPages;
        }
        if (currentBookImportPage < 1) {
            currentBookImportPage = 1;
        }
        int fromIndex = (currentBookImportPage - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalRecords);
        List<BookImport> pageItems = bookImportList.subList(fromIndex, toIndex);
        bookImportTable.setItems(FXCollections.observableArrayList(pageItems));

        lblPageBookImport.setText("Trang " + currentBookImportPage + " / " + totalBookImportPages);

        btnPrevBookImport.setDisable(currentBookImportPage <= 1);
        btnNextBookImport.setDisable(currentBookImportPage >= totalBookImportPages);
    }

    @FXML
    private void handlePrevBookImport() {
        if (currentBookImportPage > 1) {
            currentBookImportPage--;
            updateBookImportTable();
        }
    }

    @FXML
    private void handleNextBookImport() {
        if (currentBookImportPage < totalBookImportPages) {
            currentBookImportPage++;
            updateBookImportTable();
        }
    }

    private void loadBookImports() {
        List<BookImport> imports = bookImportRepo.findAll();
        bookImportList.setAll(imports);
        currentBookImportPage = 1;
        updateBookImportTable();
    }

    // ===== Phân trang BookImportDetail =====
    private void updateBookImportDetailTable() {
        int totalRecords = bookImportDetailList.size();
        totalBookImportDetailPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
        if (totalBookImportDetailPages == 0) totalBookImportDetailPages = 1;
        if (currentBookImportDetailPage > totalBookImportDetailPages) {
            currentBookImportDetailPage = totalBookImportDetailPages;
        }
        if (currentBookImportDetailPage < 1) {
            currentBookImportDetailPage = 1;
        }
        int fromIndex = (currentBookImportDetailPage - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalRecords);
        List<BookImportDetail> pageItems = bookImportDetailList.subList(fromIndex, toIndex);
        bookImportDetailTable.setItems(FXCollections.observableArrayList(pageItems));

        lblPageBookImportDetail.setText("Trang " + currentBookImportDetailPage + " / " + totalBookImportDetailPages);

        btnPrevBookImportDetail.setDisable(currentBookImportDetailPage <= 1);
        btnNextBookImportDetail.setDisable(currentBookImportDetailPage >= totalBookImportDetailPages);
    }

    @FXML
    private void handlePrevBookImportDetail() {
        if (currentBookImportDetailPage > 1) {
            currentBookImportDetailPage--;
            updateBookImportDetailTable();
        }
    }

    @FXML
    private void handleNextBookImportDetail() {
        if (currentBookImportDetailPage < totalBookImportDetailPages) {
            currentBookImportDetailPage++;
            updateBookImportDetailTable();
        }
    }

    private void loadBookImportDetails() {
        List<BookImportDetail> details = bookImportDetailRepo.findAll();
        bookImportDetailList.setAll(details);
        currentBookImportDetailPage = 1;
        updateBookImportDetailTable();
    }

    // ===== Supplier handlers =====
    @FXML
    private void handleAddSupplier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/supplier_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm nhà cung cấp mới");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(supplierTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SupplierFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSupplier(null); // Tạo mới supplier

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Supplier newSupplier = controller.getSupplier();
                if (supplierRepo.insert(newSupplier)) {
                    loadSuppliers();
                } else {
                    showAlert("Lỗi", "Không thể lưu nhà cung cấp mới.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form thêm nhà cung cấp.");
        }
    }

    @FXML
    private void handleEditSupplier() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn nhà cung cấp để sửa.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/supplier_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sửa thông tin nhà cung cấp");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(supplierTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SupplierFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSupplier(selected);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Supplier updatedSupplier = controller.getSupplier();
                if (supplierRepo.update(updatedSupplier)) {
                    loadSuppliers();
                } else {
                    showAlert("Lỗi", "Không thể cập nhật nhà cung cấp.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form sửa nhà cung cấp.");
        }
    }

    public void handleDeleteSupplier() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn nhà cung cấp để xóa.");
            return;
        }
        boolean confirmed = confirmDialog("Bạn có chắc muốn xóa nhà cung cấp này?");
        if (confirmed) {
            if (supplierRepo.delete(selected.getSupplierId())) {
                showInfo("Xóa nhà cung cấp thành công.");
                loadSuppliers();
            } else {
                showWarning("Xóa nhà cung cấp thất bại.");
            }
        }
    }

    private void loadSuppliers() {
        List<Supplier> list = supplierRepo.findAll();
        supplierList.setAll(list);
        supplierTable.setItems(supplierList);
    }

    // ===== BookImport handlers =====
    @FXML
    public void handleAddBookImport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/book_import_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm Phiếu nhập sách");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookImportTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setWidth(400);
            dialogStage.setHeight(240);

            BookImportFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookImport(null);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                BookImport newImport = controller.getBookImport();
                if (bookImportRepo.insert(newImport)) {
                    loadBookImports();
                } else {
                    showAlert("Lỗi", "Lưu phiếu nhập thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form thêm phiếu nhập.");
        }
    }

    @FXML
    public void handleEditBookImport() {
        BookImport selected = bookImportTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn phiếu nhập để sửa.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/book_import_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sửa Phiếu nhập sách");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookImportTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setWidth(400);
            dialogStage.setHeight(240);

            BookImportFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookImport(selected);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                BookImport updatedImport = controller.getBookImport();
                if (bookImportRepo.update(updatedImport)) {
                    loadBookImports();
                } else {
                    showAlert("Lỗi", "Cập nhật phiếu nhập thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form sửa phiếu nhập.");
        }
    }

    public void handleDeleteBookImport() {
        BookImport selected = bookImportTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn phiếu nhập để xóa.");
            return;
        }
        boolean confirmed = confirmDialog("Bạn có chắc muốn xóa phiếu nhập này?");
        if (confirmed) {
            if (bookImportRepo.delete(selected.getImportId())) {
                showInfo("Xóa phiếu nhập thành công.");
                loadData();
            } else {
                showWarning("Xóa phiếu nhập thất bại.");
            }
        }
    }

    // ===== BookImportDetail handlers =====
    @FXML
    public void handleAddBookImportDetail() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/book_import_detail_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm chi tiết nhập sách");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookImportDetailTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setWidth(400);

            BookImportDetailFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookImportDetail(null);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                BookImportDetail newDetail = controller.getBookImportDetail();
                if (bookImportDetailRepo.insert(newDetail)) {
                    loadBookImportDetails();
                } else {
                    showAlert("Lỗi", "Lưu chi tiết nhập sách thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở cửa sổ thêm chi tiết nhập sách.");
        }
    }

    @FXML
    public void handleEditBookImportDetail() {
        BookImportDetail selected = bookImportDetailTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn chi tiết nhập để sửa.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/book_import_detail_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sửa chi tiết nhập sách");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookImportDetailTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setWidth(400);

            BookImportDetailFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBookImportDetail(selected);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                BookImportDetail updatedDetail = controller.getBookImportDetail();
                if (bookImportDetailRepo.update(updatedDetail)) {
                    loadBookImportDetails();
                } else {
                    showAlert("Lỗi", "Cập nhật chi tiết nhập sách thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở cửa sổ sửa chi tiết nhập sách.");
        }
    }

    public void handleDeleteBookImportDetail() {
        BookImportDetail selected = bookImportDetailTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn chi tiết nhập để xóa.");
            return;
        }
        boolean confirmed = confirmDialog("Bạn có chắc muốn xóa chi tiết nhập này?");
        if (confirmed) {
            if (bookImportDetailRepo.delete(selected.getImportDetailId())) {
                showInfo("Xóa chi tiết nhập thành công.");
                loadData();
            } else {
                showWarning("Xóa chi tiết nhập thất bại.");
            }
        }
    }

    // ===== Helper methods =====

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
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
