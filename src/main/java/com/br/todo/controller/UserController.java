package com.br.todo.controller;

import com.br.todo.dto.user.UserDTO;
import com.br.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/current")
    public ResponseEntity<UserDTO> findCurrentUserData(@RequestHeader(name = "authorization") String authorization) {
        return new ResponseEntity<>(userService.findCurrentUserData(authorization), HttpStatus.OK);
    }
}
