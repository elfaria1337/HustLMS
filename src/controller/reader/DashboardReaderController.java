package controller.reader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import model.Reader;
import repo.LoanRepository;
import repo.ReaderRepository;

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

    private int currentReaderId;

    @FXML
    private Label lblWelcome;

    @FXML
    public void initialize() {
        btnBookSearch.setOnAction(e -> loadView("/res/view/reader/book_search.fxml"));
        btnLoanHistory.setOnAction(e -> loadView("/res/view/reader/loan_history.fxml"));
        btnReservationHistory.setOnAction(e -> loadView("/res/view/reader/reservation_history.fxml"));
        btnFinePayment.setOnAction(e -> loadView("/res/view/reader/fine_payment.fxml"));
    }

    public void setCurrentReaderId(Integer id) {
        this.currentReaderId = id;
        loadReaderProfile();
        loadLoanSummary();
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

    private void loadReaderProfile() {
        // Ví dụ gọi repo ReaderRepository lấy tên độc giả theo id
        ReaderRepository repo = new ReaderRepository();
        Reader reader = repo.findById(currentReaderId);
        if (reader != null) {
            lblWelcome.setText("Chào mừng, " + reader.getFullName() + "!");
        } else {
            lblWelcome.setText("Chào mừng độc giả!");
        }
    }

    private void loadLoanSummary() {
        // Ví dụ lấy số lượng phiếu mượn chưa trả
        LoanRepository loanRepo = new LoanRepository();
        int loanCount = loanRepo.countActiveLoansByReader(currentReaderId);
        System.out.println("Số phiếu mượn đang hoạt động: " + loanCount);
        // Bạn có thể show lên giao diện hoặc xử lý thêm
    }
}
