package com.ecomers.auth.infraestructure.mapper;

import com.ecomers.auth.domain.model.Usuario;
import com.ecomers.auth.infraestructure.driver_adapters.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class MapperUsuario {

    public Usuario toUsuario(UsuarioData usuarioData) {

        return new Usuario(
                usuarioData.getIdUsuario(),
                usuarioData.getNombre(),
                usuarioData.getCorreo(),
                usuarioData.getClave(),
                usuarioData.getRol(),
                usuarioData.getNumeroTelefonico(),
                usuarioData.getEdad()


        );
    }

    public UsuarioData toData(Usuario usuario){
        
        return  new UsuarioData(
                
                usuario.getIdUsuario(),
                usuario.getClave(),
                usuario.getCorreo(),
                usuario.getNombre(),
                usuario.getRol(),
                usuario.getNumeroTelefonico(),
                usuario.getEdad()
                
                
                
        );
        
        
        
    }
        


    }
