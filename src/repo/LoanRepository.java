package repo;

import model.Loan;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository {

    public List<Loan> findAll() {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loan ORDER BY loan_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Loan loan = mapResultSetToLoan(rs);
                list.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Loan findById(int id) {
        String sql = "SELECT * FROM loan WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoan(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Loan> findByReaderId(int readerId) {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loan WHERE reader_id = ? ORDER BY loan_date DESC";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToLoan(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Loan loan) {
        String sql = "INSERT INTO loan(loan_date, due_date, reader_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(2, Date.valueOf(loan.getDueDate()));
            stmt.setInt(3, loan.getReaderId());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try(ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        loan.setLoanId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Loan loan) {
        String sql = "UPDATE loan SET loan_date = ?, due_date = ?, reader_id = ? WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(2, Date.valueOf(loan.getDueDate()));
            stmt.setInt(3, loan.getReaderId());
            stmt.setInt(4, loan.getLoanId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM loan WHERE loan_id = ?";
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

    public boolean save(Loan loan) {
        if (loan.getLoanId() == 0) {
            // Thêm mới
            String sql = "INSERT INTO loan (loan_date, due_date, reader_id) VALUES (?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setDate(1, Date.valueOf(loan.getLoanDate()));
                stmt.setDate(2, Date.valueOf(loan.getDueDate()));
                stmt.setInt(3, loan.getReaderId());
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            loan.setLoanId(keys.getInt(1));
                        }
                    }
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            // Cập nhật
            String sql = "UPDATE loan SET loan_date = ?, due_date = ?, reader_id = ? WHERE loan_id = ?";
            try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(loan.getLoanDate()));
                stmt.setDate(2, Date.valueOf(loan.getDueDate()));
                stmt.setInt(3, loan.getReaderId());
                stmt.setInt(4, loan.getLoanId());
                int affected = stmt.executeUpdate();
                return affected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setLoanId(rs.getInt("loan_id"));
        loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
        loan.setDueDate(rs.getDate("due_date").toLocalDate());
        loan.setReaderId(rs.getInt("reader_id"));
        return loan;
    }

    public int countLoansToday() {
        String sql = "SELECT COUNT(*) FROM loan WHERE loan_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
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

    public int countActiveLoansByReader(int readerId) {
        String sql = """
            SELECT COUNT(DISTINCT l.loan_id)
            FROM loan l
            JOIN loan_detail ld ON l.loan_id = ld.loan_id
            WHERE l.reader_id = ? AND ld.return_date IS NULL
        """;
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
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
}
