package controller.reader;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Fine;
import model.Invoice;
import repo.FineRepository;
import repo.InvoiceRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class FinePaymentController {

    @FXML private TableView<Fine> fineTable;
    @FXML private TableColumn<Fine, Integer> colFineId;
    @FXML private TableColumn<Fine, String> colReason;
    @FXML private TableColumn<Fine, String> colAmount;
    @FXML private TableColumn<Fine, String> colIssueDate;

    @FXML private TableView<Invoice> invoiceTable;
    @FXML private TableColumn<Invoice, Integer> colInvoiceId;
    @FXML private TableColumn<Invoice, String> colPaymentMethod;
    @FXML private TableColumn<Invoice, String> colPaymentDate;
    @FXML private TableColumn<Invoice, String> colAmountPaid;

    private FineRepository fineRepo = new FineRepository();
    private InvoiceRepository invoiceRepo = new InvoiceRepository();

    private ObservableList<Fine> fineList = FXCollections.observableArrayList();
    private ObservableList<Invoice> invoiceList = FXCollections.observableArrayList();

    private int currentReaderId = 1; // TODO: lấy từ session đăng nhập

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        colFineId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getFineId()).asObject());
        colReason.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReason()));
        colAmount.setCellValueFactory(data -> new SimpleStringProperty(String.format("%,.0f", data.getValue().getAmount())));
        colIssueDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIssueDate().format(formatter)));

        colInvoiceId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getInvoiceId()).asObject());
        colPaymentMethod.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPaymentMethod()));
        colPaymentDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPaymentDate().format(formatter)));
        colAmountPaid.setCellValueFactory(data -> new SimpleStringProperty(String.format("%,.0f", data.getValue().getAmount())));

        fineTable.setItems(fineList);
        invoiceTable.setItems(invoiceList);

        loadFines();
        // Tự động load hóa đơn khi chọn phạt
        fineTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadInvoices(newVal.getFineId());
            } else {
                invoiceList.clear();
            }
        });
    }

    private void loadFines() {
        List<Fine> fines = fineRepo.findByReaderId(currentReaderId);
        fineList.setAll(fines);
    }

    private void loadInvoices(int fineId) {
        List<Invoice> invoices = invoiceRepo.findByFineId(fineId);
        invoiceList.setAll(invoices);
    }

    @FXML
    private void handlePayFine() {
        Fine selectedFine = fineTable.getSelectionModel().getSelectedItem();
        if (selectedFine == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một khoản phạt để thanh toán.");
            return;
        }

        // TODO: hiện form hoặc xử lý thanh toán
        showAlert(Alert.AlertType.INFORMATION, "Thanh toán", "Chức năng thanh toán sẽ được triển khai sau.");
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) fineTable.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
