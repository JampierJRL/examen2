package com.codigo.examen.request;

import com.codigo.examen.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class SignUpRequest {
    private Long   idUsuario;
    private String username;
    private String email;
    private String password;
    private String telefono;
    Set<Rol> roles;

    public SignUpRequest() {

    }
}
