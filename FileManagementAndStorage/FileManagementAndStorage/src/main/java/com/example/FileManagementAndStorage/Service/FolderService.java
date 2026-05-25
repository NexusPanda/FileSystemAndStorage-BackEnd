package com.example.FileManagementAndStorage.Service;

import com.example.FileManagementAndStorage.ModelDTO.FolderDTO;

import java.util.List;

public interface FolderService {
    FolderDTO createFolder(String name, Long id, String username);

    FolderDTO getFolder(Long id);

    FolderDTO renameFolder(Long id, String folderName);

    void deleteFolder(Long id);

    List<FolderDTO> getUserFolders(String username);
}