package com.ecomers.auth.infraestructure.driver_adapters;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="usuarios")//crea la tabla con los atributos como columnas

public class UsuarioData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario")
    private String idUsuario;
    private String nombre;
    @Column(length = 30,nullable = false, unique = true)//se aplica arriba de quien este
    private String correo;
    @Column(length = 8,nullable = false)
    private String clave;
    private String rol;
    private String numeroTelefonico;
    private Integer edad;

}
