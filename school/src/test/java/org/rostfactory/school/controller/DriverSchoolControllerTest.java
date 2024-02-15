package org.rostfactory.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.rostfactory.school.dto.DriverLicenseDtoRequest;
import org.rostfactory.school.dto.DriverLicenseDtoRequestCreate;
import org.rostfactory.school.dto.DriverLicenseDtoResponse;
import org.rostfactory.school.entity.DriverLicense;
import org.rostfactory.school.mapper.DriverSchoolMapper;
import org.rostfactory.school.service.DriverSchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DriverSchoolController.class)
class DriverSchoolControllerTest {
    @MockBean
    private DriverSchoolService driverSchoolService;
    @MockBean
    private DriverSchoolMapper driverSchoolMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllDriverLicense() throws Exception {
        List<DriverLicense> driverLicenseEntityList = List.of(
                DriverLicense.builder()
                        .id(1L)
                        .citizenId(2L)
                        .value("Петр Васильев")
                        .build()
        );
        List<DriverLicenseDtoResponse> driverLicenseDtoResponseList = List.of(
                DriverLicenseDtoResponse.builder()
                        .id(1L)
                        .value("Петр Васильев")
                        .build()
        );

        when(driverSchoolService.findAllDriverLicense()).thenReturn(driverLicenseEntityList);
        when(driverSchoolMapper.toDto(driverLicenseEntityList.get(0))).thenReturn(driverLicenseDtoResponseList.get(0));

        mockMvc.perform(get("/school/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].value").value("Петр Васильев"));

        verify(driverSchoolService, times(1)).findAllDriverLicense();
        verify(driverSchoolMapper, times(1)).toDto(driverLicenseEntityList.get(0));
    }

    @Test
    void findDriverLicenseById() throws Exception {
        DriverLicense driverLicenseEntity = DriverLicense.builder()
                .id(1L)
                .citizenId(2L)
                .value("Петр Васильев")
                .build();
        DriverLicenseDtoResponse driverLicenseDtoResponse = DriverLicenseDtoResponse.builder()
                .id(1L)
                .value("Петр Васильев")
                .build();

        when(driverSchoolService.findById(1L)).thenReturn(driverLicenseEntity);
        when(driverSchoolMapper.toDto(driverLicenseEntity)).thenReturn(driverLicenseDtoResponse);

        mockMvc.perform(get("/school/find/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value("Петр Васильев"));

        verify(driverSchoolService, times(1)).findById(1L);
        verify(driverSchoolMapper, times(1)).toDto(driverLicenseEntity);
    }

    @Test
    void findDriverLicenseByCitizenId() throws Exception {
        DriverLicense driverLicenseEntity = DriverLicense.builder()
                .id(1L)
                .citizenId(2L)
                .value("Петр Васильев")
                .build();
        DriverLicenseDtoResponse driverLicenseDtoResponse = DriverLicenseDtoResponse.builder()
                .id(1L)
                .value("Петр Васильев")
                .build();

        when(driverSchoolService.findByCitizenId(2L)).thenReturn(driverLicenseEntity);
        when(driverSchoolMapper.toDto(driverLicenseEntity)).thenReturn(driverLicenseDtoResponse);

        mockMvc.perform(get("/school/findDriverLicenseByCitizenId/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value("Петр Васильев"));

        verify(driverSchoolService, times(1)).findByCitizenId(2L);
        verify(driverSchoolMapper, times(1)).toDto(driverLicenseEntity);
    }

    @Test
    void createDriverLicense() throws Exception {
        DriverLicenseDtoRequestCreate driverLicenseDtoRequestCreate = DriverLicenseDtoRequestCreate.builder()
                .citizenId(2L)
                .value("Петр Васильев")
                .build();
        DriverLicense driverLicenseEntityBeforeSave = DriverLicense.builder()
                .citizenId(2L)
                .value("Петр Васильев")
                .build();
        DriverLicense driverLicenseEntityAfterSave = DriverLicense.builder()
                .id(1L)
                .citizenId(2L)
                .value("Петр Васильев")
                .build();
        DriverLicenseDtoResponse driverLicenseDtoResponse = DriverLicenseDtoResponse.builder()
                .id(1L)
                .value("Петр Васильев")
                .build();
        String driverLicenseDtoRequestCreateJson = objectMapper.writeValueAsString(driverLicenseDtoRequestCreate);

        when(driverSchoolMapper.toEntityForCreate(driverLicenseDtoRequestCreate)).thenReturn(driverLicenseEntityBeforeSave);
        when(driverSchoolService.create(driverLicenseEntityBeforeSave)).thenReturn(driverLicenseEntityAfterSave);
        when(driverSchoolMapper.toDto(driverLicenseEntityAfterSave)).thenReturn(driverLicenseDtoResponse);

        mockMvc.perform(post("/school/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(driverLicenseDtoRequestCreateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value("Петр Васильев"));

        verify(driverSchoolMapper, times(1)).toEntityForCreate(driverLicenseDtoRequestCreate);
        verify(driverSchoolService, times(1)).create(driverLicenseEntityBeforeSave);
        verify(driverSchoolMapper, times(1)).toDto(driverLicenseEntityAfterSave);
    }

    @Test
    void updateDriverLicense() throws Exception {
        DriverLicenseDtoRequest driverLicenseDtoRequest = DriverLicenseDtoRequest.builder()
                .id(1L)
                .citizenId(2L)
                .value("Сергей Чукотский")
                .build();
        DriverLicense driverLicenseEntity = DriverLicense.builder()
                .id(1L)
                .citizenId(2L)
                .value("Сергей Чукотский")
                .build();
        DriverLicenseDtoResponse driverLicenseDtoResponse = DriverLicenseDtoResponse.builder()
                .id(1L)
                .value("Сергей Чукотский")
                .build();
        String driverLicenseDtoRequestJson = objectMapper.writeValueAsString(driverLicenseDtoRequest);

        when(driverSchoolMapper.toEntity(driverLicenseDtoRequest)).thenReturn(driverLicenseEntity);
        when(driverSchoolService.update(driverLicenseEntity)).thenReturn(driverLicenseEntity);
        when(driverSchoolMapper.toDto(driverLicenseEntity)).thenReturn(driverLicenseDtoResponse);

        mockMvc.perform(patch("/school/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(driverLicenseDtoRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value("Сергей Чукотский"));

        verify(driverSchoolMapper, times(1)).toEntity(driverLicenseDtoRequest);
        verify(driverSchoolService, times(1)).update(driverLicenseEntity);
        verify(driverSchoolMapper, times(1)).toDto(driverLicenseEntity);
    }

    @Test
    void deleteByLicenseId() throws Exception {
        mockMvc.perform(delete("/school/deleteByLicenseId/{id}", 1L))
                .andExpect(status().isOk());

        verify(driverSchoolService, times(1)).deleteByLicenseId(1L);
    }

    @Test
    void deleteByCitizenId() throws Exception {
        mockMvc.perform(delete("/school/deleteByCitizenId/{id}", 2L))
                .andExpect(status().isOk());

        verify(driverSchoolService, times(1)).deleteByCitizenId(2L);
    }
}