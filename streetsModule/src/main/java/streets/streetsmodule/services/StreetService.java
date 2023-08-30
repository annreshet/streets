package streets.streetsmodule.services;

import streets.common.entities.Street;
import streets.common.entities.User;
import streets.common.wrappers.StreetWrapper;
import streets.common.wrappers.UserWrapper;
import streets.streetsmodule.interfaces.Streetable;
import streets.streetsmodule.repository.StreetRepository;
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
public class StreetService implements Streetable {
    private final StreetRepository streetRepository;
    private final KafkaTemplate<Long, StreetWrapper> streetKafkaTemplate;
    private final KafkaTemplate<Long, List<StreetWrapper>> listKafkaTemplate;
    @KafkaListener(topics = "save street")
    @Override
    public Street save(StreetWrapper streetWrapper) {
        Street entity = new Street(streetWrapper.getId(), streetWrapper.getName(), streetWrapper.getIndex(), streetWrapper.getUser());
        Street street = streetRepository.save(entity);
        streetKafkaTemplate.send("send street", street.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return street;
    }

    @KafkaListener(topics = "delete street by id")
    @Override
    public void deleteById(long id) {
        streetRepository.deleteById(id);
    }

    @KafkaListener(topics = "delete street")
    @Override
    public void deleteByEntity(StreetWrapper streetWrapper) {
        Street entity = new Street(streetWrapper.getId(), streetWrapper.getName(), streetWrapper.getIndex(), streetWrapper.getUser());
        streetRepository.delete(entity);
    }

    @KafkaListener(topics = "delete all streets")
    @Override
    public void deleteAll() {
        streetRepository.deleteAll();
    }

    @KafkaListener(topics = "update street")
    @Override
    public Street update(StreetWrapper streetWrapper) {
        Street entity = new Street(streetWrapper.getId(), streetWrapper.getName(), streetWrapper.getIndex(), streetWrapper.getUser());
        Street street = streetRepository.save(entity);
        streetKafkaTemplate.send("send street", street.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return street;
    }

    @KafkaListener(topics = "get street by id")
    @Override
    public Street getById(long id) {
        Street street = streetRepository.getReferenceById(id);
        streetKafkaTemplate.send("send street", street.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return street;
    }

    @KafkaListener(topics = "get all streets")
    @Override
    public List<Street> getAll() {
        List<Street> streetList = streetRepository.findAll();
        List<StreetWrapper> wrappedStreets = streetList.stream().map(Street::getWrapper).toList();
        listKafkaTemplate.send("send street list", wrappedStreets);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return streetList;
    }

    @KafkaListener(topics = "get street by user")
    public Street getByUser(UserWrapper userWrapper) {
        User user = new User(userWrapper.getId(), userWrapper.getUsername(), userWrapper.getPassword(), userWrapper.getRole(), userWrapper.getStreet());
        Street street = streetRepository.getByUser(user);
        streetKafkaTemplate.send("send street", street.getWrapper());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return street;
    }
}
