package com.todimu.backend.dropboxclone.repository;

import com.todimu.backend.dropboxclone.data.entity.File;
import com.todimu.backend.dropboxclone.data.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByUserIdAndFolderIsNull(Long userId);

    boolean existsByNameAndExtensionAndFolder(String name, String extension, Folder folder);

    boolean existsByNameAndExtensionAndFolderIsNull(String name, String extension);
}
