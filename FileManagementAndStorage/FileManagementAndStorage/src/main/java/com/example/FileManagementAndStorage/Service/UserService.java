package com.example.FileManagementAndStorage.Service;

import com.example.FileManagementAndStorage.ModelDTO.UserDTO;
import com.example.FileManagementAndStorage.Security.Request.LoginRequest;
import com.example.FileManagementAndStorage.Security.Request.SignUpRequest;
import com.example.FileManagementAndStorage.Security.Response.LoginResponse;
import jakarta.validation.Valid;

public interface UserService {
    UserDTO register(@Valid SignUpRequest signup);

    LoginResponse login(LoginRequest loginRequest);

    UserDTO getUserByUsername(String username);
}
