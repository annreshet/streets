package streets.controllers.service;

import streets.common.wrappers.HouseWrapper;
import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class HouseService {
    private HouseWrapper house;
    private List<HouseWrapper> houseList;

    @KafkaListener(topics = "send house")
    public void setHouseWrapper(HouseWrapper houseWrapper) {
        this.house = houseWrapper;
    }

    @KafkaListener(topics = "send house list")
    public void setHouseWrapperList(List<HouseWrapper> houseWrapperList) {
        this.houseList = houseWrapperList;
    }
}
