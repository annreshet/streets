package streets.controllers.interfaces;

import streets.common.entities.User;

import java.util.List;

public interface Userable {
    User save(User entity);
    void deleteById(long id);
    void deleteByEntity(User entity);
    void deleteAll();
    User update(User entity);
    User getById(long id);
    User getByUsername(String username);
    List<User> getAll();
}
