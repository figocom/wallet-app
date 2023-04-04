package com.softex.figo.walletapp.service;

import com.softex.figo.walletapp.domain.AuthUser;
import com.softex.figo.walletapp.domain.Photo;
import com.softex.figo.walletapp.exception.FileStorageException;
import com.softex.figo.walletapp.exception.ItemNotFoundException;
import com.softex.figo.walletapp.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    public Photo storeFile(MultipartFile file, AuthUser user) {



        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                try {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                } catch (FileStorageException e) {
                    throw new RuntimeException(e);
                }
            }
            photoRepository.deleteByUser(user);
            Photo dbFile = new Photo(user,fileName, file.getContentType(), file.getBytes());

            return photoRepository.save(dbFile);
        } catch (IOException ex) {
            try {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            } catch (FileStorageException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Transactional
    public Photo getFile(String fileId) {
        try {
            return photoRepository.findByIdAndDeleted(Long.valueOf(fileId), false)
                    .orElseThrow(() -> new ItemNotFoundException("File not found with id " + fileId));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
