package org.rostfactory.log.mapper;

import org.mapstruct.Mapper;
import org.rostfactory.log.entity.Entry;
import org.rostfactory.sharemodule.dto.EntryDtoResponse;

@Mapper(componentModel = "spring")
public interface EntryMapper {
    EntryDtoResponse toDto(Entry entry);
}
