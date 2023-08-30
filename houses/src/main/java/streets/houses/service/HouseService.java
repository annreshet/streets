package streets.houses.service;

import streets.common.entities.House;
import streets.common.entities.Street;
import streets.common.wrappers.HouseWrapper;
import streets.houses.interfaces.Housable;
import streets.houses.jpaRepository.HouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HouseService implements Housable {

    private final HouseRepository houseRepository;
    private final KafkaTemplate<Long, HouseWrapper> houseKafkaTemplate;
    private final KafkaTemplate<Long, List<HouseWrapper>> listKafkaTemplate;

    @KafkaListener(topics = "save house")
    @Override
    public House save(HouseWrapper houseWrapper) {
        House entity = new House(
                houseWrapper.getId(),
                houseWrapper.getName(),
                houseWrapper.getDate(),
                houseWrapper.getFloorsAmount(),
                houseWrapper.getBuildingType(),
                houseWrapper.getMaterial(),
                houseWrapper.getStreet());
        House house = houseRepository.save(entity);
        Street street = house.getStreet();
        street.getHouses().add(house);
        houseKafkaTemplate.send("send house", house.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return house;
    }

    @KafkaListener(topics = "delete house by id")
    @Override
    public void deleteById(long id) {
        houseRepository.deleteById(id);
    }

    @KafkaListener(topics = "delete house")
    @Override
    public void deleteByEntity(HouseWrapper houseWrapper) {
        House entity = new House(
            houseWrapper.getId(),
            houseWrapper.getName(),
            houseWrapper.getDate(),
            houseWrapper.getFloorsAmount(),
            houseWrapper.getBuildingType(),
            houseWrapper.getMaterial(),
            houseWrapper.getStreet());
        Street street = entity.getStreet();
        street.getHouses().remove(entity);
        houseRepository.delete(entity);
    }

    @KafkaListener(topics = "delete all houses")
    @Override
    public void deleteAll() {
        houseRepository.deleteAll();
    }

    @KafkaListener(topics = "update house")
    @Override
    public House update(HouseWrapper houseWrapper) {
        houseRepository.deleteById(houseWrapper.getId());
        House entity = new House(
                houseWrapper.getId(),
                houseWrapper.getName(),
                houseWrapper.getDate(),
                houseWrapper.getFloorsAmount(),
                houseWrapper.getBuildingType(),
                houseWrapper.getMaterial(),
                houseWrapper.getStreet());
        House house = houseRepository.save(entity);
        Street street = house.getStreet();
        street.getHouses().add(house);
        houseKafkaTemplate.send("send house", house.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return house;
    }

    @KafkaListener(topics = "get house by id")
    @Override
    public House getById(long id) {
        House house = houseRepository.getReferenceById(id);
        houseKafkaTemplate.send("send house", house.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return house;
    }

    @KafkaListener(topics = "get all houses")
    @Override
    public List<House> getAll() {
        List<House> houses = houseRepository.findAll();
        List<HouseWrapper> wrappedHouses = houses.stream().map(House::getWrapper).toList();
        listKafkaTemplate.send("send house list", wrappedHouses);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return houses;
    }

    @KafkaListener(topics = "get houses on street by street id")
    @Override
    public List<House> getAllByParentId(long parentId) {
        List<House> houses = houseRepository.findAll().stream().filter(h -> h.getStreet().getId() == parentId).toList();
        List<HouseWrapper> wrappedHouses = houses.stream().map(House::getWrapper).toList();
        listKafkaTemplate.send("send house list", wrappedHouses);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return houses;
    }
}
