package repo;

import model.Reader;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderRepository {

    public List<Reader> findAll() {
        List<Reader> readers = new ArrayList<>();
        String sql = "SELECT * FROM reader ORDER BY reader_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                readers.add(mapResultSetToReader(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readers;
    }

    public Reader findById(int id) {
        String sql = "SELECT * FROM reader WHERE reader_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReader(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Reader> searchByNameOrPhone(String keyword) {
        List<Reader> result = new ArrayList<>();
        String sql = "SELECT * FROM reader WHERE full_name ILIKE ? OR phone ILIKE ? ORDER BY reader_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            stmt.setString(1, kw);
            stmt.setString(2, kw);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToReader(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isPhoneExists(String phone) {
        String sql = "SELECT COUNT(*) FROM reader WHERE phone = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        String sql = "SELECT COUNT(*) FROM reader WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insert(Reader reader) {
        String sql = "INSERT INTO reader(full_name, birth_date, address, phone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, reader.getFullName());
            stmt.setDate(2, Date.valueOf(reader.getBirthDate()));
            stmt.setString(3, reader.getAddress());
            stmt.setString(4, reader.getPhone());
            stmt.setString(5, reader.getEmail());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reader.setReaderId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Reader reader) {
        String sql = "UPDATE reader SET full_name = ?, birth_date = ?, address = ?, phone = ?, email = ? WHERE reader_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reader.getFullName());
            stmt.setDate(2, Date.valueOf(reader.getBirthDate()));
            stmt.setString(3, reader.getAddress());
            stmt.setString(4, reader.getPhone());
            stmt.setString(5, reader.getEmail());
            stmt.setInt(6, reader.getReaderId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM reader WHERE reader_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Reader mapResultSetToReader(ResultSet rs) throws SQLException {
        Reader reader = new Reader();
        reader.setReaderId(rs.getInt("reader_id"));
        reader.setFullName(rs.getString("full_name"));
        reader.setBirthDate(rs.getDate("birth_date").toLocalDate());
        reader.setAddress(rs.getString("address"));
        reader.setPhone(rs.getString("phone"));
        reader.setEmail(rs.getString("email"));
        return reader;
    }
}
