package org.rostfactory.orchestrator.service.servicetype;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component("schoolServiceType")
@Getter
public class SchoolServiceType extends ServiceType {
    public SchoolServiceType() {
        super("school","license",3,"police","passport");
    }
}
