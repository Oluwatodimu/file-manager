package com.todimu.backend.dropboxclone.controller;

import com.todimu.backend.dropboxclone.data.dto.response.BaseResponse;
import com.todimu.backend.dropboxclone.data.entity.File;
import com.todimu.backend.dropboxclone.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/files", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    public static final String SUCCESS_MESSAGE = "successful";

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<BaseResponse> uploadFiles(@RequestParam("files") List<MultipartFile> files, @RequestParam(value = "folderId", required = false) Long folderId) {
        log.info("uploading file attempt to folder: {}", folderId != null ? folderId: "root");
        List<File> uploadedFiles = fileService.uploadFiles(files, folderId);
        return new ResponseEntity<>(new BaseResponse(uploadedFiles, SUCCESS_MESSAGE, false), HttpStatus.CREATED);
    }
}
