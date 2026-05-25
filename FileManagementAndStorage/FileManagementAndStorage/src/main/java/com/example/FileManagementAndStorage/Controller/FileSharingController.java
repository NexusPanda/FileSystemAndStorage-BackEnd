package com.example.FileManagementAndStorage.Controller;

import com.example.FileManagementAndStorage.ModelDTO.FileShareDTO;
import com.example.FileManagementAndStorage.Service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileSharingController {

    @Autowired
    private ShareService sharingService;

    @PostMapping("/{id}/share")
    public ResponseEntity<?> shareFile(@PathVariable Long id, @RequestParam Long userId) {
        sharingService.shareFile(id, userId);
        return ResponseEntity.ok("File shared successfully with user " + userId);
    }

    @GetMapping("/{id}/shared")
    public ResponseEntity<List<FileShareDTO>> getSharedUsers(@PathVariable Long id) {
        return ResponseEntity.ok(sharingService.getSharedUsers(id));
    }

    @DeleteMapping("/{id}/share/{userId}")
    public ResponseEntity<?> revokeShare(@PathVariable Long id, @PathVariable Long userId) {
        sharingService.revokeShare(id, userId);
        return ResponseEntity.ok("File sharing revoked for user " + userId);
    }

    @GetMapping("/{id}/downloadpre-signedurl")
    public ResponseEntity<?> downloadSharedFile(
            @PathVariable Long id,
            @RequestParam Long userId) {

        String url = sharingService.getDownloadUrl(id, userId);
        return ResponseEntity.ok(java.util.Map.of("url", url));
    }

}
