package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DashboardController {

    @FXML private Button btnReaderManagement;
    @FXML private Button btnBookManagement;
    @FXML private Button btnBookImportManagement;
    @FXML private Button btnLoanManagement;
    @FXML private Button btnFineInvoiceManagement;

    @FXML private StackPane contentPane;
    @FXML private Label defaultDashboardLabel;

    @FXML
    public void initialize() {
        btnReaderManagement.setOnAction(e -> loadPage("/res/view/reader_management.fxml"));
        btnBookManagement.setOnAction(e -> loadPage("/res/view/book_management.fxml"));
        btnBookImportManagement.setOnAction(e -> loadPage("/res/view/book_import_management.fxml"));
        btnLoanManagement.setOnAction(e -> loadPage("/res/view/loan_management.fxml"));
        // btnFineInvoiceManagement.setOnAction(e -> loadPage("/res/view/fine_invoice_management.fxml"));
    }

    private void loadPage(String fxmlPath) {
        try {
            Node page = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(page);
            defaultDashboardLabel.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
            defaultDashboardLabel.setText("Không thể tải trang: " + fxmlPath);
            defaultDashboardLabel.setVisible(true);
        }
    }
}
