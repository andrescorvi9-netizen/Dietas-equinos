package com.ecomers.auth.domain.model.gateway;

import com.ecomers.auth.domain.model.Usuario;

public interface UsuarioGateway {

   Usuario guardarusuario(Usuario usuario);

   Usuario buscarPorIdUsuario(String idUsuario);

   Usuario actualizarUsuario(Usuario usuario);

   void eliminarPorIdUsuario(String idUsuario);

}
