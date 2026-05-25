package com.example.FileManagementAndStorage.Repository;

import com.example.FileManagementAndStorage.Model.FileModel;
import com.example.FileManagementAndStorage.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileModel, Long> {
    List<FileModel> findByOwnerAndIsDeletedFalse(UserEntity owner);
    List<FileModel> findByOwnerAndIsDeletedTrue(UserEntity owner);
    List<FileModel> findByOwner(UserEntity owner);
}
