package com.br.todo.controller;

import com.br.todo.dto.auth.LoginRequestDTO;
import com.br.todo.dto.auth.LoginResponseDTO;
import com.br.todo.dto.user.UserDTO;
import com.br.todo.entity.UserCreateDTO;
import com.br.todo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        String token = userService.login(loginRequestDTO);
        return new ResponseEntity<>(new LoginResponseDTO(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        return new ResponseEntity<>(userService.create(userCreateDTO), HttpStatus.CREATED);
    }
}
