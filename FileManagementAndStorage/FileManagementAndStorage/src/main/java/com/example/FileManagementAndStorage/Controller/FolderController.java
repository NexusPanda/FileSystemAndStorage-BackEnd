package com.example.FileManagementAndStorage.Controller;

import com.example.FileManagementAndStorage.ModelDTO.FolderDTO;
import com.example.FileManagementAndStorage.Security.Request.UpdateFolderNameRequest;
import com.example.FileManagementAndStorage.Service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<FolderDTO>> getUserFolders(Principal principal) {
        return ResponseEntity.ok(folderService.getUserFolders(principal.getName()));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<FolderDTO> createFolder(@RequestBody FolderDTO folderDTO, Principal principal) {
        FolderDTO createdFolder = folderService.createFolder(
                folderDTO.getFolderName(),
                folderDTO.getParentId(),
                principal.getName()
        );
        return ResponseEntity.ok(createdFolder);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<FolderDTO> getFolder(@PathVariable Long id) {
        return ResponseEntity.ok(folderService.getFolder(id));
    }

    @PatchMapping("/{id}/rename")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<FolderDTO> renameFolder(@PathVariable Long id, @RequestBody UpdateFolderNameRequest request) {
        return ResponseEntity.ok(folderService.renameFolder(id, request.getFolderName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
        return ResponseEntity.ok("Folder deleted successfully!");
    }
}
