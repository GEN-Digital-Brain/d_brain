package com.accept.entities;

import lombok.Data;

@Data
public class UserLogin {

    private Long id;
    private String nome;
    private String usuario;
    private String senha;
    private String foto;
    private String token;
}
