package repo;

import model.LoanDetail;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDetailRepository {

    public List<LoanDetail> findAll() {
        List<LoanDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM loan_detail ORDER BY loan_detail_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LoanDetail detail = mapResultSetToLoanDetail(rs);
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public LoanDetail findById(int id) {
        String sql = "SELECT * FROM loan_detail WHERE loan_detail_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoanDetail(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LoanDetail> findByLoanId(int loanId) {
        List<LoanDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM loan_detail WHERE loan_id = ? ORDER BY loan_detail_id";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loanId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LoanDetail detail = mapResultSetToLoanDetail(rs);
                    list.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(LoanDetail detail) {
        String sql = "INSERT INTO loan_detail(return_date, loan_id, copy_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (detail.getReturnDate() != null) {
                stmt.setDate(1, Date.valueOf(detail.getReturnDate()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            stmt.setInt(2, detail.getLoanId());
            stmt.setInt(3, detail.getCopyId());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try(ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        detail.setLoanDetailId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(LoanDetail detail) {
        String sql = "UPDATE loan_detail SET return_date = ?, loan_id = ?, copy_id = ? WHERE loan_detail_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (detail.getReturnDate() != null) {
                stmt.setDate(1, Date.valueOf(detail.getReturnDate()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            stmt.setInt(2, detail.getLoanId());
            stmt.setInt(3, detail.getCopyId());
            stmt.setInt(4, detail.getLoanDetailId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM loan_detail WHERE loan_detail_id = ?";
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

    public void save(LoanDetail loanDetail) {
        if (loanDetail.getLoanDetailId() == 0) {
            insert(loanDetail);
        } else {
            update(loanDetail);
        }
    }

    private LoanDetail mapResultSetToLoanDetail(ResultSet rs) throws SQLException {
        LoanDetail detail = new LoanDetail();
        detail.setLoanDetailId(rs.getInt("loan_detail_id"));
        Date retDate = rs.getDate("return_date");
        if(retDate != null) {
            detail.setReturnDate(retDate.toLocalDate());
        }
        detail.setLoanId(rs.getInt("loan_id"));
        detail.setCopyId(rs.getInt("copy_id"));
        return detail;
    }
}
