package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Account;
import model.Reader;

public class ReaderFormController {

    @FXML private TextField fullNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    private Stage dialogStage;
    private Reader reader; // đối tượng Reader đang sửa hoặc tạo mới
    private Account account; // đối tượng Account liên quan
    private boolean saveClicked = false;

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("reader"));
        roleComboBox.setValue("reader");
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
        if (reader != null) {
            fullNameField.setText(reader.getFullName());
            birthDatePicker.setValue(reader.getBirthDate());
            addressField.setText(reader.getAddress());
            phoneField.setText(reader.getPhone());
            emailField.setText(reader.getEmail());
        }
    }

    public void setAccount(Account account) {
        this.account = account;
        if (account != null) {
            usernameField.setText(account.getUsername());
            passwordField.setText(account.getPassword());
            roleComboBox.setValue(account.getRole());
        } else {
            roleComboBox.setValue("reader"); // mặc định
        }
        usernameField.setDisable(false);
        passwordField.setDisable(false);
    }

    public Reader getReader() {
        return this.reader;
    }

    public Account getAccount() {
        return this.account;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            reader.setFullName(fullNameField.getText());
            reader.setBirthDate(birthDatePicker.getValue());
            reader.setAddress(addressField.getText());
            reader.setPhone(phoneField.getText());
            reader.setEmail(emailField.getText());

            if (account == null) account = new Account();
            account.setUsername(usernameField.getText());
            account.setPassword(passwordField.getText()); // Lưu mật khẩu nên mã hoá trước nhé
            account.setRole("reader");
            account.setStatus("active");
            // Liên kết reader_id sẽ gán ở controller gọi sau khi insert Reader

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

        if (fullNameField.getText() == null || fullNameField.getText().trim().isEmpty()) {
            errorMessage += "Họ tên không được để trống!\n";
        }
        if (birthDatePicker.getValue() == null) {
            errorMessage += "Ngày sinh chưa chọn!\n";
        }
        if (addressField.getText() == null || addressField.getText().trim().isEmpty()) {
            errorMessage += "Địa chỉ không được để trống!\n";
        }
        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            errorMessage += "Số điện thoại không được để trống!\n";
        }
        if (usernameField.getText() == null || usernameField.getText().trim().isEmpty()) {
            errorMessage += "Username không được để trống!\n";
        }
        if (passwordField.getText() == null || passwordField.getText().trim().isEmpty()) {
            errorMessage += "Password không được để trống!\n";
        }
        if (roleComboBox.getValue() == null || roleComboBox.getValue().trim().isEmpty()) {
            errorMessage += "Role chưa chọn!\n";
        }
        // Có thể thêm regex kiểm tra phone, email, mật khẩu mạnh...

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Lỗi nhập liệu");
            alert.setHeaderText("Thông tin nhập không hợp lệ");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
