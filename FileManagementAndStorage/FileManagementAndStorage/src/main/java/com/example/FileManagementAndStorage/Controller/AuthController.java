package com.example.FileManagementAndStorage.Controller;


import com.example.FileManagementAndStorage.ModelDTO.UserDTO;
import com.example.FileManagementAndStorage.Security.Request.LoginRequest;
import com.example.FileManagementAndStorage.Security.Request.SignUpRequest;
import com.example.FileManagementAndStorage.Security.Response.LoginResponse;
import com.example.FileManagementAndStorage.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signup){
        UserDTO userDTO = userService.register(signup);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        LoginResponse login = userService.login(loginRequest);
        return new ResponseEntity<>(login, HttpStatus.OK);
    }


    @GetMapping("/auth/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = userService.getUserByUsername(auth.getName());
        return ResponseEntity.ok(user);
    }

}
