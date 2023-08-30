package streets.streetsmodule.interfaces;

import streets.common.entities.Street;
import streets.common.wrappers.StreetWrapper;

import java.util.List;

public interface Streetable {
    Street save(StreetWrapper entity);
    void deleteById(long id);
    void deleteByEntity(StreetWrapper entity);
    void deleteAll();
    Street update(StreetWrapper entity);
    Street getById(long id);
//    Street getByUsername(String username);
    List<Street> getAll();
}
