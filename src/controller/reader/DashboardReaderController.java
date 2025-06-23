package controller.reader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DashboardReaderController {

    @FXML
    private Button btnBookSearch;

    @FXML
    private Button btnLoanHistory;

    @FXML
    private Button btnReservationHistory;

    @FXML
    private Button btnFinePayment;

    @FXML
    private StackPane contentPane;

    @FXML
    private Label defaultLabel;

    @FXML
    public void initialize() {
        btnBookSearch.setOnAction(e -> loadView("/res/view/reader/book_search.fxml"));
        btnLoanHistory.setOnAction(e -> loadView("/res/view/reader/loan_history.fxml"));
        btnReservationHistory.setOnAction(e -> loadView("/res/view/reader/reservation_history.fxml"));
        btnFinePayment.setOnAction(e -> loadView("/res/view/reader/fine_payment.fxml"));
    }

    private void loadView(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(node);
            defaultLabel.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
