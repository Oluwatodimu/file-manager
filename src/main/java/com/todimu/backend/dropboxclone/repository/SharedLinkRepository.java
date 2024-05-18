package com.todimu.backend.dropboxclone.repository;

import com.todimu.backend.dropboxclone.data.entity.SharedLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SharedLinkRepository extends JpaRepository<SharedLink, Long> {
    Optional<SharedLink> findByToken(UUID token);
}
