package com.EDLM.showcase.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDLM.showcase.entities.Usuario;

/**
 * Repositorio JPA para la entidad {@link Usuario}.
 * Proporciona operaciones CRUD básicas heredadas de {@link JpaRepository}
 * y consultas personalizadas para la autenticación y búsqueda de usuarios.
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Búsqueda combinada por email y password — sustituida por findByEmail + BCrypt en el service
    // Optional<Usuario> findByEmailAndPassword(String email, String password);

    /**
     * Busca un usuario por su email.
     * Se usa en el login para obtener el usuario y comparar
     * la contraseña introducida con el hash BCrypt almacenado.
     *
     * @param email El email del usuario a buscar
     * @return El usuario si existe, vacío si no
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca un usuario por su id.
     * Sobreescribe el findById heredado de JpaRepository
     * para aceptar int en lugar de Integer.
     *
     * @param idUsuario El id del usuario a buscar
     * @return El usuario si existe, vacío si no
     */
    Optional<Usuario> findById(int idUsuario);
}