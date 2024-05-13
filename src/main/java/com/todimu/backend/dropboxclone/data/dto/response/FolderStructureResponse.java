package com.todimu.backend.dropboxclone.data.dto.response;

import com.todimu.backend.dropboxclone.data.entity.File;
import com.todimu.backend.dropboxclone.data.entity.Folder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderStructureResponse {

    private List<Folder> folders;
    private List<File> files;
}
