package com.indayrental.backend.util;

import com.indayrental.backend.model.Room;
import com.indayrental.backend.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class DataInitializer implements CommandLineRunner {

    private final RoomRepository roomRepository;

    public DataInitializer(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roomRepository.count() == 0) {
            Room unit101 = Room.builder()
                    .roomNumber("Unit 101")
                    .monthlyRent(5500.0)
                    .status("AVAILABLE")
                    .description("Ground floor unit near main gate")
                    .images(List.of())
                    .maxOccupancy(2)
                    .build();

            Room unit102 = Room.builder()
                    .roomNumber("Unit 102")
                    .monthlyRent(6000.0)
                    .status("AVAILABLE")
                    .description("Second floor unit with balcony")
                    .images(List.of())
                    .maxOccupancy(4)
                    .build();

            roomRepository.save(unit101);
            roomRepository.save(unit102);

            System.out.println("🌱 Cloud database successfully seeded with initial rental units!");
        } else {
            System.out.println("✅ Rental units already exist. Seeding skipped.");
        }
    }
}