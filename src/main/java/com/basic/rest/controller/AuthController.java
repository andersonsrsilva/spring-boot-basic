package com.basic.rest.controller;

import com.basic.rest.dto.CredentialsRestDTO;
import com.basic.service.AuthService;
import com.basic.service.dto.TokenDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(tags = "Auth Controller")
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping()
    @ApiOperation(value = "Authenticate a user")
    public ResponseEntity<?> authenticate(@RequestBody @Valid CredentialsRestDTO credentialsDTO) {
        TokenDTO tokenDTO = authService.authenticate(credentialsDTO);
        return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
    }

}
