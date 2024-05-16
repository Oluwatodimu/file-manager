package com.todimu.backend.dropboxclone.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file")
public class File implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String path;

    private String extension;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(name = "user_id", updatable = false)
    private Long userId;
}
