package repo;

import model.Staff;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffRepository {

    public List<Staff> findAll() {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY staff_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Staff s = mapResultSetToStaff(rs);
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Staff findById(int id) {
        String sql = "SELECT * FROM staff WHERE staff_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStaff(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Staff staff) {
        String sql = "INSERT INTO staff(full_name, birth_date, address, phone, email, hire_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, staff.getFullName());
            stmt.setDate(2, Date.valueOf(staff.getBirthDate()));
            stmt.setString(3, staff.getAddress());
            stmt.setString(4, staff.getPhone());
            stmt.setString(5, staff.getEmail());
            stmt.setDate(6, Date.valueOf(staff.getHireDate()));

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        staff.setStaffId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Staff staff) {
        String sql = "UPDATE staff SET full_name = ?, birth_date = ?, address = ?, phone = ?, email = ?, hire_date = ? WHERE staff_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staff.getFullName());
            stmt.setDate(2, Date.valueOf(staff.getBirthDate()));
            stmt.setString(3, staff.getAddress());
            stmt.setString(4, staff.getPhone());
            stmt.setString(5, staff.getEmail());
            stmt.setDate(6, Date.valueOf(staff.getHireDate()));
            stmt.setInt(7, staff.getStaffId());

            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM staff WHERE staff_id = ?";
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

    private Staff mapResultSetToStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setStaffId(rs.getInt("staff_id"));
        staff.setFullName(rs.getString("full_name"));
        staff.setBirthDate(rs.getDate("birth_date").toLocalDate());
        staff.setAddress(rs.getString("address"));
        staff.setPhone(rs.getString("phone"));
        staff.setEmail(rs.getString("email"));
        staff.setHireDate(rs.getDate("hire_date").toLocalDate());
        return staff;
    }
}
