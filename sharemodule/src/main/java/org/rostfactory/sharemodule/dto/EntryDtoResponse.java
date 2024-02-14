package org.rostfactory.sharemodule.dto;

import lombok.Data;
import org.rostfactory.sharemodule.enums.TypeEntry;

import java.time.LocalDateTime;

@Data
public class EntryDtoResponse {
    private TypeEntry type;
    private Long quantity;
    private LocalDateTime dateTime;
}
