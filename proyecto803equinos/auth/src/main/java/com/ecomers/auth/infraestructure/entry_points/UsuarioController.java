package com.ecomers.auth.infraestructure.entry_points;

import com.ecomers.auth.domain.model.Usuario;
import com.ecomers.auth.domain.usecase.UsuarioUseCase;
import com.ecomers.auth.infraestructure.driver_adapters.UsuarioData;
import com.ecomers.auth.infraestructure.mapper.MapperUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecomerce/usuario")
@RequiredArgsConstructor

public class UsuarioController {



    private final UsuarioUseCase usuarioUseCase;
    private final MapperUsuario mapperUsuario;

    @PostMapping("/guardarusuario")
    public ResponseEntity<Usuario> guardarusuario(@RequestBody UsuarioData usuarioData){
        Usuario usuario = mapperUsuario.toUsuario(usuarioData);
        Usuario usuarioValidadoGuardado = usuarioUseCase.guardarusuario(usuario);

        if (usuarioValidadoGuardado.getIdUsuario() != null){

            return new ResponseEntity<>(usuarioValidadoGuardado, HttpStatus.OK);

        }

        return new ResponseEntity<>(usuarioValidadoGuardado, HttpStatus.CONFLICT);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarusuario(@PathVariable String id){
        Usuario usuarioValidadoEncontrado = usuarioUseCase.buscarPorIdUsuario(id);

        if(usuarioValidadoEncontrado.getIdUsuario()!= null){
            return new ResponseEntity<>(usuarioValidadoEncontrado, HttpStatus.OK);
        }

        return new ResponseEntity<>(usuarioValidadoEncontrado, HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPorIdUsuario(@PathVariable String id){

        try {
            usuarioUseCase.eliminarPorIdUsuario(id);
            return ResponseEntity.ok().body("USUARIO Eliminado");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }


    @PutMapping("/update")
    public ResponseEntity<Usuario> actualizarUsuario(@RequestBody UsuarioData usuarioData) {

        if (usuarioData.getIdUsuario() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Usuario usuario = mapperUsuario.toUsuario(usuarioData);
        Usuario usuarioActualizado = usuarioUseCase.actualizarUsuario(usuario);

        if (usuarioActualizado != null && usuarioActualizado.getIdUsuario() != null) {
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
