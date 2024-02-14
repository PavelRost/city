package org.rostfactory.orchestrator.service.servicetype;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component("citizenServiceType")
public class CitizenServiceType extends ServiceType {
    public CitizenServiceType() {
        super("citizen","citizen",0,"bank","");
    }
}
