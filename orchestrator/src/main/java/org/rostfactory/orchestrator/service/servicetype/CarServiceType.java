package org.rostfactory.orchestrator.service.servicetype;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component("carServiceType")
@Getter
public class CarServiceType extends ServiceType {
    public CarServiceType() {
        super("car","car",6,"","house");
    }
}
