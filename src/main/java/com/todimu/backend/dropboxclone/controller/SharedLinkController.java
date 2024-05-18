package com.todimu.backend.dropboxclone.controller;

import com.todimu.backend.dropboxclone.data.dto.request.CreateSharedLinkRequest;
import com.todimu.backend.dropboxclone.data.dto.response.BaseResponse;
import com.todimu.backend.dropboxclone.data.entity.SharedLink;
import com.todimu.backend.dropboxclone.service.SharedLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/links", produces = MediaType.APPLICATION_JSON_VALUE)
public class SharedLinkController {

    public static final String SUCCESS_MESSAGE = "successful";

    private final SharedLinkService sharedLinkService;

    @PostMapping(path = "/create")
    public ResponseEntity<BaseResponse> createSharedLink(@Valid @RequestBody CreateSharedLinkRequest request) {
        log.info("user: {} creating link for object: {} ", request.getUserId(), request.getFileOrFolderId());
        SharedLink sharedLink = sharedLinkService.createSharedLink(request);
        return new ResponseEntity<>(new BaseResponse(sharedLink, SUCCESS_MESSAGE, false), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{token}")
    public ResponseEntity<BaseResponse> getSharedResource(@PathVariable(name = "token") String token) {
        log.info("getting resource with token: {}", token);
        Object resource = sharedLinkService.getSharedResource(UUID.fromString(token));
        return new ResponseEntity<>(new BaseResponse(resource, SUCCESS_MESSAGE, false), HttpStatus.OK);
    }
}
