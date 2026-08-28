package com.indayrental.backend.controller;

import com.indayrental.backend.exception.ApiError;
import com.indayrental.backend.exception.SupabaseStorageException;
import com.indayrental.backend.model.Billing;
import com.indayrental.backend.model.Room;
import com.indayrental.backend.repository.BillingRepository;
import com.indayrental.backend.repository.RoomRepository;
import com.indayrental.backend.service.SupabaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private SupabaseStorageService supabaseStorageService;

    @GetMapping
    public org.springframework.data.domain.Page<Room> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return roomRepository.findAll(org.springframework.data.domain.PageRequest.of(page, size));
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody Room room) {
        if (room.getRoomNumber() == null || room.getRoomNumber().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiError("roomNumber is required", "INVALID_REQUEST", Instant.now()));
        }

        if (roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            return ResponseEntity.badRequest()
                    .body(new ApiError("roomNumber already exists", "DUPLICATE_ROOM_NUMBER", Instant.now()));
        }

        if (room.getImages() == null) {
            room.setImages(List.of());
        }

        if (room.getStatus() == null || room.getStatus().isBlank()) {
            room.setStatus("AVAILABLE");
        }

        try {
            Room savedRoom = roomRepository.save(room);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiError("Unable to create room: " + e.getMessage(), "CREATE_ROOM_FAILED", Instant.now()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        if (room.getRoomNumber() == null || room.getRoomNumber().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiError("roomNumber is required", "INVALID_REQUEST", Instant.now()));
        }

        return roomRepository.findById(id)
                .map(existingRoom -> {
                    boolean roomNumberChanged = !existingRoom.getRoomNumber().equals(room.getRoomNumber());
                    if (roomNumberChanged && roomRepository.existsByRoomNumber(room.getRoomNumber())) {
                        return ResponseEntity.badRequest()
                                .body(new ApiError("roomNumber already exists", "DUPLICATE_ROOM_NUMBER", Instant.now()));
                    }

                    existingRoom.setRoomNumber(room.getRoomNumber());
                    existingRoom.setStatus(
                            room.getStatus() == null || room.getStatus().isBlank() ? "AVAILABLE" : room.getStatus());
                    existingRoom.setMonthlyRent(room.getMonthlyRent());
                    existingRoom.setMaxOccupancy(room.getMaxOccupancy());
                    existingRoom.setImages(room.getImages() == null ? List.of() : room.getImages());
                    existingRoom.setDescription(room.getDescription());
                    existingRoom.setCurrentTenant(room.getCurrentTenant());

                    return ResponseEntity.ok(roomRepository.save(existingRoom));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiError("Room not found", "ROOM_NOT_FOUND", Instant.now())));
    }

    @PutMapping("/{id}/images")
    public ResponseEntity<Room> updateRoomImages(@PathVariable Long id, @RequestBody List<String> imageUrls) {
        return roomRepository.findById(id)
                .map(room -> {
                    room.setImages(imageUrls == null ? List.of() : imageUrls);
                    return ResponseEntity.ok(roomRepository.save(room));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        return roomRepository.findById(id)
                .map(room -> {
                    // Check for active occupant
                    if (room.getCurrentTenant() != null) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(new ApiError(
                                        "Cannot delete room because it currently has an active occupant.",
                                        "ROOM_HAS_ACTIVE_OCCUPANT",
                                        Instant.now()));
                    }

                    // Check for pending unpaid bills
                    List<Billing> roomBills = billingRepository.findByRoomId(room.getId());
                    boolean hasUnpaidBills = roomBills.stream()
                            .anyMatch(bill -> "UNPAID".equalsIgnoreCase(bill.getStatus()) || "OVERDUE".equalsIgnoreCase(bill.getStatus()));

                    if (hasUnpaidBills) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(new ApiError(
                                        "Cannot delete room because there are pending unpaid or overdue bills associated with it.",
                                        "ROOM_HAS_UNPAID_BILLS",
                                        Instant.now()));
                    }

                    try {
                        supabaseStorageService.deleteImages(room.getImages());
                    } catch (SupabaseStorageException ex) {
                        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                                .body(new ApiError(
                                        "Unable to delete room images from Supabase: " + ex.getMessage(),
                                        "SUPABASE_DELETE_FAILED",
                                        Instant.now()));
                    }

                    // Detach paid bills from the room to preserve billing history
                    for (Billing bill : roomBills) {
                        bill.setRoom(null);
                        billingRepository.save(bill);
                    }

                    roomRepository.delete(room);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiError("Room not found", "ROOM_NOT_FOUND", Instant.now())));
    }
}
