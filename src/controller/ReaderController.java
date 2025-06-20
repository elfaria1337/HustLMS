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
import model.*;
import repo.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReaderController {

    @FXML private TextField searchField;
    @FXML private TableView<Reader> readerTable;
    @FXML private TableColumn<Reader, Integer> colId;
    @FXML private TableColumn<Reader, String> colFullName;
    @FXML private TableColumn<Reader, String> colBirthDate;
    @FXML private TableColumn<Reader, String> colAddress;
    @FXML private TableColumn<Reader, String> colPhone;
    @FXML private TableColumn<Reader, String> colEmail;

    // Lịch sử
    @FXML private TableView<Loan> loanHistoryTable;
    @FXML private TableColumn<Loan, Integer> colLoanId;
    @FXML private TableColumn<Loan, String> colLoanDate;
    @FXML private TableColumn<Loan, String> colDueDate;

    @FXML private TableView<Fine> fineHistoryTable;
    @FXML private TableColumn<Fine, Integer> colFineId;
    @FXML private TableColumn<Fine, String> colFineDate;
    @FXML private TableColumn<Fine, String> colFineReason;

    @FXML private TableView<Invoice> invoiceHistoryTable;
    @FXML private TableColumn<Invoice, Integer> colInvoiceId;
    @FXML private TableColumn<Invoice, String> colInvoiceDate;
    @FXML private TableColumn<Invoice, BigDecimal> colInvoiceAmount;

    private ReaderRepository readerRepo = new ReaderRepository();
    private AccountRepository accountRepo = new AccountRepository();
    private LoanRepository loanRepo = new LoanRepository();
    private FineRepository fineRepo = new FineRepository();
    private InvoiceRepository invoiceRepo = new InvoiceRepository();

    private ObservableList<Reader> readerList = FXCollections.observableArrayList();
    private ObservableList<Loan> loanList = FXCollections.observableArrayList();
    private ObservableList<Fine> fineList = FXCollections.observableArrayList();
    private ObservableList<Invoice> invoiceList = FXCollections.observableArrayList();

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // Độc giả
        colId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getReaderId()));
        colFullName.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getFullName()));
        colBirthDate.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getBirthDate().format(dateFormatter)));
        colAddress.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAddress()));
        colPhone.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getPhone()));
        colEmail.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getEmail()));

        // Lịch sử mượn
        colLoanId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getLoanId()));
        colLoanDate.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getLoanDate().format(dateFormatter)));
        colDueDate.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDueDate().format(dateFormatter)));

        // Lịch sử vi phạm
        colFineId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getFineId()));
        colFineDate.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getFineDate().format(dateFormatter)));
        colFineReason.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getReason()));

        // Lịch sử thanh toán
        colInvoiceId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getInvoiceId()));
        colInvoiceDate.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getInvoiceDate().format(dateFormatter)));
        colInvoiceAmount.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAmount()));

        // Tải danh sách độc giả ban đầu
        loadReaders();

        // Tìm kiếm theo tên hoặc điện thoại
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                loadReaders();
            } else {
                List<Reader> filtered = readerRepo.searchByNameOrPhone(newVal.trim());
                readerList.setAll(filtered);
                readerTable.setItems(readerList);
            }
        });

        // Khi chọn độc giả, load lịch sử tương ứng
        readerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int readerId = newVal.getReaderId();
                loadLoanHistory(readerId);
                loadFineHistory(readerId);
                loadInvoiceHistory(readerId);
            } else {
                loanHistoryTable.getItems().clear();
                fineHistoryTable.getItems().clear();
                invoiceHistoryTable.getItems().clear();
            }
        });
    }

    private void loadReaders() {
        List<Reader> readers = readerRepo.findAll();
        readerList.setAll(readers);
        readerTable.setItems(readerList);
    }

    private void loadLoanHistory(int readerId) {
        List<Loan> loans = loanRepo.findByReaderId(readerId);
        loanList.setAll(loans);
        loanHistoryTable.setItems(loanList);
    }

    private void loadFineHistory(int readerId) {
        List<Fine> fines = fineRepo.findByReaderId(readerId);
        fineList.setAll(fines);
        fineHistoryTable.setItems(fineList);
    }

    private void loadInvoiceHistory(int readerId) {
        List<Invoice> invoices = invoiceRepo.findByReaderId(readerId);
        invoiceList.setAll(invoices);
        invoiceHistoryTable.setItems(invoiceList);
    }

    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/reader_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Thêm độc giả mới");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(readerTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setWidth(350);

            ReaderFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setReader(new Reader()); // tạo mới
            controller.setAccount(null);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Reader newReader = controller.getReader();
                Account newAccount = controller.getAccount();

                if (readerRepo.insert(newReader)) {
                    newAccount.setReaderId(newReader.getReaderId());
                    if (accountRepo.insert(newAccount)) {
                        loadReaders();
                    } else {
                        showAlert("Lưu tài khoản thất bại.");
                    }
                } else {
                    showAlert("Lưu độc giả thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit() {
        Reader selected = readerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn độc giả để sửa.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/reader_form.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sửa thông tin độc giả");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(readerTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setWidth(350);

            ReaderFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setReader(selected);

            Account account = accountRepo.findByReaderId(selected.getReaderId());
            controller.setAccount(account);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Reader updatedReader = controller.getReader();
                Account updatedAccount = controller.getAccount();

                boolean successReader = readerRepo.update(updatedReader);
                boolean successAccount = false;

                if (updatedAccount.getAccountId() != 0) {
                    successAccount = accountRepo.update(updatedAccount);
                } else {
                    updatedAccount.setReaderId(updatedReader.getReaderId());
                    successAccount = accountRepo.insert(updatedAccount);
                }

                if (successReader && successAccount) {
                    loadReaders();
                } else {
                    showAlert("Cập nhật thất bại.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleToggleStatus() {
        Reader selected = readerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn độc giả để khóa/mở tài khoản.");
            return;
        }
        Account account = accountRepo.findByReaderId(selected.getReaderId());
        if (account == null) {
            showAlert("Không tìm thấy tài khoản của độc giả này.");
            return;
        }
        String currentStatus = account.getStatus();
        String newStatus = "active".equalsIgnoreCase(currentStatus) ? "locked" : "active";
        account.setStatus(newStatus);
        boolean success = accountRepo.update(account);
        if (success) {
            showAlert("Đã " + (newStatus.equals("active") ? "mở khóa" : "khóa") + " tài khoản độc giả.");
        } else {
            showAlert("Cập nhật trạng thái tài khoản thất bại.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
