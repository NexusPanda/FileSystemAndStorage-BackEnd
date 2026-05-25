package com.example.FileManagementAndStorage.Repository;

import com.example.FileManagementAndStorage.Model.FileShare;
import com.example.FileManagementAndStorage.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileShareRepository extends JpaRepository<FileShare, Long> {
    // returns all FileShare rows for a file
    List<FileShare> findByFileId(Long fileId);

    // returns the share record for fileId + sharedWith user id
    Optional<FileShare> findByFileIdAndSharedWithId(Long fileId, Long sharedWithId);

    List<FileShare> findBySharedWith(UserEntity user);
}
