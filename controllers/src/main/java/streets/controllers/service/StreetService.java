package streets.controllers.service;

import streets.common.wrappers.StreetWrapper;
import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class StreetService {
    private StreetWrapper street;
    private List<StreetWrapper> streetList;

    @KafkaListener(topics = "send street")
    public void setStreetWrapper(StreetWrapper streetWrapper) {
        this.street = streetWrapper;
    }

    @KafkaListener(topics = "send street list")
    public void setStreetWrapperList(List<StreetWrapper> streetWrapperList) {
        this.streetList = streetWrapperList;
    }
}
