package com.ecomers.auth.infraestructure.driver_adapters;

import com.ecomers.auth.domain.model.Usuario;
import com.ecomers.auth.domain.model.gateway.UsuarioGateway;
import com.ecomers.auth.infraestructure.mapper.MapperUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor

public class UsurioDataGatewayImpl implements UsuarioGateway {

    private final MapperUsuario mapperUsuario;
    private final UsuarioDataJpaRepository repository;

    @Override
    public Usuario guardarusuario(Usuario usuario){

        UsuarioData usuarioData = mapperUsuario.toData(usuario);
        return mapperUsuario.toUsuario(repository.save(usuarioData));

    }



    @Override
    public Usuario buscarPorIdUsuario(String idUsuario) {
        return repository.findById(idUsuario)
                .map(usuarioData -> mapperUsuario.toUsuario(usuarioData))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {

        UsuarioData usuarioData = mapperUsuario.toData(usuario);
        UsuarioData usuarioGuardado = repository.save(usuarioData);

        if (!repository.existsById(usuario.getIdUsuario())) {
            return null;
        }

        return mapperUsuario.toUsuario(usuarioGuardado);
    }

    @Override
    public void eliminarPorIdUsuario(String idUsuario) {

        try {
            repository.deleteById(idUsuario);
        }catch (Exception error){
            throw new RuntimeException("No se logro eliminar :(");

        }

    }




}
