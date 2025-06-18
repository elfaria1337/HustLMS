package repo;

import model.Reservation;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {

    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservation ORDER BY reservation_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Reservation res = mapResultSetToReservation(rs);
                list.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Reservation findById(int id) {
        String sql = "SELECT * FROM reservation WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Reservation res) {
        String sql = "INSERT INTO reservation(reservation_date, status, reader_id, title_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(res.getReservationDate()));
            stmt.setString(2, res.getStatus());
            stmt.setInt(3, res.getReaderId());
            stmt.setInt(4, res.getTitleId());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try(ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        res.setReservationId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Reservation res) {
        String sql = "UPDATE reservation SET reservation_date = ?, status = ?, reader_id = ?, title_id = ? WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(res.getReservationDate()));
            stmt.setString(2, res.getStatus());
            stmt.setInt(3, res.getReaderId());
            stmt.setInt(4, res.getTitleId());
            stmt.setInt(5, res.getReservationId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM reservation WHERE reservation_id = ?";
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

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation res = new Reservation();
        res.setReservationId(rs.getInt("reservation_id"));
        res.setReservationDate(rs.getDate("reservation_date").toLocalDate());
        res.setStatus(rs.getString("status"));
        res.setReaderId(rs.getInt("reader_id"));
        res.setTitleId(rs.getInt("title_id"));
        return res;
    }
}
