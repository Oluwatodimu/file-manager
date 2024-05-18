package com.todimu.backend.dropboxclone.service.supplier;

import com.todimu.backend.dropboxclone.data.dto.request.CreateSharedLinkRequest;
import com.todimu.backend.dropboxclone.data.entity.Folder;
import com.todimu.backend.dropboxclone.data.entity.SharedLink;
import com.todimu.backend.dropboxclone.data.entity.User;
import com.todimu.backend.dropboxclone.exception.NotFoundException;
import com.todimu.backend.dropboxclone.repository.FolderRepository;
import com.todimu.backend.dropboxclone.repository.SharedLinkRepository;
import com.todimu.backend.dropboxclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateSharedLinkForFolders implements CreateSharedLinkForObject {

    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final SharedLinkRepository sharedLinkRepository;

    @Override
    @Transactional
    public SharedLink createSharedLink(CreateSharedLinkRequest linkRequest) {
        Folder folderToBeShared = folderRepository.findById(linkRequest.getFileOrFolderId()).orElseThrow(() -> new NotFoundException("file not found"));
        SharedLink sharedLink = new SharedLink();
        sharedLink.setFolder(folderToBeShared);
        sharedLink.setToken(UUID.randomUUID());
        sharedLink.setUser(getUser());
        sharedLink.setPublic(linkRequest.getIsPublic());
        return sharedLinkRepository.save(sharedLink);
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
    }
}
