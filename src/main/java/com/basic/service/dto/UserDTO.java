package com.basic.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    //private Long id;

    //add validation
    private String name;
    private String username;
    private String password;

}