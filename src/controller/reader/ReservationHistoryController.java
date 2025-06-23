package controller.reader;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Reservation;
import model.BookTitle;
import repo.BookTitleRepository;
import repo.ReservationRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationHistoryController {

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> colReservationId;
    @FXML private TableColumn<Reservation, String> colReservationDate;
    @FXML private TableColumn<Reservation, String> colStatus;
    @FXML private TableColumn<Reservation, String> colTitleName;

    private ReservationRepository reservationRepo = new ReservationRepository();
    private BookTitleRepository bookTitleRepo = new BookTitleRepository();

    private ObservableList<Reservation> reservationList = FXCollections.observableArrayList();

    private int readerId;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void setReaderId(int readerId) {
        this.readerId = readerId;
        loadReservations();
    }

    @FXML
    public void initialize() {
        colReservationId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getReservationId()).asObject());
        colReservationDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReservationDate().format(formatter)));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        colTitleName.setCellValueFactory(data -> {
            int titleId = data.getValue().getTitleId();
            BookTitle title = bookTitleRepo.findById(titleId);
            if (title != null) {
                return new SimpleStringProperty(title.getTitleName());
            }
            return new SimpleStringProperty("Không rõ");
        });

        reservationTable.setItems(reservationList);
    }

    private void loadReservations() {
        if (readerId <= 0) return;
        List<Reservation> list = reservationRepo.findByReaderId(readerId);
        reservationList.setAll(list);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) reservationTable.getScene().getWindow();
        stage.close();
    }
}
