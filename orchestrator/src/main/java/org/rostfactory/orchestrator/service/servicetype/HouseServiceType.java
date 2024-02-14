package org.rostfactory.orchestrator.service.servicetype;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component("houseServiceType")
@Getter
public class HouseServiceType extends ServiceType {
    public HouseServiceType() {
        super("house","house",5,"car","police");
    }
}
