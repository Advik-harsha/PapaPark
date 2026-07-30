package com.parking.system.entity;

import com.parking.system.enums.ParkingStatus;
import com.parking.system.enums.PaymentStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "parking_sessions")
public class ParkingSession {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Vehicle vehicle;

    @DBRef
    private ParkingSlot parkingSlot;

    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Integer totalHours;
    private BigDecimal totalAmount;

    private ParkingStatus status = ParkingStatus.ACTIVE;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @DBRef
    private Payment payment;

    // Constructors
    public ParkingSession() {}

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private User user;
        private Vehicle vehicle;
        private ParkingSlot parkingSlot;
        private LocalDateTime entryTime;
        private ParkingStatus status = ParkingStatus.ACTIVE;
        private PaymentStatus paymentStatus = PaymentStatus.PENDING;

        public Builder id(String id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder vehicle(Vehicle vehicle) { this.vehicle = vehicle; return this; }
        public Builder parkingSlot(ParkingSlot parkingSlot) { this.parkingSlot = parkingSlot; return this; }
        public Builder entryTime(LocalDateTime entryTime) { this.entryTime = entryTime; return this; }
        public Builder status(ParkingStatus status) { this.status = status; return this; }
        public Builder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }

        public ParkingSession build() {
            ParkingSession s = new ParkingSession();
            s.id = this.id;
            s.user = this.user;
            s.vehicle = this.vehicle;
            s.parkingSlot = this.parkingSlot;
            s.entryTime = this.entryTime;
            s.status = this.status;
            s.paymentStatus = this.paymentStatus;
            return s;
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public ParkingSlot getParkingSlot() { return parkingSlot; }
    public void setParkingSlot(ParkingSlot parkingSlot) { this.parkingSlot = parkingSlot; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public Integer getTotalHours() { return totalHours; }
    public void setTotalHours(Integer totalHours) { this.totalHours = totalHours; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public ParkingStatus getStatus() { return status; }
    public void setStatus(ParkingStatus status) { this.status = status; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
}
