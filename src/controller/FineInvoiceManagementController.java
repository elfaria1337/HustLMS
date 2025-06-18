package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Fine;
import model.Invoice;
import repo.FineRepository;
import repo.InvoiceRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class FineInvoiceManagementController {

    // Fine
    @FXML private TableView<Fine> fineTable;
    @FXML private TableColumn<Fine, Integer> colFineId;
    @FXML private TableColumn<Fine, String> colReason;
    @FXML private TableColumn<Fine, String> colAmount;
    @FXML private TableColumn<Fine, String> colIssueDate;
    @FXML private TableColumn<Fine, Integer> colReaderIdFine;

    // Invoice
    @FXML private TableView<Invoice> invoiceTable;
    @FXML private TableColumn<Invoice, Integer> colInvoiceId;
    @FXML private TableColumn<Invoice, String> colAmountInvoice;
    @FXML private TableColumn<Invoice, String> colPaymentMethod;
    @FXML private TableColumn<Invoice, String> colPaymentDate;
    @FXML private TableColumn<Invoice, Integer> colReaderIdInvoice;
    @FXML private TableColumn<Invoice, Integer> colFineIdInvoice;

    private FineRepository fineRepo = new FineRepository();
    private InvoiceRepository invoiceRepo = new InvoiceRepository();

    private ObservableList<Fine> fineList = FXCollections.observableArrayList();
    private ObservableList<Invoice> invoiceList = FXCollections.observableArrayList();

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // Fine columns
        colFineId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getFineId()));
        colReason.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReason()));
        colAmount.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAmount().toString()));
        colIssueDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getIssueDate().format(dateFormatter)));
        colReaderIdFine.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReaderId()));

        // Invoice columns
        colInvoiceId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getInvoiceId()));
        colAmountInvoice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAmount().toString()));
        colPaymentMethod.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPaymentMethod()));
        colPaymentDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPaymentDate().format(dateFormatter)));
        colReaderIdInvoice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReaderId()));
        colFineIdInvoice.setCellValueFactory(cellData -> {
            Integer fineId = cellData.getValue().getFineId();
            return new ReadOnlyObjectWrapper<>(fineId != null ? fineId : 0);
        });

        loadData();
    }

    private void loadData() {
        List<Fine> fines = fineRepo.findAll();
        fineList.setAll(fines);
        fineTable.setItems(fineList);

        List<Invoice> invoices = invoiceRepo.findAll();
        invoiceList.setAll(invoices);
        invoiceTable.setItems(invoiceList);
    }

    // ===== Fine handlers =====
    public void handleAddFine() {
        showInfo("Chức năng thêm phạt chưa triển khai.");
    }

    public void handleEditFine() {
        Fine selected = fineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn phạt để sửa.");
            return;
        }
        showInfo("Chức năng sửa phạt chưa triển khai.");
    }

    public void handleDeleteFine() {
        Fine selected = fineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn phạt để xóa.");
            return;
        }
        if (confirmDialog("Bạn có chắc muốn xóa phạt này?")) {
            if (fineRepo.delete(selected.getFineId())) {
                showInfo("Xóa phạt thành công.");
                loadData();
            } else {
                showWarning("Xóa phạt thất bại.");
            }
        }
    }

    // ===== Invoice handlers =====
    public void handleAddInvoice() {
        showInfo("Chức năng thêm hóa đơn chưa triển khai.");
    }

    public void handleEditInvoice() {
        Invoice selected = invoiceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn hóa đơn để sửa.");
            return;
        }
        showInfo("Chức năng sửa hóa đơn chưa triển khai.");
    }

    public void handleDeleteInvoice() {
        Invoice selected = invoiceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn hóa đơn để xóa.");
            return;
        }
        if (confirmDialog("Bạn có chắc muốn xóa hóa đơn này?")) {
            if (invoiceRepo.delete(selected.getInvoiceId())) {
                showInfo("Xóa hóa đơn thành công.");
                loadData();
            } else {
                showWarning("Xóa hóa đơn thất bại.");
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

    private boolean confirmDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setContentText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
