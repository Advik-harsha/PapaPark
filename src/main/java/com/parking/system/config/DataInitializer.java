package com.parking.system.config;

import com.parking.system.entity.ParkingSlot;
import com.parking.system.entity.User;
import com.parking.system.enums.Role;
import com.parking.system.enums.VehicleType;
import com.parking.system.repository.ParkingSlotRepository;
import com.parking.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ParkingSlotRepository slotRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed default parking slots if database is empty
        if (slotRepository.count() == 0) {
            List<ParkingSlot> defaultSlots = Arrays.asList(
                ParkingSlot.builder().slotNumber("A-101").zone("A").vehicleType(VehicleType.FOUR_WHEELER).occupied(false).build(),
                ParkingSlot.builder().slotNumber("A-102").zone("A").vehicleType(VehicleType.FOUR_WHEELER).occupied(false).build(),
                ParkingSlot.builder().slotNumber("A-103").zone("A").vehicleType(VehicleType.FOUR_WHEELER).occupied(false).build(),
                ParkingSlot.builder().slotNumber("A-104").zone("A").vehicleType(VehicleType.FOUR_WHEELER).occupied(false).build(),
                
                ParkingSlot.builder().slotNumber("B-201").zone("B").vehicleType(VehicleType.TWO_WHEELER).occupied(false).build(),
                ParkingSlot.builder().slotNumber("B-202").zone("B").vehicleType(VehicleType.TWO_WHEELER).occupied(false).build(),
                ParkingSlot.builder().slotNumber("B-203").zone("B").vehicleType(VehicleType.TWO_WHEELER).occupied(false).build(),
                ParkingSlot.builder().slotNumber("B-204").zone("B").vehicleType(VehicleType.TWO_WHEELER).occupied(false).build(),

                ParkingSlot.builder().slotNumber("C-301").zone("C").vehicleType(VehicleType.HEAVY_VEHICLE).occupied(false).build(),
                ParkingSlot.builder().slotNumber("C-302").zone("C").vehicleType(VehicleType.HEAVY_VEHICLE).occupied(false).build(),
                ParkingSlot.builder().slotNumber("C-303").zone("C").vehicleType(VehicleType.HEAVY_VEHICLE).occupied(false).build(),
                ParkingSlot.builder().slotNumber("C-304").zone("C").vehicleType(VehicleType.HEAVY_VEHICLE).occupied(false).build()
            );

            slotRepository.saveAll(defaultSlots);
            System.out.println("✅ Successfully seeded 12 default parking slots (Zones A, B, C)!");
        }

        // Seed default Admin user
        if (!userRepository.existsByEmail("admin@smartpark.com")) {
            User admin = User.builder()
                    .fullName("System Admin")
                    .email("admin@smartpark.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("9999999999")
                    .role(Role.ROLE_ADMIN)
                    .walletBalance(new BigDecimal("1000.00"))
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            System.out.println("👑 Default Admin User created: admin@smartpark.com / admin123");
        }

        // Seed demo User
        if (!userRepository.existsByEmail("user@smartpark.com")) {
            User demoUser = User.builder()
                    .fullName("John Doe")
                    .email("user@smartpark.com")
                    .password(passwordEncoder.encode("user123"))
                    .phone("8888888888")
                    .role(Role.ROLE_USER)
                    .walletBalance(new BigDecimal("500.00"))
                    .enabled(true)
                    .build();
            userRepository.save(demoUser);
            System.out.println("👤 Demo User created: user@smartpark.com / user123");
        }
    }
}
