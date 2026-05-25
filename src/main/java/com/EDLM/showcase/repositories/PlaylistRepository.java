package com.EDLM.showcase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDLM.showcase.entities.Playlist;

/**
 * Repositorio JPA para la entidad {@link Playlist}.
 * Proporciona operaciones CRUD básicas heredadas de {@link JpaRepository}
 * y consultas para obtener playlists por usuario.
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

    /**
     * Obtiene todas las playlists de un usuario concreto.
     *
     * @param idUsuario El ID del usuario
     * @return Lista de playlists del usuario
     */
    List<Playlist> findByUsuarioIdUsuario(int idUsuario);

    /**
     * Comprueba si un usuario ya tiene una playlist con un nombre concreto.
     * Útil para validar la restricción unique_nombre_usuario antes de persistir.
     *
     * @param nombre    El nombre de la playlist
     * @param idUsuario El ID del usuario
     * @return true si ya existe, false en caso contrario
     */
    boolean existsByNombreAndUsuarioIdUsuario(String nombre, int idUsuario);
}
