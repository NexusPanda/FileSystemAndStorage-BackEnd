package com.example.FileManagementAndStorage.Service;

import com.example.FileManagementAndStorage.ModelDTO.FileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    FileDTO uploadFile(MultipartFile multipartFile, Long folderId, String username);

    ResponseEntity<byte[]> downloadFile(Long id);

    void softDelete(Long id);

    void restoreFile(Long id);

    void permanentDelete(Long id);

    List<FileDTO> getUserFiles(String username);

    List<FileDTO> getSharedWithMe(String username);
}
