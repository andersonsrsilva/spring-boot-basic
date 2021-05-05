package com.basic.rest.controller;

import com.basic.rest.dto.UserRestDTO;
import com.basic.service.UserService;
import com.basic.rest.dto.CredentialsRestDTO;
import com.basic.service.dto.TokenDTO;
import com.basic.service.dto.UserServiceDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "User Controller")
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    @ApiOperation(value = "New record")
    public ResponseEntity<?> create(@RequestBody @Valid UserRestDTO userRestDTO) {
        userService.create(userRestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Edit record")
    public ResponseEntity<?> store(@RequestBody @Valid Long id, UserRestDTO userRestDTO) {
        userService.store(id, userRestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    @ApiOperation(value = "List records")
    public ResponseEntity<?> findAll() {
        List<UserServiceDTO> list = userService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find by id")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        UserServiceDTO userServiceDTO = userService.findById(id);
        return new ResponseEntity<>(userServiceDTO, HttpStatus.OK);
    }

}
