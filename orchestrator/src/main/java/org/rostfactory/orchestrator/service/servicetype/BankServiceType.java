package org.rostfactory.orchestrator.service.servicetype;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component("bankServiceType")
@Getter
public class BankServiceType extends ServiceType {
    public BankServiceType() {
        super("bank","account",1,"passport","citizen");
    }
}
