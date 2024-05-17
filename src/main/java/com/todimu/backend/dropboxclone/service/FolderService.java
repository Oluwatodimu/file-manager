package com.todimu.backend.dropboxclone.service;

import com.todimu.backend.dropboxclone.data.dto.request.CreateFolderRequest;
import com.todimu.backend.dropboxclone.data.dto.request.MoveFolderRequest;
import com.todimu.backend.dropboxclone.data.dto.request.RenameFolderRequest;
import com.todimu.backend.dropboxclone.data.dto.response.FolderStructureResponse;
import com.todimu.backend.dropboxclone.data.entity.File;
import com.todimu.backend.dropboxclone.data.entity.Folder;
import com.todimu.backend.dropboxclone.data.entity.User;
import com.todimu.backend.dropboxclone.exception.FolderAlreadyExistsException;
import com.todimu.backend.dropboxclone.exception.NotFoundException;
import com.todimu.backend.dropboxclone.repository.FileRepository;
import com.todimu.backend.dropboxclone.repository.FolderRepository;
import com.todimu.backend.dropboxclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;


    public FolderStructureResponse getFolderStructure(Long id) {
        User loggedInUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));
        List<Folder> rootFolders = folderRepository.findByUserAndParentFolderIsNull(loggedInUser);
        List<File> rootFiles = fileRepository.findByUserIdAndFolderIsNull(loggedInUser.getId());
        return new FolderStructureResponse(rootFolders, rootFiles);
    }


    @Transactional
    public Folder createFolder(CreateFolderRequest request, Long userId) {

        if (!isFolderNameUnique(request)) {
            throw new FolderAlreadyExistsException("folder name already exists");
        }

        User loggedInUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));

        Folder newFolder = new Folder();
        newFolder.setName(request.getName());
        newFolder.setUser(loggedInUser);

        if (request.getParentFolderId() != null) {
            Folder parentFolder = folderRepository.findById(request.getParentFolderId()).orElseThrow(() -> new NotFoundException("folder not found"));
            newFolder.setParentFolder(parentFolder);
        }

        return folderRepository.save(newFolder);
    }

    public boolean isFolderNameUnique(CreateFolderRequest request) {

        if (request.getParentFolderId() == null) {
            return !folderRepository.existsByParentFolderIsNullAndName(request.getName());
        }

        Folder folder = folderRepository.findById(request.getParentFolderId()).orElseThrow(() -> new NotFoundException("folder not found"));
        return !folderRepository.existsByParentFolderAndName(folder, request.getName());
    }

    public Folder changeFolderName(RenameFolderRequest request) {
        Folder folderToBeRenamed = folderRepository.findById(request.getFolderId()).orElseThrow(() -> new NotFoundException("folder not found"));
        folderToBeRenamed.setName(request.getNewFolderName());
        return folderRepository.save(folderToBeRenamed);
    }

    public Folder moveFolder(MoveFolderRequest request) {
        Folder folderToBeMoved = folderRepository.findById(request.getFolderId()).orElseThrow(() -> new NotFoundException("folder not found"));

        if (request.getDestinationFolderId() == null) { // saved to root directory
            folderToBeMoved.setParentFolder(null);
            return folderRepository.save(folderToBeMoved);
        }

        Folder destinationFolder = folderRepository.findById(request.getDestinationFolderId()).orElseThrow(() -> new NotFoundException("destination folder not found"));
        folderToBeMoved.setParentFolder(destinationFolder);
        return folderRepository.save(folderToBeMoved);
    }
}
