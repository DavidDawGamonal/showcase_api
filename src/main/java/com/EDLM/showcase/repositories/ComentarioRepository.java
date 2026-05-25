package com.EDLM.showcase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EDLM.showcase.entities.Comentario;
import com.EDLM.showcase.entities.Like;

/**
 * Repositorio JPA para la entidad {@link Comentario}.
 * Proporciona operaciones CRUD básicas heredadas de {@link JpaRepository}
 * y consultas para obtener comentarios por track o por usuario.
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    /**
     * Obtiene todos los comentarios principales (sin padre) de un track concreto.
     * Útil para mostrar el hilo de comentarios de un track.
     *
     * @param idTrack El ID del track
     * @return Lista de comentarios principales del track
     */
    List<Comentario> findByTrackIdTrackAndComentarioPadreIsNull(Integer idTrack);

    /**
     * Obtiene todos los comentarios realizados por un usuario concreto.
     *
     * @param idUsuario El ID del usuario
     * @return Lista de comentarios del usuario
     */
    List<Comentario> findByUsuarioIdUsuario(int idUsuario);
    
    /**
     * Obtiene todos los comentarios de un track concreto.
     * Útil para mostrar cuántos usuarios han comentado un track.
     *
     * @param idTrack El ID del track
     * @return Lista de comentarios del track
     */
    Integer countByTrackIdTrack(Integer idTrack);// Agregado nuevo para obtener el número de comentarios
    
    /**
     * Cuenta el total de comentarios recibidos en todos los tracks de un productor.
     * Útil para mostrar las estadísticas globales del productor en su perfil.
     *
     * @param idUsuario El ID del productor
     * @return Número total de comentarios recibidos por el productor
     */
    @Query("SELECT COUNT(c) FROM Comentario c WHERE c.track.usuario.idUsuario = :idUsuario")
    Integer contarComentariosByProductor(@Param("idUsuario") Integer idUsuario);
    
    /**
     * Cuenta el número de comentarios escritos por un usuario concreto.
     * Útil para mostrar las estadísticas de actividad del usuario en su perfil.
     *
     * @param idUsuario El ID del usuario
     * @return Número total de comentarios escritos por el usuario
     */
    Integer countByUsuarioIdUsuario(Integer idUsuario);
    
    /**
     * Obtiene todos los comentarios escritos por un usuario concreto ordenados por fecha descendente.
     * Útil para mostrar el historial de comentarios del usuario en su perfil.
     *
     * @param idUsuario El ID del usuario
     * @return Lista de comentarios del usuario ordenados del más reciente al más antiguo
     */
    List<Comentario> findByUsuarioIdUsuarioOrderByFecComentDesc(Integer idUsuario);
    
    /**
     * Obtiene todas las respuestas directas a un comentario concreto,
     * ordenadas por fecha ascendente para mostrarlas en orden cronológico.
     *
     * @param idComent El ID del comentario padre
     * @return Lista de respuestas al comentario
     */
    List<Comentario> findByComentarioPadreIdComentOrderByFecComentAsc(Integer idComent);
}
