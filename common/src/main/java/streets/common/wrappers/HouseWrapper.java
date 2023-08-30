package streets.common.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import streets.common.entities.Street;
import streets.common.entities.BuildingType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class HouseWrapper {
    private long id;
    private String name;
    private LocalDate date;
    private int floorsAmount;
    private BuildingType buildingType;
    private String material;
    private Street street;
}
