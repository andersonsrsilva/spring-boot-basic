package com.basic.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    //add validation
    private Long id;
    private String name;
    private String username;
    private String password;

}
