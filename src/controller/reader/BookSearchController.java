package controller.reader;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.BookCopy;
import model.BookTitle;
import model.Reservation;
import repo.BookCopyRepository;
import repo.BookTitleRepository;
import repo.LoanRepository;
import repo.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

public class BookSearchController {

    @FXML private TextField searchField;

    @FXML private TableView<BookTitle> bookTitleTable;
    @FXML private TableColumn<BookTitle, String> colTitleName;
    @FXML private TableColumn<BookTitle, String> colAuthor;
    @FXML private TableColumn<BookTitle, String> colGenre;
    @FXML private TableColumn<BookTitle, String> colPublisher;
    @FXML private TableColumn<BookTitle, Integer> colPublishYear;

    @FXML private TableView<BookCopy> bookCopyTable;
    @FXML private TableColumn<BookCopy, Integer> colCopyId;
    @FXML private TableColumn<BookCopy, String> colLocationName;
    @FXML private TableColumn<BookCopy, String> colState;

    private BookTitleRepository bookTitleRepo = new BookTitleRepository();
    private BookCopyRepository bookCopyRepo = new BookCopyRepository();
    private ReservationRepository reservationRepo = new ReservationRepository();
    private LoanRepository loanRepository = new LoanRepository();

    private ObservableList<BookTitle> bookTitleList = FXCollections.observableArrayList();
    private ObservableList<BookCopy> bookCopyList = FXCollections.observableArrayList();

    private int currentReaderId = 2; // TODO: lấy ID reader từ phiên đăng nhập thực tế

    @FXML
    public void initialize() {
        // Setup columns for BookTitle
        colTitleName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitleName()));
        colAuthor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
        colGenre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGenre()));
        colPublisher.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPublisher()));
        colPublishYear.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getPublishYear()).asObject());

        // Setup columns for BookCopy
        colCopyId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCopyId()).asObject());
        colLocationName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocationName()));
        colState.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getState()));

        bookTitleTable.setItems(bookTitleList);
        bookCopyTable.setItems(bookCopyList);

        loadAllBookTitles();

        // Listener text thay đổi để tự động tìm kiếm
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            handleSearch();
        });

        bookTitleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadBookCopies(newSelection.getTitleId());
            } else {
                bookCopyList.clear();
            }
        });
    }

    private void loadAllBookTitles() {
        List<BookTitle> titles = bookTitleRepo.findAll();
        bookTitleList.setAll(titles);
    }

    private void loadBookCopies(int titleId) {
        List<BookCopy> copies = bookCopyRepo.findByTitleId(titleId);
        bookCopyList.setAll(copies);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText();
        if (keyword == null || keyword.isBlank()) {
            loadAllBookTitles();
            return;
        }
        List<BookTitle> filtered = bookTitleRepo.searchByKeyword(keyword.trim());
        bookTitleList.setAll(filtered);
    }

    @FXML
    private void handleReserveBook() {
        BookTitle selected = bookTitleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn đầu sách để đặt trước.");
            return;
        }

        // Kiểm tra còn bản sao "Sẵn sàng" hay không
        List<BookCopy> copies = bookCopyRepo.findByTitleId(selected.getTitleId());
        boolean hasAvailableCopy = copies.stream()
                                        .anyMatch(copy -> copy.getState().equalsIgnoreCase("Sẵn sàng"));
        if (hasAvailableCopy) {
            showInfo("Hiện tại đầu sách còn bản sao sẵn sàng, không cần đặt trước.");
            return;
        }

        // Tạo đặt trước
        Reservation res = new Reservation();
        res.setReservationDate(LocalDate.now());
        res.setStatus("Pending"); // trạng thái "Chờ xử lý"
        res.setReaderId(currentReaderId);
        res.setTitleId(selected.getTitleId());

        boolean success = reservationRepo.insert(res);
        if (success) {
            showInfo("Đặt trước thành công! Vui lòng chờ thông báo xử lý.");
        } else {
            showError("Đặt trước thất bại, vui lòng thử lại.");
        }
    }

    @FXML
    private void handleSuggestBooks() {
        List<String> favoriteGenres = loanRepository.findFavoriteGenresByReaderIdWithinMonths(currentReaderId, 3);
        if (favoriteGenres == null || favoriteGenres.isEmpty()) {
            showInfo("Không có dữ liệu lịch sử mượn để gợi ý sách.");
            return;
        }

        List<BookTitle> suggestedBooks = bookTitleRepo.findByGenres(favoriteGenres);
        if (suggestedBooks.isEmpty()) {
            showInfo("Không tìm thấy sách phù hợp với sở thích của bạn.");
            return;
        }

        bookTitleList.setAll(suggestedBooks);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
