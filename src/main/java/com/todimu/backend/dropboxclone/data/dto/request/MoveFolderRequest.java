package com.todimu.backend.dropboxclone.data.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MoveFolderRequest {

    @NotNull
    private Long folderId;

    private Long destinationFolderId;
}
