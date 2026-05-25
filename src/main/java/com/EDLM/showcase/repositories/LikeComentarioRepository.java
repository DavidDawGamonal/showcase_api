package com.EDLM.showcase.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EDLM.showcase.entities.LikeComentario;

/**
 * Repositorio JPA para la entidad {@link LikeComentario}.
 * Proporciona operaciones CRUD básicas heredadas de {@link JpaRepository}
 * y consultas para gestionar los likes de comentarios.
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface LikeComentarioRepository extends JpaRepository<LikeComentario, Integer> {

    /**
     * Comprueba si un usuario ya ha dado like a un comentario concreto.
     *
     * @param idUsuario El ID del usuario
     * @param idComent  El ID del comentario
     * @return true si ya existe el like, false en caso contrario
     */
    boolean existsByUsuarioIdUsuarioAndComentarioIdComent(Integer idUsuario, Integer idComent);

    /**
     * Cuenta el número de likes de un comentario concreto.
     *
     * @param idComent El ID del comentario
     * @return Número total de likes del comentario
     */
    Integer countByComentarioIdComent(Integer idComent);

    /**
     * Elimina el like de un usuario a un comentario concreto.
     *
     * @param idUsuario El ID del usuario
     * @param idComent  El ID del comentario
     */
    void deleteByUsuarioIdUsuarioAndComentarioIdComent(Integer idUsuario, Integer idComent);
    
    /**
     * Devuelve los nombres de los usuarios que han dado like a un comentario.
     *
     * @param idComent El ID del comentario
     * @return Lista de nombres de usuarios
     */
    @Query("SELECT l.usuario.nombre FROM LikeComentario l WHERE l.comentario.idComent = :idComent")
    List<String> findNombresByComentarioIdComent(@Param("idComent") Integer idComent);
}