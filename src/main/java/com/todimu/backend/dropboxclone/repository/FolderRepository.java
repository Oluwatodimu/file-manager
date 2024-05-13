package com.todimu.backend.dropboxclone.repository;

import com.todimu.backend.dropboxclone.data.entity.Folder;
import com.todimu.backend.dropboxclone.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByUserAndParentFolderIsNull(User user);

    boolean existsByParentFolderAndName(Folder parentFolder, String name);

    boolean existsByParentFolderIsNullAndName(String name);
}
