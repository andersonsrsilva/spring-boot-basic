package com.basic.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(unique = true, name = "username", nullable = false, length = 80)
    private String username;

    @Column(name = "password", nullable = false, length = 128)
    private String password;

}

