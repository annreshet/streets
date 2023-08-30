package streets.common.wrappers;

import streets.common.entities.House;
import streets.common.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class StreetWrapper {
    private long id;
    private String name;
    private int index;
    private User user;
    private final List<House> houses = new ArrayList<>();
}
