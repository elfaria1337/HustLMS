package controller.reader;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.BookCopy;
import model.BookTitle;
import model.Loan;
import model.LoanDetail;
import repo.BookCopyRepository;
import repo.BookTitleRepository;
import repo.LoanDetailRepository;
import repo.LoanRepository;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoanHistoryController {

    @FXML private TableView<Loan> loanTable;
    @FXML private TableColumn<Loan, Integer> colLoanId;
    @FXML private TableColumn<Loan, String> colLoanDate;
    @FXML private TableColumn<Loan, String> colDueDate;

    @FXML private TableView<LoanDetail> loanDetailTable;
    @FXML private TableColumn<LoanDetail, Integer> colCopyId;
    @FXML private TableColumn<LoanDetail, String> colTitleName;
    @FXML private TableColumn<LoanDetail, String> colReturnDate;
    @FXML private TableColumn<LoanDetail, String> colState;

    private LoanRepository loanRepo = new LoanRepository();
    private LoanDetailRepository loanDetailRepo = new LoanDetailRepository();
    private BookCopyRepository bookCopyRepo = new BookCopyRepository();
    private BookTitleRepository bookTitleRepo = new BookTitleRepository();

    private ObservableList<Loan> loanList = FXCollections.observableArrayList();
    private ObservableList<LoanDetail> loanDetailList = FXCollections.observableArrayList();

    private int currentReaderId = 2; // TODO: Lấy ID reader từ session đăng nhập

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // Setup Loan Table columns
        colLoanId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getLoanId()).asObject());
        colLoanDate.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getLoanDate().format(formatter)));
        colDueDate.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDueDate().format(formatter)));

        // Setup LoanDetail Table columns
        colCopyId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCopyId()).asObject());
        colReturnDate.setCellValueFactory(data -> {
            if (data.getValue().getReturnDate() != null) {
                return new SimpleStringProperty(data.getValue().getReturnDate().format(formatter));
            } else {
                return new SimpleStringProperty("Chưa trả");
            }
        });
        colState.setCellValueFactory(data -> {
            int copyId = data.getValue().getCopyId();
            BookCopy copy = bookCopyRepo.findById(copyId);
            if (copy != null) {
                return new SimpleStringProperty(copy.getState());
            } else {
                return new SimpleStringProperty("Không rõ");
            }
        });

        // Để hiển thị tên sách theo copyId
        colTitleName.setCellValueFactory(data -> {
            int copyId = data.getValue().getCopyId();
            BookCopy copy = bookCopyRepo.findById(copyId);
            if (copy != null) {
                BookTitle title = bookTitleRepo.findById(copy.getTitleId());
                if (title != null) {
                    return new SimpleStringProperty(title.getTitleName());
                }
            }
            return new SimpleStringProperty("Không rõ");
        });

        loanTable.setItems(loanList);
        loanDetailTable.setItems(loanDetailList);

        loadLoans();
        // Khi chọn phiếu mượn, load chi tiết
        loanTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadLoanDetails(newVal.getLoanId());
            } else {
                loanDetailList.clear();
            }
        });
    }

    private void loadLoans() {
        List<Loan> loans = loanRepo.findByReaderId(currentReaderId);
        loanList.setAll(loans);
    }

    private void loadLoanDetails(int loanId) {
        List<LoanDetail> details = loanDetailRepo.findByLoanId(loanId);
        loanDetailList.setAll(details);
    }

    @FXML
    private void handleCreateNewLoan() {
        // TODO: Chuyển sang màn hình tạo phiếu mượn mới, hoặc hiện dialog chọn sách mượn
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tạo phiếu mượn");
        alert.setHeaderText(null);
        alert.setContentText("Chức năng tạo phiếu mượn sẽ được triển khai sau.");
        alert.showAndWait();
    }

    @FXML
    private void handleViewReservationHistory() {
        try {
            // Tải FXML cửa sổ lịch sử đặt trước
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/view/reader/reservation_history.fxml"));
            Parent root = loader.load();

            // Truyền dữ liệu Reader ID cho controller nếu cần
            ReservationHistoryController controller = loader.getController();
            controller.setReaderId(currentReaderId);

            // Tạo Stage mới
            Stage stage = new Stage();
            stage.setTitle("Lịch sử đặt trước");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // modal - chặn cửa sổ cha
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi khi mở lịch sử đặt trước", e.getMessage());
        }
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
