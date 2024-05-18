package com.todimu.backend.dropboxclone.service;

import com.todimu.backend.dropboxclone.data.dto.request.CreateSharedLinkRequest;
import com.todimu.backend.dropboxclone.data.dto.response.GetFolderResourceResponse;
import com.todimu.backend.dropboxclone.data.entity.Folder;
import com.todimu.backend.dropboxclone.data.entity.SharedLink;
import com.todimu.backend.dropboxclone.exception.ActionNotAllowedException;
import com.todimu.backend.dropboxclone.repository.SharedLinkRepository;
import com.todimu.backend.dropboxclone.service.supplier.CreateSharedLinkFactory;
import com.todimu.backend.dropboxclone.service.supplier.CreateSharedLinkForObject;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SharedLinkService {

    private final SharedLinkRepository sharedLinkRepository;
    private final CreateSharedLinkFactory createSharedLinkFactory;

    public SharedLink createSharedLink(CreateSharedLinkRequest linkRequest) {
        CreateSharedLinkForObject sharedLinkForObject = createSharedLinkFactory.supply(linkRequest.getType());
        return sharedLinkForObject.createSharedLink(linkRequest);
    }

    public Object getSharedResource(UUID token) {
        SharedLink link = sharedLinkRepository.findByToken(token).orElseThrow(() -> new BadCredentialsException("token not found"));

        if (link.getFile() != null) {
            if (link.isPublic() || link.getFile().getUserId().equals(link.getUser().getId())) {
                return  link.getFile();
            }
            throw new ActionNotAllowedException("access denied");

        } else if (link.getFolder() != null) {
            if (link.isPublic() || link.getFolder().getUser().getId().equals(link.getUser().getId())) {
                List<String> folderNames = getListOfFolderNames(link.getFolder());
                return  new GetFolderResourceResponse(folderNames, link.getFolder().getFiles());
            }
            throw new ActionNotAllowedException("access denied");
        }
        return new Folder();
    }

    private List<String> getListOfFolderNames(Folder parentFolder) {
        return parentFolder.getChildFolders().stream()
                .map(Folder::getName)
                .toList();
    }
}
