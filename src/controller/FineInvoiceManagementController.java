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
import model.Fine;
import model.Invoice;
import repo.FineRepository;
import repo.InvoiceRepository;

import java.io.IOException;
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
        // Thiết lập cột bảng Fine
        colFineId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getFineId()));
        colReason.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReason()));
        colAmount.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAmount().toString()));
        colIssueDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getIssueDate().format(dateFormatter)));
        colReaderIdFine.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReaderId()));

        // Thiết lập cột bảng Invoice
        colInvoiceId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getInvoiceId()));
        colAmountInvoice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAmount().toString()));
        colPaymentMethod.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPaymentMethod()));
        colPaymentDate.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPaymentDate() != null) {
                return new ReadOnlyObjectWrapper<>(cellData.getValue().getPaymentDate().format(dateFormatter));
            } else {
                return new ReadOnlyObjectWrapper<>("");
            }
        });
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
        try {
            Fine newFine = new Fine();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/fine_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm mới phạt");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(fineTable.getScene().getWindow());
            dialogStage.setScene(new Scene(page));
            dialogStage.setWidth(300);

            FineFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setFine(newFine);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Fine savedFine = controller.getFine();
                if (fineRepo.insert(savedFine)) {
                    showInfo("Thêm phạt thành công.");
                    loadFines();
                } else {
                    showWarning("Thêm phạt thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể mở form thêm phạt.");
        }
    }

    public void handleEditFine() {
        Fine selected = fineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn phạt để sửa.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/fine_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sửa phạt");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(fineTable.getScene().getWindow());
            dialogStage.setScene(new Scene(page));
            dialogStage.setWidth(300);

            FineFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setFine(selected);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Fine updatedFine = controller.getFine();
                if (fineRepo.update(updatedFine)) {
                    showInfo("Cập nhật phạt thành công.");
                    loadFines();
                } else {
                    showWarning("Cập nhật phạt thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể mở form sửa phạt.");
        }
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

    private void loadFines() {
        List<Fine> fines = fineRepo.findAll();
        fineList.setAll(fines);
        fineTable.setItems(fineList);
    }

    // ===== Invoice handlers =====
    public void handleAddInvoice() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/invoice_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm hóa đơn mới");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(invoiceTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setWidth(370);

            InvoiceFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setInvoice(new Invoice());

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Invoice newInvoice = controller.getInvoice();
                if (invoiceRepo.insert(newInvoice)) {
                    loadInvoices();
                    showInfo("Thêm hóa đơn thành công.");
                } else {
                    showWarning("Thêm hóa đơn thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi hệ thống", "Không thể mở form thêm hóa đơn.");
        }
    }

    public void handleEditInvoice() {
        Invoice selected = invoiceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn hóa đơn để sửa.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/invoice_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sửa thông tin hóa đơn");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(invoiceTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setWidth(370);

            InvoiceFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setInvoice(selected);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Invoice updatedInvoice = controller.getInvoice();
                if (invoiceRepo.update(updatedInvoice)) {
                    loadInvoices();
                    showInfo("Cập nhật hóa đơn thành công.");
                } else {
                    showWarning("Cập nhật hóa đơn thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi hệ thống", "Không thể mở form sửa hóa đơn.");
        }
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

    private void loadInvoices() {
        List<Invoice> invoices = invoiceRepo.findAll();
        invoiceList.setAll(invoices);
        invoiceTable.setItems(invoiceList);

        colInvoiceId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getInvoiceId()));
        colAmountInvoice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAmount().toString()));
        colPaymentMethod.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPaymentMethod()));
        colPaymentDate.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPaymentDate() != null) {
                return new ReadOnlyObjectWrapper<>(cellData.getValue().getPaymentDate().format(dateFormatter));
            } else {
                return new ReadOnlyObjectWrapper<>("");
            }
        });
        colReaderIdInvoice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReaderId()));
        colFineIdInvoice.setCellValueFactory(cellData -> {
            Integer fineId = cellData.getValue().getFineId();
            return new ReadOnlyObjectWrapper<>(fineId != null ? fineId : 0);
        });
    }

    // ===== Helper methods =====
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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
