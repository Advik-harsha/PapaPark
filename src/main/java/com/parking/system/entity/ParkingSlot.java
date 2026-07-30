package com.parking.system.entity;

import com.parking.system.enums.VehicleType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parking_slots")
public class ParkingSlot {

    @Id
    private String id;

    @Indexed(unique = true)
    private String slotNumber;

    private String zone;
    private boolean occupied = false;
    private VehicleType vehicleType;

    // Constructors
    public ParkingSlot() {}

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String slotNumber;
        private String zone;
        private boolean occupied = false;
        private VehicleType vehicleType;

        public Builder id(String id) { this.id = id; return this; }
        public Builder slotNumber(String slotNumber) { this.slotNumber = slotNumber; return this; }
        public Builder zone(String zone) { this.zone = zone; return this; }
        public Builder occupied(boolean occupied) { this.occupied = occupied; return this; }
        public Builder vehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; return this; }

        public ParkingSlot build() {
            ParkingSlot s = new ParkingSlot();
            s.id = this.id;
            s.slotNumber = this.slotNumber;
            s.zone = this.zone;
            s.occupied = this.occupied;
            s.vehicleType = this.vehicleType;
            return s;
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
}
