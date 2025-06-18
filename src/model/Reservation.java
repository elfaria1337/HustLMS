package model;

import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private LocalDate reservationDate;
    private String status; // trạng thái như "Pending", "Approved", "Rejected", "Cancelled"
    private int readerId;
    private int titleId;

    public Reservation() {
    }

    public Reservation(int reservationId, LocalDate reservationDate, String status, int readerId, int titleId) {
        this.reservationId = reservationId;
        this.reservationDate = reservationDate;
        this.status = status;
        this.readerId = readerId;
        this.titleId = titleId;
    }

    // Getters và setters
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getReaderId() { return readerId; }
    public void setReaderId(int readerId) { this.readerId = readerId; }

    public int getTitleId() { return titleId; }
    public void setTitleId(int titleId) { this.titleId = titleId; }
}
