package com.todimu.backend.dropboxclone.controller;

import com.todimu.backend.dropboxclone.data.dto.request.CreateFolderRequest;
import com.todimu.backend.dropboxclone.data.dto.response.BaseResponse;
import com.todimu.backend.dropboxclone.data.dto.response.FolderStructureResponse;
import com.todimu.backend.dropboxclone.data.entity.Folder;
import com.todimu.backend.dropboxclone.service.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/folders", produces = {MediaType.APPLICATION_JSON_VALUE})
public class FolderController {

    public static final String SUCCESS_MESSAGE = "successful";

    private final FolderService folderService;

    @GetMapping(path = "/root")
    public ResponseEntity<BaseResponse> getFolderStructure(Authentication authentication) {
        log.info("getting folder structure for user: {}", authentication.getName());
        FolderStructureResponse folderStructure = folderService.getFolderStructure(Long.valueOf(authentication.getName()));
        return ResponseEntity.ok(new BaseResponse(folderStructure, SUCCESS_MESSAGE, false));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<BaseResponse> createFolder(Authentication authentication, @Valid @RequestBody CreateFolderRequest request) {
        log.info("creating folder for user: {}, with name: {}", authentication.getName(), request.getName());
        Folder newFolder = folderService.createFolder(request, Long.valueOf(authentication.getName()));
        return new ResponseEntity<>(new BaseResponse(newFolder, SUCCESS_MESSAGE, false), HttpStatus.CREATED);
    }
}
