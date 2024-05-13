package com.todimu.backend.dropboxclone.data.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateFolderRequest {

    @NotEmpty(message = "folder name cannot be empty")
    private String name;


    private Long parentFolderId;
}
