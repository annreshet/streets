package streets.common.entities;


import streets.common.wrappers.HouseWrapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="house")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "floors_amount", nullable = false)
    private int floorsAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "building_type", nullable = false)
    private BuildingType buildingType;

    @Column(name = "material", length = 20)
    private String material;

    @ManyToOne
    @JoinColumn(name = "street_id", nullable = false)
    private Street street;
    public HouseWrapper getWrapper() {
        return new HouseWrapper(id, name, date, floorsAmount, buildingType, material, street);
    }
}
