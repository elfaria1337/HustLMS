package controller.reader;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.BookCopy;
import model.BookTitle;
import repo.BookCopyRepository;
import repo.BookTitleRepository;

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

    private ObservableList<BookTitle> bookTitleList = FXCollections.observableArrayList();
    private ObservableList<BookCopy> bookCopyList = FXCollections.observableArrayList();

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
}
