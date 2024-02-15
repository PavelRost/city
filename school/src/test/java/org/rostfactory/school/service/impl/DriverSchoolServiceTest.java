package org.rostfactory.school.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rostfactory.school.client.PoliceClient;
import org.rostfactory.school.entity.DriverLicense;
import org.rostfactory.school.repository.DriverSchoolRepository;
import org.rostfactory.school.service.OcGateway;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;
import org.rostfactory.sharemodule.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverSchoolServiceTest {
    @InjectMocks
    private DriverSchoolServiceImpl driverSchoolService;
    @Mock
    private DriverSchoolRepository driverSchoolRepository;
    @Mock
    private OcGateway ocGateway;
    @Mock
    private PoliceClient policeClient;

    @Test
    void findAllDriverLicense() {
        List<DriverLicense> driverLicenseList = List.of(
                DriverLicense.builder()
                        .id(1L)
                        .value("Alex")
                        .build(),
                DriverLicense.builder()
                        .id(2L)
                        .value("Bob")
                        .build()
        );
        when(driverSchoolRepository.findAllByDeletedFalse()).thenReturn(driverLicenseList);

        List<DriverLicense> driverLicenseActual = driverSchoolService.findAllDriverLicense();

        assertEquals(driverLicenseList, driverLicenseActual);
        verify(driverSchoolRepository, times(1)).findAllByDeletedFalse();
    }

    @Test
    void whenFindByIdThenSuccess() {
        DriverLicense driverLicense = DriverLicense.builder()
                .id(1L)
                .value("Alex")
                .build();
        when(driverSchoolRepository.findDriverLicenseByIdAndDeletedFalse(driverLicense.getId())).thenReturn(Optional.of(driverLicense));

        DriverLicense driverLicenseActual = driverSchoolService.findById(driverLicense.getId());

        assertEquals(driverLicense, driverLicenseActual);
        verify(driverSchoolRepository, times(1)).findDriverLicenseByIdAndDeletedFalse(driverLicense.getId());
    }

    @Test
    void whenFindByIdThenException() {
        DriverLicense driverLicense = DriverLicense.builder()
                .id(1L)
                .value("Alex")
                .build();
        when(driverSchoolRepository.findDriverLicenseByIdAndDeletedFalse(driverLicense.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()-> driverSchoolService.findById(driverLicense.getId()));
    }

    @Test
    void whenFindByCitizenIdThenSuccess() {
        DriverLicense driverLicense = DriverLicense.builder()
                .id(1L)
                .citizenId(1L)
                .value("Alex")
                .build();
        when(driverSchoolRepository.findDriverLicenseByCitizenIdAndDeletedFalse(driverLicense.getCitizenId())).thenReturn(Optional.of(driverLicense));

        DriverLicense driverLicenseActual = driverSchoolService.findByCitizenId(driverLicense.getCitizenId());

        assertEquals(driverLicense, driverLicenseActual);
        verify(driverSchoolRepository, times(1)).findDriverLicenseByCitizenIdAndDeletedFalse(driverLicense.getCitizenId());
    }

    @Test
    void whenFindByCitizenIdThenException() {
        DriverLicense driverLicense = DriverLicense.builder()
                .id(1L)
                .citizenId(1L)
                .value("Alex")
                .build();
        when(driverSchoolRepository.findDriverLicenseByCitizenIdAndDeletedFalse(driverLicense.getCitizenId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()-> driverSchoolService.findByCitizenId(driverLicense.getCitizenId()));
    }

    @Test
    void create() {
        DriverLicense driverLicenseEntityBeforeSave = DriverLicense.builder()
                .value("Alex")
                .build();
        DriverLicense driverLicenseEntityAfterSave = DriverLicense.builder()
                .id(1L)
                .value("Alex")
                .build();
        when(driverSchoolRepository.save(driverLicenseEntityBeforeSave)).thenReturn(driverLicenseEntityAfterSave);

        DriverLicense driverLicenseActual = driverSchoolService.create(driverLicenseEntityBeforeSave);

        assertEquals(driverLicenseEntityAfterSave, driverLicenseActual);
        verify(driverSchoolRepository, times(1)).save(driverLicenseEntityBeforeSave);
        verify(policeClient, times(1)).createAndSendPoliceFile(driverLicenseEntityAfterSave);
        verify(ocGateway, times(1)).sendEntryInLog(TypeEntry.LICENSE, TypeOperationInLog.INCREMENT);
    }

    @Test
    void whenUpdateThenSuccess() {
        DriverLicense driverLicense = DriverLicense.builder()
                .id(1L)
                .value("Alex")
                .build();
        when(driverSchoolRepository.existsByIdAndDeletedFalse(driverLicense.getId())).thenReturn(true);
        when(driverSchoolRepository.save(driverLicense)).thenReturn(driverLicense);

        DriverLicense driverLicenseActual = driverSchoolService.update(driverLicense);

        assertEquals(driverLicense, driverLicenseActual);
        verify(driverSchoolRepository, times(1)).existsByIdAndDeletedFalse(driverLicense.getId());
        verify(driverSchoolRepository, times(1)).save(driverLicense);
    }

    @Test
    void whenUpdateThenException() {
        DriverLicense driverLicense = DriverLicense.builder()
                .id(1L)
                .value("Alex")
                .build();
        when(driverSchoolRepository.existsByIdAndDeletedFalse(driverLicense.getId())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, ()-> driverSchoolService.update(driverLicense));
        verify(driverSchoolRepository, times(1)).existsByIdAndDeletedFalse(driverLicense.getId());
        verify(driverSchoolRepository, times(0)).save(driverLicense);
    }

    @Test
    void deleteByCitizenId() {
        DriverLicense driverLicense = DriverLicense.builder()
                .id(1L)
                .citizenId(1L)
                .value("Alex")
                .build();
        when(driverSchoolRepository.findDriverLicenseByCitizenIdAndDeletedFalse(driverLicense.getCitizenId())).thenReturn(Optional.of(driverLicense));

        driverSchoolService.deleteByCitizenId(driverLicense.getCitizenId());

        verify(driverSchoolRepository, times(1)).findDriverLicenseByCitizenIdAndDeletedFalse(driverLicense.getCitizenId());
        verify(driverSchoolRepository, times(1)).delete(driverLicense);
        verify(ocGateway, times(1)).sendEntryInLog(TypeEntry.LICENSE, TypeOperationInLog.DECREMENT);
    }

    @Test
    void deleteByLicenseId() {
        DriverLicense driverLicense = DriverLicense.builder()
                .id(1L)
                .citizenId(1L)
                .value("Alex")
                .build();
        when(driverSchoolRepository.findDriverLicenseByIdAndDeletedFalse(driverLicense.getId())).thenReturn(Optional.of(driverLicense));

        driverSchoolService.deleteByLicenseId(driverLicense.getId());

        verify(driverSchoolRepository, times(1)).findDriverLicenseByIdAndDeletedFalse(driverLicense.getId());
        verify(policeClient, times(1)).deletePoliceFileByCitizenId(driverLicense.getCitizenId());
        verify(driverSchoolRepository, times(1)).delete(driverLicense);
        verify(ocGateway, times(1)).sendEntryInLog(TypeEntry.LICENSE, TypeOperationInLog.DECREMENT);
    }
}