package com.todimu.backend.dropboxclone.data.dto.request;

import com.todimu.backend.dropboxclone.data.enums.ObjectType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateSharedLinkRequest {

    @NotNull(message = "fileOrFolderId field cannot be null")
    private Long fileOrFolderId;

    @NotNull(message = "userId field cannot be null")
    private Long userId;

    @NotNull
    private Boolean isPublic;

    @NotNull(message = "object type cannot be null")
    private ObjectType type;
}
