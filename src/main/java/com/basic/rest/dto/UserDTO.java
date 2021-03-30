package com.basic.rest.dto;

import javax.validation.constraints.NotBlank;

public class UserDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

}
