package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Loan;
import model.LoanDetail;
import model.Reservation;
import repo.LoanDetailRepository;
import repo.LoanRepository;
import repo.ReservationRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class LoanManagementController {

    // Phiếu mượn
    @FXML private TableView<Loan> loanTable;
    @FXML private TableColumn<Loan, Integer> colLoanId;
    @FXML private TableColumn<Loan, String> colLoanDate;
    @FXML private TableColumn<Loan, String> colDueDate;
    @FXML private TableColumn<Loan, Integer> colReaderId;

    // Chi tiết mượn
    @FXML private TableView<LoanDetail> loanDetailTable;
    @FXML private TableColumn<LoanDetail, Integer> colLoanDetailId;
    @FXML private TableColumn<LoanDetail, String> colReturnDate;
    @FXML private TableColumn<LoanDetail, Integer> colLoanIdDetail;
    @FXML private TableColumn<LoanDetail, Integer> colCopyId;

    // Đặt trước
    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> colReservationId;
    @FXML private TableColumn<Reservation, String> colReservationDate;
    @FXML private TableColumn<Reservation, String> colStatus;
    @FXML private TableColumn<Reservation, Integer> colReaderIdRes;
    @FXML private TableColumn<Reservation, Integer> colTitleId;

    private LoanRepository loanRepo = new LoanRepository();
    private LoanDetailRepository loanDetailRepo = new LoanDetailRepository();
    private ReservationRepository reservationRepo = new ReservationRepository();

    private ObservableList<Loan> loanList = FXCollections.observableArrayList();
    private ObservableList<LoanDetail> loanDetailList = FXCollections.observableArrayList();
    private ObservableList<Reservation> reservationList = FXCollections.observableArrayList();

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // Phiếu mượn
        colLoanId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLoanId()));
        colLoanDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLoanDate().format(dateFormatter)));
        colDueDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDueDate().format(dateFormatter)));
        colReaderId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReaderId()));

        // Chi tiết mượn
        colLoanDetailId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLoanDetailId()));
        colReturnDate.setCellValueFactory(cellData -> {
            if (cellData.getValue().getReturnDate() != null) {
                return new ReadOnlyObjectWrapper<>(cellData.getValue().getReturnDate().format(dateFormatter));
            } else {
                return new ReadOnlyObjectWrapper<>("Chưa trả");
            }
        });
        colLoanIdDetail.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLoanId()));
        colCopyId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCopyId()));

        // Đặt trước
        colReservationId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReservationId()));
        colReservationDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReservationDate().format(dateFormatter)));
        colStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getStatus()));
        colReaderIdRes.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReaderId()));
        colTitleId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTitleId()));

        loadData();
    }

    private void loadData() {
        List<Loan> loans = loanRepo.findAll();
        loanList.setAll(loans);
        loanTable.setItems(loanList);

        List<LoanDetail> details = loanDetailRepo.findAll();
        loanDetailList.setAll(details);
        loanDetailTable.setItems(loanDetailList);

        List<Reservation> reservations = reservationRepo.findAll();
        reservationList.setAll(reservations);
        reservationTable.setItems(reservationList);
    }

    // ===== Loan handlers =====
    public void handleAddLoan() {
        showInfo("Chức năng thêm phiếu mượn chưa triển khai.");
    }

    public void handleEditLoan() {
        Loan selected = loanTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn phiếu mượn để sửa.");
            return;
        }
        showInfo("Chức năng sửa phiếu mượn chưa triển khai.");
    }

    public void handleDeleteLoan() {
        Loan selected = loanTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn phiếu mượn để xóa.");
            return;
        }
        if (confirmDialog("Bạn có chắc muốn xóa phiếu mượn này?")) {
            if (loanRepo.delete(selected.getLoanId())) {
                showInfo("Xóa phiếu mượn thành công.");
                loadData();
            } else {
                showWarning("Xóa phiếu mượn thất bại.");
            }
        }
    }

    // ===== LoanDetail handlers =====
    public void handleAddLoanDetail() {
        showInfo("Chức năng thêm chi tiết mượn chưa triển khai.");
    }

    public void handleEditLoanDetail() {
        LoanDetail selected = loanDetailTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn chi tiết mượn để sửa.");
            return;
        }
        showInfo("Chức năng sửa chi tiết mượn chưa triển khai.");
    }

    public void handleDeleteLoanDetail() {
        LoanDetail selected = loanDetailTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn chi tiết mượn để xóa.");
            return;
        }
        if (confirmDialog("Bạn có chắc muốn xóa chi tiết mượn này?")) {
            if (loanDetailRepo.delete(selected.getLoanDetailId())) {
                showInfo("Xóa chi tiết mượn thành công.");
                loadData();
            } else {
                showWarning("Xóa chi tiết mượn thất bại.");
            }
        }
    }

    // ===== Reservation handlers =====
    public void handleAddReservation() {
        showInfo("Chức năng thêm đặt trước chưa triển khai.");
    }

    public void handleEditReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn đặt trước để sửa.");
            return;
        }
        showInfo("Chức năng sửa đặt trước chưa triển khai.");
    }

    public void handleDeleteReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn đặt trước để xóa.");
            return;
        }
        if (confirmDialog("Bạn có chắc muốn xóa đặt trước này?")) {
            if (reservationRepo.delete(selected.getReservationId())) {
                showInfo("Xóa đặt trước thành công.");
                loadData();
            } else {
                showWarning("Xóa đặt trước thất bại.");
            }
        }
    }

    // ===== Helper methods =====
    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private boolean confirmDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setContentText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
