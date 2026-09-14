package com.ecomers.auth.application;


import com.ecomers.auth.domain.model.gateway.UsuarioGateway;
import com.ecomers.auth.domain.usecase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class UsuarioConfig {

    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway){

        return new UsuarioUseCase(usuarioGateway);
    }

}
