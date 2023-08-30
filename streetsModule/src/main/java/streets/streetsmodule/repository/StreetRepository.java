package streets.streetsmodule.repository;

import streets.common.entities.Street;
import streets.common.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreetRepository extends JpaRepository<Street, Long> {
    Street getByUser(User user);
}
