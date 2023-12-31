package streets.houses.jpaRepository;

import streets.common.entities.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
}
