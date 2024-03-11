package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.request.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static javax.security.auth.callback.ConfirmationCallback.OK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testCreateUsuarioUsernameExists(){
        // Configuro el usuario existente
        SignUpRequest usuarioRequest = new SignUpRequest();
        usuarioRequest.setUsername("examplo");

        // Válido los roles validos para objeto usuarioRequest
        Set<Rol> roles = new HashSet<>();
        // Agrego los roles
        roles.add(new Rol());
        usuarioRequest.setRoles(roles);

        // Simulo el comportamiento del usuarioRepository
        Optional<Usuario> existingUser = Optional.of(new Usuario());
        when(usuarioRepository.findByUsername(usuarioRequest.getUsername())).thenReturn(existingUser);

        // Ejecuto el método que estoy probando
        ResponseEntity<Usuario> response = usuarioService.createUsuario(usuarioRequest);

        //Veo si se devuelve un ResponseEntity con estado BadRequest 400
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateUsuario_NewUser() {

            // Simulo mis datos de mi usuarioRequest
            SignUpRequest usuarioRequest = new SignUpRequest();
            usuarioRequest.setUsername("nuevoUsuario");
            usuarioRequest.setEmail("usuario@example.com");
            usuarioRequest.setPassword("contraseña");

            // Simulo crear un rol
            Set<Rol> roles = new HashSet<>();
            roles.add(new Rol("ROLE_USER"));
            usuarioRequest.setRoles(roles);

            // Simulo que el usuario no existe en la base de datos
            when(usuarioRepository.findByUsername(usuarioRequest.getUsername())).thenReturn(Optional.empty());

            // Simulo que la operación de guardado en el repositorio devuelve un usuario nulo
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(null);

            // Ejejcuto el metodo que pruebo
            ResponseEntity<Usuario> response = usuarioService.createUsuario(usuarioRequest);
            assertEquals(BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
    }

    @Test
    void getUsuarioById_ExistingId_ReturnsUsuario() {
        // Inicio mi ID
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> response = usuarioService.getUsuarioById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void getUsuarioById_NonExistingId_ReturnsNotFound() {
        // Inicializo mi ID
        Long id = 2L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        //when(usuarioRepository.findById(id)))
        ResponseEntity<Usuario> response = usuarioService.getUsuarioById(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateUsuario_NonExistingId_ReturnsNotFound() {
        // Inicio mi ID
        Long id = 2L;
        SignUpRequest usuarioRequest = new SignUpRequest();

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioService.updateUsuario(id, usuarioRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    void updateUsuario_ExistingUser_ReturnsOk() {
        // Inicio mi ID
        Long id = 1L;
        SignUpRequest usuarioRequest = new SignUpRequest();
        usuarioRequest.setUsername("newUsername");
        usuarioRequest.setRoles(Collections.emptySet()); // Mis coles no sean nulos

        Usuario existingUsuario = new Usuario();
        existingUsuario.setIdUsuario(id);
        existingUsuario.setUsername("existingUsername");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(existingUsuario));
        when(usuarioRepository.findByUsername(usuarioRequest.getUsername())).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioService.updateUsuario(id, usuarioRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}