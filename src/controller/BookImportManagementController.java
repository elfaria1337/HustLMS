package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.BookImport;
import model.BookImportDetail;
import model.Supplier;
import repo.BookImportDetailRepository;
import repo.BookImportRepository;
import repo.SupplierRepository;

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

    // Repositories
    private SupplierRepository supplierRepo = new SupplierRepository();
    private BookImportRepository bookImportRepo = new BookImportRepository();
    private BookImportDetailRepository bookImportDetailRepo = new BookImportDetailRepository();

    private ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
    private ObservableList<BookImport> bookImportList = FXCollections.observableArrayList();
    private ObservableList<BookImportDetail> bookImportDetailList = FXCollections.observableArrayList();

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
            // TODO: lấy tên sách theo titleId (cần tạo repo BookTitleRepository)
            return new ReadOnlyObjectWrapper<>("Chưa có");
        });
        colImportRef.setCellValueFactory(cellData -> {
            int importId = cellData.getValue().getImportId();
            BookImport b = bookImportRepo.findById(importId);
            return new ReadOnlyObjectWrapper<>(b != null ? String.valueOf(b.getImportId()) : "N/A");
        });

        loadData();
    }

    private void loadData() {
        List<Supplier> suppliers = supplierRepo.findAll();
        supplierList.setAll(suppliers);
        supplierTable.setItems(supplierList);

        List<BookImport> imports = bookImportRepo.findAll();
        bookImportList.setAll(imports);
        bookImportTable.setItems(bookImportList);

        List<BookImportDetail> details = bookImportDetailRepo.findAll();
        bookImportDetailList.setAll(details);
        bookImportDetailTable.setItems(bookImportDetailList);
    }

    // ========== Supplier handlers ==========
    public void handleAddSupplier() {
        showInfo("Chức năng thêm nhà cung cấp chưa triển khai.");
    }

    public void handleEditSupplier() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn nhà cung cấp để sửa.");
            return;
        }
        showInfo("Chức năng sửa nhà cung cấp chưa triển khai.");
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
                loadData();
            } else {
                showWarning("Xóa nhà cung cấp thất bại.");
            }
        }
    }

    // ========== BookImport handlers ==========
    public void handleAddBookImport() {
        showInfo("Chức năng thêm phiếu nhập chưa triển khai.");
    }

    public void handleEditBookImport() {
        BookImport selected = bookImportTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn phiếu nhập để sửa.");
            return;
        }
        showInfo("Chức năng sửa phiếu nhập chưa triển khai.");
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

    // ========== BookImportDetail handlers ==========
    public void handleAddBookImportDetail() {
        showInfo("Chức năng thêm chi tiết nhập chưa triển khai.");
    }

    public void handleEditBookImportDetail() {
        BookImportDetail selected = bookImportDetailTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn chi tiết nhập để sửa.");
            return;
        }
        showInfo("Chức năng sửa chi tiết nhập chưa triển khai.");
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

    // === Helper methods ===

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
