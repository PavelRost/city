package org.rostfactory.police.mapper;

import org.mapstruct.Mapper;
import org.rostfactory.police.dto.PoliceFileDtoRequestCreate;
import org.rostfactory.police.dto.PoliceFileDtoResponse;
import org.rostfactory.police.entity.PoliceFile;

@Mapper(componentModel = "spring")
public interface PoliceFileMapper {
    PoliceFileDtoResponse toDto(PoliceFile policeFile);
    PoliceFile toEntityForCreate(PoliceFileDtoRequestCreate policeFileDtoRequestCreate);
}
