package com.EDLM.showcase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EDLM.showcase.entities.Descarga;

/**
 * Repositorio JPA para la entidad {@link Descarga}.
 * Proporciona operaciones CRUD básicas heredadas de {@link JpaRepository}
 * y consultas para gestionar las descargas de tracks por usuario.
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface DescargaRepository extends JpaRepository<Descarga, Integer> {

    /**
     * Cuenta el número de descargas realizadas por un usuario concreto.
     * Útil para mostrar las estadísticas del usuario en su perfil.
     *
     * @param idUsuario El ID del usuario
     * @return Número total de descargas realizadas por el usuario
     */
    Integer countByUsuarioIdUsuario(Integer idUsuario);

    /**
     * Cuenta el número de descargas de un track concreto.
     * Útil para mostrar la popularidad de un track.
     *
     * @param idTrack El ID del track
     * @return Número total de descargas del track
     */
    Integer countByTrackIdTrack(Integer idTrack);

    /**
     * Obtiene todas las descargas realizadas por un usuario concreto.
     * Útil para mostrar el historial de descargas en el perfil del usuario.
     *
     * @param idUsuario El ID del usuario
     * @return Lista de descargas del usuario ordenadas por fecha descendente
     */
    List<Descarga> findByUsuarioIdUsuarioOrderByFecDescargaDesc(Integer idUsuario);
}