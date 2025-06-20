package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.BookTitle;

public class BookTitleFormController {

    @FXML private TextField titleNameField;
    @FXML private TextField authorField;
    @FXML private TextField genreField;
    @FXML private TextField publisherField;
    @FXML private TextField publishYearField;

    private Stage dialogStage;
    private BookTitle bookTitle;
    private boolean saveClicked = false;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setBookTitle(BookTitle bookTitle) {
        this.bookTitle = bookTitle;
        if (bookTitle != null) {
            titleNameField.setText(bookTitle.getTitleName());
            authorField.setText(bookTitle.getAuthor());
            genreField.setText(bookTitle.getGenre());
            publisherField.setText(bookTitle.getPublisher());
            publishYearField.setText(String.valueOf(bookTitle.getPublishYear()));
        }
    }

    public BookTitle getBookTitle() {
        return this.bookTitle;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (bookTitle == null) {
                bookTitle = new BookTitle();
            }
            bookTitle.setTitleName(titleNameField.getText());
            bookTitle.setAuthor(authorField.getText());
            bookTitle.setGenre(genreField.getText());
            bookTitle.setPublisher(publisherField.getText());
            bookTitle.setPublishYear(Integer.parseInt(publishYearField.getText()));

            saveClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (titleNameField.getText() == null || titleNameField.getText().trim().isEmpty()) {
            errorMessage += "Tên sách không được để trống!\n";
        }
        if (authorField.getText() == null || authorField.getText().trim().isEmpty()) {
            errorMessage += "Tác giả không được để trống!\n";
        }
        if (genreField.getText() == null || genreField.getText().trim().isEmpty()) {
            errorMessage += "Thể loại không được để trống!\n";
        }
        if (publisherField.getText() == null || publisherField.getText().trim().isEmpty()) {
            errorMessage += "Nhà xuất bản không được để trống!\n";
        }
        if (publishYearField.getText() == null || publishYearField.getText().trim().isEmpty()) {
            errorMessage += "Năm xuất bản không được để trống!\n";
        } else {
            try {
                int year = Integer.parseInt(publishYearField.getText());
                if (year < 0) {
                    errorMessage += "Năm xuất bản phải là số dương!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Năm xuất bản phải là số!\n";
            }
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Lỗi nhập liệu");
            alert.setHeaderText("Thông tin đầu sách không hợp lệ");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
