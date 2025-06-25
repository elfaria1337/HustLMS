package repo;

import model.BookCopy;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookCopyRepository {
    public List<BookCopy> findAll() {
        List<BookCopy> list = new ArrayList<>();
        String sql = "SELECT bc.*, i.location_name " +
                    "FROM book_copy bc " +
                    "LEFT JOIN inventory i ON bc.inventory_id = i.inventory_id " +
                    "ORDER BY bc.copy_id";
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BookCopy copy = mapResultSetToBookCopy(rs);
                list.add(copy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public BookCopy findById(int id) {
        String sql = "SELECT bc.*, i.location_name" + " FROM book_copy bc "
            + "JOIN inventory i ON bc.inventory_id = i.inventory_id "
            + "WHERE bc.copy_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBookCopy(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BookCopy> findByTitleId(int titleId) {
        List<BookCopy> list = new ArrayList<>();
        String sql = "SELECT bc.*, i.location_name " +
                    "FROM book_copy bc " +
                    "JOIN inventory i ON bc.inventory_id = i.inventory_id " +
                    "WHERE bc.title_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, titleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BookCopy copy = mapResultSetToBookCopy(rs);
                    list.add(copy);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Integer findAvailableCopyIdByTitleName(String titleName) {
        String sql = "SELECT bc.copy_id FROM book_copy bc " +
                    "JOIN book_title bt ON bc.title_id = bt.title_id " +
                    "WHERE bt.title_name ILIKE ? AND bc.state = 'Sẵn sàng' " +
                    "ORDER BY bc.copy_id LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + titleName + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("copy_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countByTitleId(int titleId) {
        String sql = "SELECT COUNT(*) FROM book_copy WHERE title_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, titleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean insert(BookCopy copy) {
        String sql = "INSERT INTO book_copy(state, inventory_id, title_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, copy.getState());
            stmt.setInt(2, copy.getInventoryId());
            stmt.setInt(3, copy.getTitleId());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        copy.setCopyId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(BookCopy copy) {
        String sql = "UPDATE book_copy SET state = ?, inventory_id = ?, title_id = ? WHERE copy_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, copy.getState());
            stmt.setInt(2, copy.getInventoryId());
            stmt.setInt(3, copy.getTitleId());
            stmt.setInt(4, copy.getCopyId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM book_copy WHERE copy_id = ?";
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

    private BookCopy mapResultSetToBookCopy(ResultSet rs) throws SQLException {
        BookCopy copy = new BookCopy();
        copy.setCopyId(rs.getInt("copy_id"));
        copy.setState(rs.getString("state"));
        copy.setInventoryId(rs.getInt("inventory_id"));
        copy.setTitleId(rs.getInt("title_id"));
        copy.setLocationName(rs.getString("location_name")); // lấy tên kho từ bảng inventory
        return copy;
    }
}
