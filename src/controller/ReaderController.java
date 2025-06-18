package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Reader;
import repo.ReaderRepository;

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

        loadReaders();
    }

    private void loadReaders() {
        List<Reader> readers = readerRepo.findAll();
        readerList.setAll(readers);
        readerTable.setItems(readerList);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText();
        if (keyword == null || keyword.isEmpty()) {
            loadReaders();
        } else {
            List<Reader> filtered = readerRepo.searchByNameOrPhone(keyword);
            readerList.setAll(filtered);
        }
    }

    @FXML
    private void handleAdd() {
        // Mở dialog thêm mới độc giả (cần tạo form riêng)
    }

    @FXML
    private void handleEdit() {
        Reader selected = readerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn độc giả để sửa.");
            return;
        }
        // Mở dialog sửa độc giả với dữ liệu selected (cần tạo form riêng)
    }

    @FXML
    private void handleToggleStatus() {
        Reader selected = readerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn độc giả để khóa/mở tài khoản.");
            return;
        }
        // Đổi trạng thái account của độc giả này (cần Repository Account, logic riêng)
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Các phương thức load lịch sử mượn, vi phạm, thanh toán khi chọn độc giả (chưa viết)
}
