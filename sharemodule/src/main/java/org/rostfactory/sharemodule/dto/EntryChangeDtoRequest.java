package org.rostfactory.sharemodule.dto;

import lombok.Builder;
import lombok.Data;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;

@Data
@Builder
public class EntryChangeDtoRequest {
    private TypeEntry typeEntry;
    private TypeOperationInLog typeOperation;
}
