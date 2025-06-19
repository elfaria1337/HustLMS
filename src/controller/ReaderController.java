package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Account;
import model.Reader;
import repo.AccountRepository;
import repo.ReaderRepository;

import java.io.IOException;
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

    // Các bảng lịch sử
    @FXML private TableView<?> loanHistoryTable;
    @FXML private TableView<?> fineHistoryTable;
    @FXML private TableView<?> invoiceHistoryTable;

    private ReaderRepository readerRepo = new ReaderRepository();
    private AccountRepository accountRepo = new AccountRepository();

    private ObservableList<Reader> readerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup column mappings
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getReaderId()).asObject());
        colFullName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName()));
        colBirthDate.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colAddress.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));
        colPhone.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPhone()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                loadReaders();
            } else {
                List<Reader> filtered = readerRepo.searchByNameOrPhone(newValue.trim());
                readerList.setAll(filtered);
            }
        });
        loadReaders();
    }

    private void loadReaders() {
        List<Reader> readers = readerRepo.findAll();
        readerList.setAll(readers);
        readerTable.setItems(readerList);
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
            dialogStage.setWidth(300);

            ReaderFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setReader(new Reader()); // tạo mới
            controller.setAccount(null);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Reader newReader = controller.getReader();
                Account newAccount = controller.getAccount();

                if (readerRepo.insert(newReader)) {
                    // gán reader_id vào account rồi lưu
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
            dialogStage.setWidth(300);

            ReaderFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setReader(selected);

            // --- Đây là phần quan trọng ---
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
        String newStatus;
        if ("active".equalsIgnoreCase(currentStatus)) {
            newStatus = "locked";
        } else {
            newStatus = "active";
        }
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

    // Các phương thức load lịch sử mượn, vi phạm, thanh toán khi chọn độc giả (chưa viết)
}
