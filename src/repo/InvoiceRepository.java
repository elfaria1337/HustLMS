package repo;

import model.Invoice;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceRepository {

    public List<Invoice> findAll() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice ORDER BY invoice_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Invoice invoice = mapResultSetToInvoice(rs);
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Invoice findById(int id) {
        String sql = "SELECT * FROM invoice WHERE invoice_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoice(rs);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Invoice> findByReaderId(int readerId) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE reader_id = ? ORDER BY payment_date DESC";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToInvoice(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Invoice invoice) {
        String sql = "INSERT INTO invoice(amount, payment_method, payment_date, reader_id, fine_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setBigDecimal(1, invoice.getAmount());
            stmt.setString(2, invoice.getPaymentMethod());
            stmt.setDate(3, Date.valueOf(invoice.getPaymentDate()));
            stmt.setInt(4, invoice.getReaderId());
            if (invoice.getFineId() != null) {
                stmt.setInt(5, invoice.getFineId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try(ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        invoice.setInvoiceId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Invoice invoice) {
        String sql = "UPDATE invoice SET amount = ?, payment_method = ?, payment_date = ?, reader_id = ?, fine_id = ? WHERE invoice_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, invoice.getAmount());
            stmt.setString(2, invoice.getPaymentMethod());
            stmt.setDate(3, Date.valueOf(invoice.getPaymentDate()));
            stmt.setInt(4, invoice.getReaderId());
            if (invoice.getFineId() != null) {
                stmt.setInt(5, invoice.getFineId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, invoice.getInvoiceId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM invoice WHERE invoice_id = ?";
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

    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("invoice_id"));
        invoice.setAmount(rs.getBigDecimal("amount"));
        invoice.setPaymentMethod(rs.getString("payment_method"));
        invoice.setPaymentDate(rs.getDate("payment_date").toLocalDate());
        invoice.setReaderId(rs.getInt("reader_id"));
        int fineId = rs.getInt("fine_id");
        if (!rs.wasNull()) {
            invoice.setFineId(fineId);
        }
        return invoice;
    }
}
