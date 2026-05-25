package com.EDLM.showcase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDLM.showcase.entities.PlaylistTrack;
import com.EDLM.showcase.entities.PlaylistTrackId;

/**
 * Repositorio JPA para la entidad {@link PlaylistTrack}.
 * Gestiona la relación many-to-many entre playlists y tracks.
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack, PlaylistTrackId> {

    /**
     * Obtiene todos los tracks de una playlist concreta ordenados por fecha de añadido descendente.
     *
     * @param idPlaylist El ID de la playlist
     * @return Lista de PlaylistTrack de la playlist
     */
    List<PlaylistTrack> findByIdIdPlaylistOrderByFecAñadidoDesc(Integer idPlaylist);

    /**
     * Comprueba si un track ya está en una playlist concreta.
     *
     * @param idPlaylist El ID de la playlist
     * @param idTrack    El ID del track
     * @return true si el track ya está en la playlist, false en caso contrario
     */
    boolean existsByIdIdPlaylistAndIdIdTrack(Integer idPlaylist, Integer idTrack);

    /**
     * Elimina un track de una playlist concreta.
     *
     * @param idPlaylist El ID de la playlist
     * @param idTrack    El ID del track
     */
    void deleteByIdIdPlaylistAndIdIdTrack(Integer idPlaylist, Integer idTrack);
}