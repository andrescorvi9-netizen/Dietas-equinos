package com.ecomers.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Usuario {
    private String idUsuario;
    private String nombre;
    private String correo;
    private String clave;
    private String rol;
    private String numeroTelefonico;
    private Integer edad;


}
