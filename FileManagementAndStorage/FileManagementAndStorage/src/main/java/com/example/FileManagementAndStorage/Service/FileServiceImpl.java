package com.example.FileManagementAndStorage.Service;

import com.example.FileManagementAndStorage.Exception.ResourceNotFoundException;
import com.example.FileManagementAndStorage.Model.FileModel;
import com.example.FileManagementAndStorage.Model.Folder;
import com.example.FileManagementAndStorage.Model.UserEntity;
import com.example.FileManagementAndStorage.ModelDTO.FileDTO;
import com.example.FileManagementAndStorage.Repository.FileRepository;
import com.example.FileManagementAndStorage.Repository.FileShareRepository;
import com.example.FileManagementAndStorage.Repository.FolderRepository;
import com.example.FileManagementAndStorage.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileShareRepository fileShareRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private S3Client s3Client;

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public FileDTO uploadFile(MultipartFile multipartFile, Long folderId, String username) {
        try {
            UserEntity owner = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));

            Folder folder = null;
            if (folderId != null) {
                folder = folderRepository.findById(folderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Folder", "Folder_Id", folderId));
            }

            // Generate unique S3 object key
            String key = owner.getUsername() + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

            // Upload to S3
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(multipartFile.getContentType())
                            .build(),
                    RequestBody.fromBytes(multipartFile.getBytes())
            );

            FileModel file = FileModel.builder()
                    .fileName(multipartFile.getOriginalFilename())
                    .type(multipartFile.getContentType())
                    .size(multipartFile.getSize())
                    .path(key) // store only S3 key
                    .owner(owner)
                    .folder(folder)
                    .isDeleted(false)
                    .build();

            FileModel saved = fileRepository.save(file);
            return modelMapper.map(saved, FileDTO.class);

        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(Long id) {
        FileModel file = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File", "File_Id", id));

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(file.getPath()) // stored S3 key
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes =
                    s3Client.getObjectAsBytes(getObjectRequest);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getFileName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, file.getType())
                    .body(objectBytes.asByteArray());

        } catch (S3Exception e) {
            throw new RuntimeException("Error downloading file from S3", e);
        }
    }

    @Override
    public void softDelete(Long id) {
        FileModel file = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File", "File_Id", id));
        file.setDeleted(true);
        fileRepository.save(file);
    }

    @Override
    public void restoreFile(Long id) {
        FileModel file = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File", "File_Id", id));
        file.setDeleted(false);
        fileRepository.save(file);
    }

    @Override
    public void permanentDelete(Long id) {
        FileModel file = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File", "File_Id", id));

        String key = file.getPath(); // assuming it stores the full S3 key

        try {
            DeleteObjectResponse response = s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());

            log.info("File deleted from S3 successfully: {}, version: {}", key, response.versionId());

            fileRepository.delete(file);
            log.info("File record deleted from database: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete file from S3 for key: {}", key, e);
            throw new RuntimeException("Error deleting file from S3", e);
        }
    }

    @Override
    public List<FileDTO> getUserFiles(String username) {
        UserEntity owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));
        return fileRepository.findByOwner(owner).stream()
                .map(file -> modelMapper.map(file, FileDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FileDTO> getSharedWithMe(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));
        return fileShareRepository.findBySharedWith(user).stream()
                .map(share -> {
                    FileDTO dto = modelMapper.map(share.getFile(), FileDTO.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
