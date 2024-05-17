package com.todimu.backend.dropboxclone.data.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RenameFolderRequest {


    @NotNull
    private Long folderId;

    @NotEmpty(message = "folder name cannot be empty")
    private String newFolderName;
}
