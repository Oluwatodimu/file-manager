package com.todimu.backend.dropboxclone.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.todimu.backend.dropboxclone.config.DoSpaceProperties;
import com.todimu.backend.dropboxclone.data.entity.File;
import com.todimu.backend.dropboxclone.data.entity.Folder;
import com.todimu.backend.dropboxclone.exception.FileAlreadyExistsException;
import com.todimu.backend.dropboxclone.exception.NotFoundException;
import com.todimu.backend.dropboxclone.repository.FileRepository;
import com.todimu.backend.dropboxclone.repository.FolderRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 amazonS3;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final DoSpaceProperties doSpaceProperties;

    @Transactional
    public List<File> uploadFiles(List<MultipartFile> files, Long folderId) {

        Folder folder = null;
        List<File> savedFiles = new ArrayList<>();

        if (folderId != null) {
            folder = folderRepository.findById(folderId).orElseThrow(() -> new NotFoundException("folder not found"));
        }

        // get files
        for (MultipartFile file: files) {

            if (file.isEmpty()) {
                log.warn("empty file was uploaded");
                continue;
            }

            String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase(Locale.ROOT);
            String fileName = FilenameUtils.removeExtension(file.getOriginalFilename()).replace(" ", "_").toLowerCase();

            if (!isFileUniqueInFolder(fileName, extension, folder)) {
                throw new FileAlreadyExistsException("file already exists in directory");
            }

            String fileUrl = generateFileUrl(fileName, extension, folder).replace(" ", "-");

            try {

                saveImageToServer(file, fileUrl);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            File newFile = File.builder()
                    .name(fileName)
                    .path(fileUrl)
                    .extension(extension)
                    .folder(folder)
                    .userId(getUserId())
                    .build();

            File savedFile = fileRepository.save(newFile);
            savedFiles.add(savedFile);
        }

        return savedFiles;
    }

    private void saveImageToServer(MultipartFile multipartFile, String url) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getInputStream().available());
        if (multipartFile.getContentType() != null && !"".equals(multipartFile.getContentType())) {
            metadata.setContentType(multipartFile.getContentType());
        }
        amazonS3.putObject(new PutObjectRequest(doSpaceProperties.getBucketName(), url, multipartFile.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private String generateFileUrl(String fileName, String ext, Folder folder) {
        return folder == null ? String.format("%s/%s/%s.%s", doSpaceProperties.getUrl(), doSpaceProperties.getBucketName(), fileName, ext):
                String.format("%s/%s/%s/%s.%s", doSpaceProperties.getUrl(), doSpaceProperties.getBucketName(), folder.getName(), fileName, ext);
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.valueOf(authentication.getName());
    }

    private boolean isFileUniqueInFolder(String fileName, String extension, Folder folder) {

        if (folder == null) {
            return !fileRepository.existsByNameAndExtensionAndFolderIsNull(fileName, extension);
        }

        return !fileRepository.existsByNameAndExtensionAndFolder(fileName, extension, folder);
    }

    public void downloadFile(Long fileId, HttpServletResponse response) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new NotFoundException("file not found"));
        S3Object s3Object = amazonS3.getObject(doSpaceProperties.getBucketName(), file.getName() + file.getExtension());

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + file.getExtension() + "\"");

        try (InputStream inputStream = s3Object.getObjectContent();
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error while downloading the file", e);
        }
    }
}
