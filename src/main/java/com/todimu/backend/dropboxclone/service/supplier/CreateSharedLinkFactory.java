package com.todimu.backend.dropboxclone.service.supplier;

import com.todimu.backend.dropboxclone.data.enums.ObjectType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateSharedLinkFactory {

    private final CreateSharedLinkForFiles createSharedLinkForFiles;
    private final CreateSharedLinkForFolders createSharedLinkForFolders;

    public CreateSharedLinkForObject supply(ObjectType objectType) {
        return switch (objectType) {
            case FILE -> createSharedLinkForFiles;
            case FOLDER -> createSharedLinkForFolders;
        };
    }
}
