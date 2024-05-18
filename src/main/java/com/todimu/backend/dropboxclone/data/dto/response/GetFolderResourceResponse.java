package com.todimu.backend.dropboxclone.data.dto.response;

import com.todimu.backend.dropboxclone.data.entity.File;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetFolderResourceResponse {

    List<String> folderNames;
    List<File> files;
}
