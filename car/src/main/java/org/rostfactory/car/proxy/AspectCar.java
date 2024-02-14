package org.rostfactory.car.proxy;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.rostfactory.car.dto.CitizenDtoResponse;
import org.rostfactory.sharemodule.enums.TypeWork;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Aspect
@Component
@RequiredArgsConstructor
public class AspectCar {
    private final Random random;

    @Around("execution(* org.rostfactory.car.service.impl.ChoosingWinnerServiceImpl.choosingWinner(..))")
    @SneakyThrows
    public CitizenDtoResponse changeWinner(ProceedingJoinPoint proceedingJoinPoint) {
        CitizenDtoResponse citizenDtoResponse = (CitizenDtoResponse) proceedingJoinPoint.proceed();
        boolean isNotFactoryJob = citizenDtoResponse.getJob() == null || !citizenDtoResponse.getJob().equals(TypeWork.FACTORY.name());
        if (isNotFactoryJob) {
            List<CitizenDtoResponse> allCitizens = (List<CitizenDtoResponse>) proceedingJoinPoint.getArgs()[0];
            return getCitizenWinnerFromFactoryJob(allCitizens);
        }
        return citizenDtoResponse;
    }

    private List<CitizenDtoResponse> getCitizensWithFactoryJob(List<CitizenDtoResponse> citizensList) {
        return citizensList.stream()
                .filter(citizen -> citizen.getJob() != null)
                .filter(citizen -> citizen.getJob().equals(TypeWork.FACTORY.name()))
                .toList();
    }

    private CitizenDtoResponse getCitizenWinnerFromFactoryJob(List<CitizenDtoResponse> citizensList) {
        List<CitizenDtoResponse> citizensListJobFactory = getCitizensWithFactoryJob(citizensList);
        int winnerNumber = random.nextInt(citizensListJobFactory.size());
        return citizensListJobFactory.get(winnerNumber);
    }
}
