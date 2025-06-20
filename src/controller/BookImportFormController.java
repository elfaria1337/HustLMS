package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.BookImport;
import model.Supplier;
import repo.SupplierRepository;

import java.time.LocalDate;
import java.util.List;

public class BookImportFormController {

    @FXML private DatePicker orderDatePicker;
    @FXML private DatePicker receiveDatePicker;
    @FXML private ComboBox<Supplier> supplierComboBox;

    private Stage dialogStage;
    private BookImport bookImport;
    private boolean saveClicked = false;

    private SupplierRepository supplierRepo = new SupplierRepository();
    private ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
    private FilteredList<Supplier> filteredSuppliers;

    @FXML
    public void initialize() {
        // Load suppliers từ repo
        loadSuppliers();

        // Khởi tạo FilteredList với danh sách đầy đủ
        filteredSuppliers = new FilteredList<>(supplierList, p -> true);
        supplierComboBox.setItems(filteredSuppliers);

        // Cho phép nhập text để tìm kiếm
        supplierComboBox.setEditable(true);

        // Hiển thị tên nhà cung cấp thay vì toString()
        supplierComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Supplier item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCompanyName());
            }
        });
        supplierComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Supplier item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getCompanyName());
            }
        });

        // Thêm listener để filter supplier khi nhập text
        supplierComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = supplierComboBox.getEditor();
            final Supplier selected = supplierComboBox.getSelectionModel().getSelectedItem();

            if (selected == null || !selected.getCompanyName().equals(editor.getText())) {
                filteredSuppliers.setPredicate(supplier -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return supplier.getCompanyName().toLowerCase().contains(lowerCaseFilter) ||
                           (supplier.getContactPerson() != null && supplier.getContactPerson().toLowerCase().contains(lowerCaseFilter));
                });
                supplierComboBox.show();
            }
        });
    }

    private void loadSuppliers() {
        List<Supplier> suppliers = supplierRepo.findAll();
        supplierList.setAll(suppliers);
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setBookImport(BookImport bookImport) {
        this.bookImport = bookImport;
        if (bookImport != null) {
            if (bookImport.getOrderDate() != null) {
                orderDatePicker.setValue(bookImport.getOrderDate());
            }
            if (bookImport.getReceiveDate() != null) {
                receiveDatePicker.setValue(bookImport.getReceiveDate());
            }
            // Chọn nhà cung cấp theo ID
            for (Supplier s : supplierList) {
                if (s.getSupplierId() == bookImport.getSupplierId()) {
                    supplierComboBox.setValue(s);
                    break;
                }
            }
        }
    }

    public BookImport getBookImport() {
        return this.bookImport;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            LocalDate orderDate = orderDatePicker.getValue();
            LocalDate receiveDate = receiveDatePicker.getValue();

            if (receiveDate.isBefore(orderDate)) {
                showAlert("Ngày nhận phải sau hoặc bằng ngày đặt.");
                return;
            }

            if (bookImport == null) {
                bookImport = new BookImport();
            }

            bookImport.setOrderDate(orderDate);
            bookImport.setReceiveDate(receiveDate);

            Supplier selectedSupplier = supplierComboBox.getValue();
            if (selectedSupplier != null) {
                bookImport.setSupplierId(selectedSupplier.getSupplierId());
            }

            saveClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (orderDatePicker.getValue() == null) {
            errorMessage += "Ngày đặt không được để trống!\n";
        }
        if (receiveDatePicker.getValue() == null) {
            errorMessage += "Ngày nhận không được để trống!\n";
        }
        if (supplierComboBox.getValue() == null) {
            errorMessage += "Vui lòng chọn nhà cung cấp!\n";
        }

        if (!errorMessage.isEmpty()) {
            showAlert(errorMessage);
            return false;
        }
        return true;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Lỗi nhập liệu");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
