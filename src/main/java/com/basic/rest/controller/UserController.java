package com.basic.rest.controller;

import com.basic.service.UserService;
import com.basic.service.dto.CredentialsDTO;
import com.basic.service.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "User Controller")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/auth")
    @ApiOperation(value = "Authenticate a user")
    public ResponseEntity<?> authenticate(@RequestBody CredentialsDTO dto) {
        userService.authenticate(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping()
    @ApiOperation(value = "List users")
    public ResponseEntity<?> findAll() {
        List<UserDTO> list = userService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "Register a new user")
    public ResponseEntity<?> create(@RequestBody UserDTO dto) {
        userService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}