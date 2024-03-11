package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.request.SignUpRequest;
import com.codigo.examen.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;


    @Override
    public ResponseEntity<Usuario> createUsuario(SignUpRequest usuario) {
        Optional<Usuario> existingUser = usuarioRepository.findByUsername(usuario.getUsername());
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        return getUsuarioResponseEntity(usuario);
    }

    @Override
    public ResponseEntity<Usuario> getUsuarioById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Usuario> updateUsuario(Long id, SignUpRequest usuario) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);
        if (existingUsuario.isPresent()) {
            usuario.setIdUsuario(id);

            if (!usuario.getUsername().equals(existingUsuario.get().getUsername())) {
                Optional<Usuario> userWithNewUsername = usuarioRepository.findByUsername(usuario.getUsername());
                if (userWithNewUsername.isPresent()) {
                    return ResponseEntity.badRequest().body(null);
                }
            }
            return getUsuarioResponseEntity(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<Usuario> getUsuarioResponseEntity(SignUpRequest signUpRequest) {
        Set<Rol> assignedRoles = new HashSet<>();
        Usuario usuario = new Usuario();
        for (Rol roles : signUpRequest.getRoles()) {
            Optional<Rol> rol = rolRepository.findById(roles.getIdRol());
            if (!rol.isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            //rol.ifPresent(assignedRoles::add);
            assignedRoles.add(rol.get());
        }

        if(signUpRequest.getPassword() != null) {
            usuario.setPassword(new BCryptPasswordEncoder()
                .encode(signUpRequest.getPassword()));
        }

        signUpRequest.setRoles(assignedRoles);


        usuario.setUsername(signUpRequest.getUsername());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setTelefono(signUpRequest.getTelefono());
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(updatedUsuario);
    }

    @Override
    public ResponseEntity<Usuario> deleteUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuario.get().setAccountnonexpire(false);
            usuario.get().setAccountnonlocked(false);
            usuario.get().setCredentialsnonexpired(false);
            usuarioRepository.delete(usuario.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }






}
