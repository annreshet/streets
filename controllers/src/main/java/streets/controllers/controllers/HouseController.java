package streets.controllers.controllers;

import streets.common.entities.House;
import streets.common.wrappers.HouseWrapper;
import streets.controllers.service.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class HouseController {
    private final KafkaTemplate<Long, HouseWrapper> houseKafkaTemplate;
    private final KafkaTemplate<Long, Long> longKafkaTemplate;
    private final KafkaTemplate<Long, Void> voidKafkaTemplate;
    private final HouseService houseService;

    @Operation(summary = "Save a house")
    @PostMapping("/admin/houses/save")
    public ResponseEntity<HouseWrapper> save(@RequestBody House entity) {
        houseKafkaTemplate.send("save house", entity.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        HouseWrapper houseWrapper = houseService.getHouse();

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/houses/save").toUriString());
        return ResponseEntity.created(uri).body(houseWrapper);
    }

    @Operation(summary = "Delete a house by ID")
    @DeleteMapping("/admin/houses/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        longKafkaTemplate.send("delete house by id", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a house")
    @DeleteMapping("/admin/houses/delete")
    public ResponseEntity<Void> deleteByEntity(@RequestBody House entity) {
        houseKafkaTemplate.send("delete house", entity.getWrapper());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all houses")
    @DeleteMapping("/admin/houses/delete/all")
    public ResponseEntity<Void> deleteAll() {
        voidKafkaTemplate.send("delete all houses", null);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a house")
    @PutMapping("/admin/houses/update")
    public ResponseEntity<HouseWrapper> update(@RequestBody House entity) {
        houseKafkaTemplate.send("update house", entity.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        HouseWrapper houseWrapper = houseService.getHouse();
        return ResponseEntity.ok(houseWrapper);
    }

    @Operation(summary = "Get house by ID")
    @GetMapping("/admin/houses/get/{id}")
    public ResponseEntity<HouseWrapper> getById(@PathVariable long id) {
        longKafkaTemplate.send("get house by id", id);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        HouseWrapper houseWrapper = houseService.getHouse();
        if (houseWrapper == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(houseWrapper);
        }
    }

    @Operation(summary = "Get list of all houses")
    @GetMapping("/admin/houses/get/all")
    public ResponseEntity<List<HouseWrapper>> getAll() {
        voidKafkaTemplate.send("get all houses", null);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<HouseWrapper> wrappedHouses = houseService.getHouseList();
        if (wrappedHouses.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(wrappedHouses);
        }
    }

    @Operation(summary = "Get list of all houses on street")
    @GetMapping("/admin/get/houses/{parentId}")
    public ResponseEntity<List<HouseWrapper>> getAllByParentId(@PathVariable long parentId) {
        longKafkaTemplate.send("get houses on street by street id", parentId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<HouseWrapper> wrappedHouses = houseService.getHouseList();
        if (wrappedHouses.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(wrappedHouses);
        }
    }
}