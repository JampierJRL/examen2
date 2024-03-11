package com.codigo.examen.repository;

import com.codigo.examen.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RolRepository extends JpaRepository<Rol,Long> {
    Set<Rol> findByNombreRol(String nombreRol);
}
