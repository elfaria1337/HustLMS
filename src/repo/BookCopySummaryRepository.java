package repo;

import util.DBConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BookCopySummaryRepository {

    // Lấy số lượng bản sao của một đầu sách
    public Integer getCopyCountByTitleId(int titleId) {
        String sql = "SELECT copy_count FROM book_copy_summary WHERE title_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, titleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("copy_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy toàn bộ dữ liệu copy_count, trả về Map<titleId, copyCount>
    public Map<Integer, Integer> getAllCopyCounts() {
        Map<Integer, Integer> map = new HashMap<>();
        String sql = "SELECT title_id, copy_count FROM book_copy_summary";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getInt("title_id"), rs.getInt("copy_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}
