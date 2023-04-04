package com.softex.figo.walletapp.controller;

import com.softex.figo.walletapp.config.security.AuthUserDetails;
import com.softex.figo.walletapp.domain.Photo;
import com.softex.figo.walletapp.response.UploadFileResponse;
import com.softex.figo.walletapp.response.WebResponse;
import com.softex.figo.walletapp.response.WebResponses;
import com.softex.figo.walletapp.service.PhotoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/profile")

public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }


    @PostMapping(value = "/uploadFile", consumes = {
            "multipart/form-data"
    })

    public ResponseEntity<WebResponse<?>> uploadFile(@RequestParam("file") MultipartFile file) {

        AuthUserDetails userDetails = (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Photo dbFile = photoService.storeFile(file, userDetails.authUser());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(String.valueOf(dbFile.getId()))
                .toUriString();

        return ResponseEntity.ok(new WebResponse<>(new UploadFileResponse(dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize())));
    }

    @PostMapping(value = "/uploadMultipleFiles", consumes = {
            "multipart/form-data"
    })
    public ResponseEntity<WebResponses<?>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {

        return ResponseEntity.ok(new WebResponses<>(Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file).getBody().data())
                .collect(Collectors.toList())));
    }

    @GetMapping(value = "/downloadFile/{fileId}", produces = "application/octet-stream")
    public ResponseEntity<Object> downloadFile(@PathVariable String fileId) {

        // Load file from database
        Photo dbFile = photoService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }


}
