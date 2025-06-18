package repo;

import model.BookTitle;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookTitleRepository {

    public List<BookTitle> findAll() {
        List<BookTitle> list = new ArrayList<>();
        String sql = "SELECT * FROM book_title ORDER BY title_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BookTitle book = mapResultSetToBookTitle(rs);
                list.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public BookTitle findById(int id) {
        String sql = "SELECT * FROM book_title WHERE title_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBookTitle(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(BookTitle book) {
        String sql = "INSERT INTO book_title(title_name, author, genre, publisher, publish_year) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitleName());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setString(4, book.getPublisher());
            if (book.getPublishYear() != null) {
                stmt.setInt(5, book.getPublishYear());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        book.setTitleId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(BookTitle book) {
        String sql = "UPDATE book_title SET title_name = ?, author = ?, genre = ?, publisher = ?, publish_year = ? WHERE title_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitleName());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setString(4, book.getPublisher());
            if (book.getPublishYear() != null) {
                stmt.setInt(5, book.getPublishYear());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, book.getTitleId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM book_title WHERE title_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private BookTitle mapResultSetToBookTitle(ResultSet rs) throws SQLException {
        BookTitle book = new BookTitle();
        book.setTitleId(rs.getInt("title_id"));
        book.setTitleName(rs.getString("title_name"));
        book.setAuthor(rs.getString("author"));
        book.setGenre(rs.getString("genre"));
        book.setPublisher(rs.getString("publisher"));
        int year = rs.getInt("publish_year");
        if (!rs.wasNull()) {
            book.setPublishYear(year);
        }
        return book;
    }
}
