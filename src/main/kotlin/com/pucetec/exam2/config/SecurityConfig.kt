package com.pucetec.exam2.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // Desactivado porque usamos tokens sin sesiones de navegador
            .authorizeHttpRequests { auth ->
                auth
                    // Endpoint PÚBLICO: Cualquier usuario anónimo puede consultar disponibles
                    .requestMatchers(HttpMethod.GET, "/api/parking-spots/available").permitAll()
                    // Endpoints PRIVADOS: Registrar entrada y salida exige token válido de Cognito
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { } // Habilita la validación automática de tokens JWT
            }

        return http.build()
    }
}