package com.ecomers.auth.domain.usecase;

import com.ecomers.auth.domain.model.Usuario;
import com.ecomers.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGteway;

    public Usuario guardarusuario(Usuario usuario) {

        if (usuario.getCorreo() == null || usuario.getClave() == null){

            throw new NullPointerException("correo o clave null");
        }

        if (usuario.getEdad()< 18){

            throw new RuntimeException("ojo con eso Menor");
        }

        String nombre = usuario.getNombre() != null ? usuario.getNombre() : campoNulo("nombre");
        String rol = usuario.getRol() != null ? usuario.getRol() : campoNulo("rol");
        String telefono = usuario.getNumeroTelefonico() != null ? usuario.getNumeroTelefonico() : campoNulo("numeroTelefonico");
        Integer edad = usuario.getEdad() != null ? usuario.getEdad() : campoNulo("edad");

        Usuario  usuarioGuardado = usuarioGteway.guardarusuario(usuario);

        return usuarioGuardado;
    }


    private <T> T campoNulo(String nombreCampo) {
        throw new NullPointerException(nombreCampo + " no puede ser null");
    }

    public Usuario buscarPorIdUsuario(String id) {

        try {
        return usuarioGteway.buscarPorIdUsuario(id);
        }
        catch (Exception e){
        System.out.println(e.getMessage());
        Usuario usuarioVacio = new Usuario();
        return usuarioVacio;
         }

    }


    public void eliminarPorIdUsuario (String id){

        try {
            usuarioGteway.eliminarPorIdUsuario(id);
        }
        catch (Exception e){

            System.out.println(e.getMessage());
        }


    }


    public Usuario actualizarUsuario(Usuario usuario){

        if (usuario.getIdUsuario()==null){

            throw new IllegalArgumentException("El ID es obligatorio para actualizar");
        }
        return usuarioGteway.actualizarUsuario(usuario);
    }


}