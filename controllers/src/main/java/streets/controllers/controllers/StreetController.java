package streets.controllers.controllers;

import streets.common.entities.User;
import streets.common.wrappers.UserWrapper;
import streets.controllers.service.HouseService;
import streets.controllers.service.StreetService;
import streets.controllers.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import streets.common.entities.Street;
import streets.common.wrappers.HouseWrapper;
import streets.common.wrappers.StreetWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class StreetController {
    private final KafkaTemplate<Long, StreetWrapper> streetKafkaTemplate;
    private final KafkaTemplate<Long, Long> longKafkaTemplate;
    private final KafkaTemplate<Long, Void> voidKafkaTemplate;
    private final KafkaTemplate<Long, UserWrapper> userKafkaTemplate;
//     private final KafkaTemplate<Long, List> listKafkaTemplate;
    private final StreetService streetService;
    private final HouseService houseService;
    private final UserService userService;

    @Operation(summary = "Save a street")
    @PostMapping("/admin/streets/save")
    public ResponseEntity<StreetWrapper> save(@RequestBody Street entity) {
        streetKafkaTemplate.send("save street", entity.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        StreetWrapper streetWrapper = streetService.getStreet();
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/streets/save").toUriString());
        return ResponseEntity.created(uri).body(streetWrapper);
    }

    @Operation(summary = "Delete a street by ID")
    @DeleteMapping("/admin/streets/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        longKafkaTemplate.send("delete street by id", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a street")
    @DeleteMapping("/admin/streets/delete")
    public ResponseEntity<Void> deleteByEntity(@RequestBody Street entity) {
        streetKafkaTemplate.send("delete street", entity.getWrapper());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all streets")
    @DeleteMapping("/admin/streets/delete/all")
    public ResponseEntity<Void> deleteAll() {
        voidKafkaTemplate.send("delete all streets", null);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a street")
    @PutMapping("/streets/update")
    public ResponseEntity<StreetWrapper> update(@RequestBody Street entity) {
        streetKafkaTemplate.send("update street", entity.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        StreetWrapper streetWrapper = streetService.getStreet();
        return ResponseEntity.ok(streetWrapper);
    }

    @Operation(summary = "Get a street by ID")
    @GetMapping("/admin/streets/get/{id}")
    public ResponseEntity<StreetWrapper> getById(@PathVariable long id) {
        longKafkaTemplate.send("get street by id", id);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        StreetWrapper streetWrapper = streetService.getStreet();
        if (streetWrapper == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(streetWrapper);
        }
    }

    @Operation(summary = "Get list of all streets")
    @GetMapping("/admin/streets/get/all")
    public ResponseEntity<List<StreetWrapper>> getAll() {
        voidKafkaTemplate.send("get all streets", null);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<StreetWrapper> wrappedStreets = streetService.getStreetList();
        if (wrappedStreets.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(wrappedStreets);
        }
    }

    private StreetWrapper getByUser(User user) {
        userKafkaTemplate.send("get street by user", user.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return streetService.getStreet();
    }
    @Operation(summary = "Get house by ID")
    @GetMapping("/streets/get/house/{id}")
    public ResponseEntity<HouseWrapper> getHouseById(@PathVariable long id) {
        Authentication auth = SecurityContextHolder.createEmptyContext().getAuthentication();
        User user = userService.getByUsername(auth.getName());
        StreetWrapper streetWrapper = getByUser(user);
        longKafkaTemplate.send("get houses on street by street id", streetWrapper.getId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<HouseWrapper> wrappedHouses = houseService.getHouseList();
        HouseWrapper wrappedHouse = wrappedHouses.stream().filter(house -> house.getId() == id).findFirst().orElse(null);
        if (wrappedHouse == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(wrappedHouse);
        }
    }

    @Operation(summary = "Get houses")
    @GetMapping("/streets/get/houses")
    public ResponseEntity<List<HouseWrapper>> getAllHouses() {
        Authentication auth = SecurityContextHolder.createEmptyContext().getAuthentication();
        User user = userService.getByUsername(auth.getName());
        StreetWrapper streetWrapper = getByUser(user);
        longKafkaTemplate.send("get houses on street by street id", streetWrapper.getId());
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