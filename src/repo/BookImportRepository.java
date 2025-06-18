package repo;

import model.BookImport;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookImportRepository {

    public List<BookImport> findAll() {
        List<BookImport> list = new ArrayList<>();
        String sql = "SELECT * FROM book_import ORDER BY import_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BookImport b = mapResultSetToBookImport(rs);
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public BookImport findById(int id) {
        String sql = "SELECT * FROM book_import WHERE import_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBookImport(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(BookImport b) {
        String sql = "INSERT INTO book_import(order_date, receive_date, supplier_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(b.getOrderDate()));
            stmt.setDate(2, Date.valueOf(b.getReceiveDate()));
            stmt.setInt(3, b.getSupplierId());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        b.setImportId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(BookImport b) {
        String sql = "UPDATE book_import SET order_date = ?, receive_date = ?, supplier_id = ? WHERE import_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(b.getOrderDate()));
            stmt.setDate(2, Date.valueOf(b.getReceiveDate()));
            stmt.setInt(3, b.getSupplierId());
            stmt.setInt(4, b.getImportId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM book_import WHERE import_id = ?";
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

    private BookImport mapResultSetToBookImport(ResultSet rs) throws SQLException {
        BookImport b = new BookImport();
        b.setImportId(rs.getInt("import_id"));
        b.setOrderDate(rs.getDate("order_date").toLocalDate());
        b.setReceiveDate(rs.getDate("receive_date").toLocalDate());
        b.setSupplierId(rs.getInt("supplier_id"));
        return b;
    }
}
