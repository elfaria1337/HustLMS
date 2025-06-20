package repo;

import model.BookImportDetail;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookImportDetailRepository {

    public List<BookImportDetail> findAll() {
        List<BookImportDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM book_import_detail ORDER BY import_detail_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BookImportDetail detail = mapResultSetToBookImportDetail(rs);
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public BookImportDetail findById(int id) {
        String sql = "SELECT * FROM book_import_detail WHERE import_detail_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBookImportDetail(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(BookImportDetail detail) {
        String sql = "INSERT INTO book_import_detail(quantity, price_per_unit, title_id, import_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detail.getQuantity());
            stmt.setBigDecimal(2, detail.getPricePerUnit());
            stmt.setInt(3, detail.getTitleId());
            stmt.setInt(4, detail.getImportId());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        detail.setImportDetailId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(BookImportDetail detail) {
        String sql = "UPDATE book_import_detail SET quantity = ?, price_per_unit = ?, title_id = ?, import_id = ? WHERE import_detail_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getQuantity());
            stmt.setBigDecimal(2, detail.getPricePerUnit());
            stmt.setInt(3, detail.getTitleId());
            stmt.setInt(4, detail.getImportId());
            stmt.setInt(5, detail.getImportDetailId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM book_import_detail WHERE import_detail_id = ?";
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

    private BookImportDetail mapResultSetToBookImportDetail(ResultSet rs) throws SQLException {
        BookImportDetail detail = new BookImportDetail();
        detail.setImportDetailId(rs.getInt("import_detail_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setPricePerUnit(rs.getBigDecimal("price_per_unit"));
        detail.setTitleId(rs.getInt("title_id"));
        detail.setImportId(rs.getInt("import_id"));
        return detail;
    }
}
