package org.rostfactory.house.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.rostfactory.house.service.OcGateway;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;
import org.rostfactory.starter.annotation.EnableSauronWs;
import org.rostfactory.starter.service.LogOcService;
import org.springframework.stereotype.Service;

@Service
@EnableSauronWs
@AllArgsConstructor
public class OcGatewayImpl implements OcGateway {
    private LogOcService logOcService;

    @PostConstruct
    void init() throws ClassNotFoundException {
        Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass("org.rostfactory.house.service.impl.OcGatewayImpl");
        if (!clazz.isAnnotationPresent(EnableSauronWs.class)) logOcService = null;
    }

    @Override
    public void sendEntryInLog(TypeEntry typeEntry, TypeOperationInLog typeOperationInLog) {
        if (logOcService == null) {
            System.out.println("Логирование недоступно. Добавьте аннотацию @EnableSauronWs");
            return;
        }
        logOcService.log(typeEntry, typeOperationInLog);
        System.out.println("Логирование выполнено успешно");
    }
}
