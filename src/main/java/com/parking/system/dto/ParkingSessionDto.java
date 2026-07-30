package com.parking.system.dto;

import com.parking.system.enums.ParkingStatus;
import com.parking.system.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParkingSessionDto {
    private String id;
    private String userId;
    private String userName;
    private String vehicleId;
    private String vehicleNumber;
    private String slotId;
    private String slotNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Integer totalHours;
    private BigDecimal totalAmount;
    private ParkingStatus status;
    private PaymentStatus paymentStatus;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public String getSlotId() { return slotId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
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
}
