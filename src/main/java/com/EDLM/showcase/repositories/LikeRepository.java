package com.EDLM.showcase.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EDLM.showcase.entities.Like;

/**
 * Repositorio JPA para la entidad {@link Like}. Proporciona operaciones CRUD
 * básicas heredadas de {@link JpaRepository} y consultas para gestionar los
 * likes de tracks y usuarios.
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

	/**
	 * Obtiene todos los likes de un track concreto. Útil para mostrar cuántos
	 * usuarios han dado like a un track.
	 *
	 * @param idTrack El ID del track
	 * @return Lista de likes del track
	 */
	List<Like> findByTrackIdTrack(Integer idTrack);

	/**
	 * Cuenta el número de likes de un track concreto.
	 *
	 * @param idTrack El ID del track
	 * @return Número total de likes del track
	 */
	Integer countByTrackIdTrack(Integer idTrack);

	/**
	 * Busca el like de un usuario concreto sobre un track concreto. Útil para
	 * comprobar si un usuario ya ha dado like a un track.
	 *
	 * @param idUsuario El ID del usuario
	 * @param idTrack   El ID del track
	 * @return El like si existe, vacío si no
	 */
	Optional<Like> findByUsuarioIdUsuarioAndTrackIdTrack(Integer idUsuario, Integer idTrack);

	/**
	 * Comprueba si un usuario ya ha dado like a un track.
	 *
	 * @param idUsuario El ID del usuario
	 * @param idTrack   El ID del track
	 * @return true si ya existe el like, false en caso contrario
	 */
	boolean existsByUsuarioIdUsuarioAndTrackIdTrack(Integer idUsuario, Integer idTrack);

	/**
	 * Cuenta el total de likes recibidos en todos los tracks de un productor. Útil
	 * para mostrar las estadísticas globales del productor en su perfil.
	 *
	 * @param idUsuario El ID del productor
	 * @return Número total de likes recibidos por el productor
	 */
//	@Query("SELECT COUNT(l) FROM Like l WHERE l.track.usuario.idUsuario = :idUsuario")
//	Integer countLikesByProductor(@Param("idUsuario") Integer idUsuario);//countByTrackUsuarioIdUsuario (cambiar el nombre)
	
	Integer countByTrackUsuarioIdUsuario(Integer idUsuario);

	
	/*mirar juntar ambas funciones*/
	/**
	 * Cuenta el número de likes dados por un usuario concreto. Útil para mostrar
	 * las estadísticas de actividad del usuario en su perfil.
	 *
	 * @param idUsuario El ID del usuario
	 * @return Número total de likes dados por el usuario
	 */
	Integer countByUsuarioIdUsuario(Integer idUsuario);
	
	/**
	 * Obtiene todos los likes dados por un usuario concreto ordenados por fecha descendente.
	 * Útil para mostrar el historial de tracks favoritos del usuario en su perfil.
	 *
	 * @param idUsuario El ID del usuario
	 * @return Lista de likes del usuario ordenados del más reciente al más antiguo
	 */
	List<Like> findByUsuarioIdUsuarioOrderByFecLikeDesc(Integer idUsuario);
}
