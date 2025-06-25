package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Account;
import repo.AccountRepository;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMessage;

    private AccountRepository accountRepo = new AccountRepository();

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            return;
        }

        Account acc = accountRepo.findByUsername(username);
        if (acc == null) {
            lblMessage.setText("Tài khoản không tồn tại.");
            return;
        }

        if (!password.equals(acc.getPassword())) {
            lblMessage.setText("Mật khẩu không đúng.");
            return;
        }

        if (!"active".equalsIgnoreCase(acc.getStatus())) {
            lblMessage.setText("Tài khoản bị khóa hoặc không hoạt động.");
            return;
        }

        // Đăng nhập thành công, phân quyền mở dashboard
        openDashboard(acc);
    }

    private void openDashboard(Account acc) {
        try {
            Stage stage = (Stage) txtUsername.getScene().getWindow();

            String fxmlPath;
            if ("reader".equalsIgnoreCase(acc.getRole())) {
                fxmlPath = "/res/view/reader/dashboard_reader.fxml";
            } else {
                fxmlPath = "/res/view/dashboard.fxml";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node root = loader.load();

            Stage dashboardStage = new Stage();
            Scene scene = new Scene((javafx.scene.Parent) root);

            // Set kích thước bằng với stage cũ
            scene.getWindow(); // chưa attach window, set sau khi show
            dashboardStage.setScene(scene);
            dashboardStage.setTitle("Dashboard - " + acc.getRole());

            // Đặt kích thước giống window login
            dashboardStage.setWidth(stage.getWidth());
            dashboardStage.setHeight(stage.getHeight());
            dashboardStage.setX(stage.getX());
            dashboardStage.setY(stage.getY());

            if ("reader".equalsIgnoreCase(acc.getRole())) {
                controller.reader.DashboardReaderController ctrl = loader.getController();
                ctrl.setCurrentReaderId(acc.getReaderId());
            } else {
                controller.DashboardController ctrl = loader.getController();
                ctrl.setCurrentStaffId(acc.getStaffId());
            }

            dashboardStage.show();

            // Đóng cửa sổ login
            stage.close();

        } catch (IOException e) {
            e.printStackTrace();
            lblMessage.setText("Lỗi khi mở dashboard.");
        }
    }
}
