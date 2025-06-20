package repo;

import model.Fine;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FineRepository {

    public List<Fine> findAll() {
        List<Fine> list = new ArrayList<>();
        String sql = "SELECT * FROM fine ORDER BY fine_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Fine fine = mapResultSetToFine(rs);
                list.add(fine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Fine findById(int id) {
        String sql = "SELECT * FROM fine WHERE fine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFine(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Fine> findByReaderId(int readerId) {
        List<Fine> list = new ArrayList<>();
        String sql = "SELECT * FROM fine WHERE reader_id = ? ORDER BY issue_date DESC";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToFine(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Fine fine) {
        String sql = "INSERT INTO fine(reason, amount, issue_date, reader_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, fine.getReason());
            stmt.setBigDecimal(2, fine.getAmount());
            stmt.setDate(3, Date.valueOf(fine.getIssueDate()));
            stmt.setInt(4, fine.getReaderId());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try(ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        fine.setFineId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Fine fine) {
        String sql = "UPDATE fine SET reason = ?, amount = ?, issue_date = ?, reader_id = ? WHERE fine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fine.getReason());
            stmt.setBigDecimal(2, fine.getAmount());
            stmt.setDate(3, Date.valueOf(fine.getIssueDate()));
            stmt.setInt(4, fine.getReaderId());
            stmt.setInt(5, fine.getFineId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM fine WHERE fine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Fine mapResultSetToFine(ResultSet rs) throws SQLException {
        Fine fine = new Fine();
        fine.setFineId(rs.getInt("fine_id"));
        fine.setReason(rs.getString("reason"));
        fine.setAmount(rs.getBigDecimal("amount"));
        fine.setIssueDate(rs.getDate("issue_date").toLocalDate());
        fine.setReaderId(rs.getInt("reader_id"));
        return fine;
    }
}
