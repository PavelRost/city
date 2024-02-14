package org.rostfactory.orchestrator.service.servicetype;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component("policeServiceType")
@Getter
public class PoliceServiceType extends ServiceType {
    public PoliceServiceType() {
        super("police","license",4,"house","school");
    }
}
