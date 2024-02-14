package org.rostfactory.orchestrator.service.servicetype;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component("passportServiceType")
@Getter
public class PassportServiceType extends ServiceType {
    public PassportServiceType() {
        super("passport","passport",2,"school","bank");
    }
}
