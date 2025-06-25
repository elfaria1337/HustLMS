package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Account;
import model.Reader;
import repo.AccountRepository;
import repo.ReaderRepository;

public class ReaderFormController {

    @FXML private TextField fullNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private Label lblPhoneWarning;
    @FXML private TextField emailField;
    @FXML private Label lblEmailWarning;

    @FXML private TextField usernameField;
    @FXML private Label lblUsernameWarning;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    private Stage dialogStage;
    private Reader reader; // đối tượng Reader đang sửa hoặc tạo mới
    private Account account; // đối tượng Account liên quan
    private boolean saveClicked = false;

    private String originalUsername = "";
    private String originalPhone = "";
    private String originalEmail = "";

    private AccountRepository accountRepository = new AccountRepository();
    private ReaderRepository readerRepository = new ReaderRepository();

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("reader"));
        roleComboBox.setValue("reader");

        // Ẩn cảnh báo ban đầu
        lblUsernameWarning.setVisible(false);
        lblPhoneWarning.setVisible(false);
        lblEmailWarning.setVisible(false);
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
            originalPhone = reader.getPhone() != null ? reader.getPhone() : "";
            originalEmail = reader.getEmail() != null ? reader.getEmail() : "";
        } else {
            originalPhone = "";
            originalEmail = "";
        }
        setupValidationListeners();
        validateAll();
    }

    public void setAccount(Account account) {
        this.account = account;
        if (account != null) {
            usernameField.setText(account.getUsername());
            passwordField.setText(account.getPassword());
            roleComboBox.setValue(account.getRole());
            originalUsername = account.getUsername() != null ? account.getUsername() : "";
        } else {
            roleComboBox.setValue("reader"); // mặc định
            originalUsername = "";
        }
        usernameField.setDisable(false);
        passwordField.setDisable(false);
        setupValidationListeners();
        validateAll();
    }

    private void setupValidationListeners() {
        // Xóa listener cũ nếu có, để tránh thêm nhiều listener khi gọi setReader/setAccount nhiều lần
        // JavaFX TextField không có API remove listener dễ dàng, nên cách đơn giản là chỉ add once
        // Nếu bạn dùng controlsfx hoặc tạo custom listener thì có thể remove, ở đây giả sử gọi 1 lần set nên add luôn

        usernameField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty() || newVal.trim().equals(originalUsername)) {
                lblUsernameWarning.setVisible(false);
            } else {
                boolean exists = accountRepository.isUsernameExists(newVal.trim());
                lblUsernameWarning.setVisible(exists);
            }
        });

        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty() || newVal.trim().equals(originalPhone)) {
                lblPhoneWarning.setVisible(false);
            } else {
                boolean exists = readerRepository.isPhoneExists(newVal.trim());
                lblPhoneWarning.setVisible(exists);
            }
        });

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty() || newVal.trim().equals(originalEmail)) {
                lblEmailWarning.setVisible(false);
            } else {
                boolean exists = readerRepository.isEmailExists(newVal.trim());
                lblEmailWarning.setVisible(exists);
            }
        });
    }

    private void validateAll() {
        // Kiểm tra username
        String username = usernameField.getText() != null ? usernameField.getText().trim() : "";
        boolean usernameExists = !username.isEmpty() && !username.equals(originalUsername) && accountRepository.isUsernameExists(username);
        lblUsernameWarning.setVisible(usernameExists);

        // Kiểm tra phone
        String phone = phoneField.getText() != null ? phoneField.getText().trim() : "";
        boolean phoneExists = !phone.isEmpty() && !phone.equals(originalPhone) && readerRepository.isPhoneExists(phone);
        lblPhoneWarning.setVisible(phoneExists);

        // Kiểm tra email
        String email = emailField.getText() != null ? emailField.getText().trim() : "";
        boolean emailExists = !email.isEmpty() && !email.equals(originalEmail) && readerRepository.isEmailExists(email);
        lblEmailWarning.setVisible(emailExists);
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
        if (!isInputValid()) {
            return;
        }

        String username = usernameField.getText().trim();
        if (account == null) {
            if (accountRepository.isUsernameExists(username)) {
                lblUsernameWarning.setVisible(true);
                showAlert("Username đã tồn tại", "Vui lòng chọn username khác.");
                return;
            }
        } else {
            if (!username.equals(account.getUsername()) && accountRepository.isUsernameExists(username)) {
                lblUsernameWarning.setVisible(true);
                showAlert("Username đã tồn tại", "Vui lòng chọn username khác.");
                return;
            }
        }

        String phone = phoneField.getText().trim();
        if (reader == null || !phone.equals(reader.getPhone())) {
            if (readerRepository.isPhoneExists(phone)) {
                lblPhoneWarning.setVisible(true);
                showAlert("Số điện thoại đã tồn tại", "Vui lòng nhập số điện thoại khác.");
                return;
            }
        }

        String email = emailField.getText().trim();
        if (!email.isEmpty() && (reader == null || !email.equals(reader.getEmail()))) {
            if (readerRepository.isEmailExists(email)) {
                lblEmailWarning.setVisible(true);
                showAlert("Email đã tồn tại", "Vui lòng nhập email khác.");
                return;
            }
        }

        reader.setFullName(fullNameField.getText());
        reader.setBirthDate(birthDatePicker.getValue());
        reader.setAddress(addressField.getText());
        reader.setPhone(phone);
        reader.setEmail(email);

        if (account == null) account = new Account();
        account.setUsername(username);
        account.setPassword(passwordField.getText());
        account.setRole("reader");
        account.setStatus("active");

        saveClicked = true;
        dialogStage.close();
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Lỗi");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
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
