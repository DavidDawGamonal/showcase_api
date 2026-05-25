package com.EDLM.showcase.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import com.EDLM.showcase.entities.Track;

/**
 * Repositorio JPA para la entidad {@link Track}. Proporciona operaciones CRUD
 * básicas heredadas de {@link JpaRepository} y consultas personalizadas para
 * filtrar tracks.
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface TrackRepository extends JpaRepository<Track, Integer> {

	/**
	 * Busca todos los tracks de un género concreto (insensible a
	 * mayúsculas/minúsculas).
	 *
	 * @param genero El género musical a buscar
	 * @return Lista de tracks que coinciden con el género
	 */
	List<Track> findByGeneroIgnoreCase(String genero);

	/**
	 * Busca todos los tracks subidos por un productor concreto.
	 *
	 * @param idUsuario El ID del productor
	 * @return Lista de tracks del productor
	 */
	List<Track> findByUsuarioIdUsuario(int idUsuario);

	/**
	 * Busca todos los tracks subidos en un rango de fechas.
	 *
	 * @param desde Fecha de inicio del rango
	 * @param hasta Fecha de fin del rango
	 * @return Lista de tracks subidos en ese período
	 */
	List<Track> findByFecSubidaBetween(LocalDateTime desde, LocalDateTime hasta);

	/**
	 * Busca tracks con duración menor a 1 minuto. Categoría: tracks cortos (menos
	 * de 00:01:00).
	 *
	 * @return Lista de tracks con duración menor a 1 minuto
	 */
	@Query("SELECT t FROM Track t WHERE t.duracion < CAST('00:01:00' AS java.time.LocalTime)")
	List<Track> findTracksMenosDeUnMinuto();

	/**
	 * Busca tracks con duración entre 1 y 5 minutos (inclusive). Categoría: tracks
	 * medios (entre 00:01:00 y 00:05:00).
	 *
	 * @return Lista de tracks con duración entre 1 y 5 minutos
	 */
	@Query("SELECT t FROM Track t WHERE t.duracion >= CAST('00:01:00' AS java.time.LocalTime) "
			+ "AND t.duracion <= CAST('00:05:00' AS java.time.LocalTime)")
	List<Track> findTracksEntreUnoYCincoMinutos();

	/**
	 * Busca tracks con duración entre 5 y 10 minutos (exclusive 10). Categoría:
	 * tracks largos (entre 00:05:01 y 00:09:59).
	 *
	 * @return Lista de tracks con duración entre 5 y 10 minutos
	 */
	@Query("SELECT t FROM Track t WHERE t.duracion > CAST('00:05:00' AS java.time.LocalTime) "
			+ "AND t.duracion < CAST('00:10:00' AS java.time.LocalTime)")
	List<Track> findTracksEntreCincoYDiezMinutos();

	/**
	 * Busca tracks con duración de 10 minutos o más. Categoría: tracks muy largos
	 * (00:10:00 en adelante).
	 *
	 * @return Lista de tracks con duración de 10 minutos o más
	 */
	@Query("SELECT t FROM Track t WHERE t.duracion >= CAST('00:10:00' AS java.time.LocalTime)")
	List<Track> findTracksDiezMinutosOMas();

	@Query("SELECT t FROM Track t LEFT JOIN Like l ON l.track = t GROUP BY t ORDER BY COUNT(l) DESC")
	List<Track> findTop5ByLikes(Pageable pageable);

	@Query("SELECT t FROM Track t LEFT JOIN Like l ON l.track = t WHERE t.usuario.idUsuario=:idUsuario GROUP BY t ORDER BY COUNT(l) DESC")
	List<Track> findTop5ByLikesAndUser(@Param("idUsuario") Integer idUsuario, Pageable pageable);

	@Query("SELECT t FROM Track t LEFT JOIN Like l ON l.track = t WHERE t.genero=:genero AND t.usuario.idUsuario=:idUsuario GROUP BY t ORDER BY COUNT(l) DESC")
	List<Track> findTop5ByLikesAndUserAndGenero(@Param("genero") String genero, @Param("idUsuario") Integer idUsuario,
			Pageable pageable);

	@Query("SELECT t FROM Track t LEFT JOIN Like l ON l.track = t WHERE t.genero=:genero AND t.usuario.idUsuario<>:idUsuario GROUP BY t ORDER BY COUNT(l) DESC")
	List<Track> findTop5ByLikesWithoutUserAndGenero(@Param("genero") String genero,
			@Param("idUsuario") Integer idUsuario, Pageable pageable);

	List<Track> findByTituloContainingIgnoreCase(String titulo);

	List<Track> findAllByOrderByFecSubidaDesc(Pageable pageable);// Mapeo en el service (para últimas subidas)

	/**
	 * PENDIENTE — para implementar el filtro de última semana en getUltimasSubidas().
	 * Busca los tracks subidos a partir de una fecha concreta, ordenados por fecha descendente.
	 * Para activarlo: descomentar esta query, descomentar las líneas en getUltimasSubidas()
	 * en TrackService y sustituir findAllByOrderByFecSubidaDesc por este método.
	 *
	 * @param desde     Fecha a partir de la cual buscar (ej: LocalDateTime.now().minusWeeks(1))
	 * @param pageable  Límite de resultados
	 * @return Lista de tracks subidos desde esa fecha
	 */
	// @Query("SELECT t FROM Track t WHERE t.fecSubida >= :desde ORDER BY t.fecSubida DESC")
	// List<Track> findByFecSubidaAfter(@Param("desde") LocalDateTime desde, Pageable pageable);
	
	/**
	 * Cuenta el número de tracks subidos por un productor concreto.
	 *
	 * @param idUsuario El ID del productor
	 * @return Número total de tracks del productor
	 */
	Integer countByUsuarioIdUsuario(Integer idUsuario);
	
	/**
	 * Suma el total de reproducciones de todos los tracks de un productor.
	 * Útil para mostrar las estadísticas globales del productor en su perfil.
	 *
	 * @param idUsuario El ID del productor
	 * @return Número total de reproducciones de todos los tracks del productor
	 */
	@Query("SELECT SUM(t.contReprod) FROM Track t WHERE t.usuario.idUsuario = :idUsuario")
	Integer sumReproduccionesByProductor(@Param("idUsuario") Integer idUsuario);
}
