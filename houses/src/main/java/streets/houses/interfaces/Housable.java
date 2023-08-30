package streets.houses.interfaces;

import streets.common.entities.House;
import streets.common.wrappers.HouseWrapper;

import java.util.List;

public interface Housable {
    House save(HouseWrapper entity);
    void deleteById(long id);
    void deleteByEntity(HouseWrapper entity);
    void deleteAll();
    House update(HouseWrapper entity);
    House getById(long id);
    List<House> getAll();
    List<House> getAllByParentId(long parentId);
}
