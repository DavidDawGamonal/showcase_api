package com.EDLM.showcase.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.EDLM.showcase.DTO.FavoritoData;
import com.EDLM.showcase.entities.Like;
import com.EDLM.showcase.entities.Track;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.repositories.LikeRepository;
import com.EDLM.showcase.repositories.TrackRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los likes. Permite
 * alternar el like de un usuario sobre un track, comprobar si ya lo ha dado y
 * contar el total de likes de un track. Solo los usuarios registrados pueden
 * dar like.
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see Like
 * @see LikeRepository
 */
@Service
public class LikeService {

	private LikeRepository likeRepository;
	private TrackRepository trackRepository;
	private UsuarioRepository usuarioRepository;

	/**
	 * Constructor con inyección de dependencias de los tres repositorios
	 * necesarios.
	 *
	 * @param likeRepository    Repositorio de likes
	 * @param trackRepository   Repositorio de tracks
	 * @param usuarioRepository Repositorio de usuarios
	 */
	public LikeService(LikeRepository likeRepository, TrackRepository trackRepository,
			UsuarioRepository usuarioRepository) {
		this.likeRepository = likeRepository;
		this.trackRepository = trackRepository;
		this.usuarioRepository = usuarioRepository;
	}

	/**
	 * Alterna el like de un usuario sobre un track. Si el usuario ya había dado
	 * like lo elimina, si no lo crea. La unicidad usuario+track está garantizada
	 * por la restricción de la BD.
	 *
	 * @param idUsuario ID del usuario que da o quita el like
	 * @param idTrack   ID del track sobre el que se actúa
	 * @return true si el like acaba de crearse, false si acaba de eliminarse
	 */
	public boolean toggleLike(Integer idUsuario, Integer idTrack) {
		System.out.println("idUsuario recibido: " + idUsuario);
		System.out.println("idTrack recibido: " + idTrack);
		boolean tieneLike = false;
		Optional<Like> like = likeRepository.findByUsuarioIdUsuarioAndTrackIdTrack(idUsuario, idTrack);
		if (like.isPresent()) {
			likeRepository.delete(like.get());
			tieneLike = false;
		} else {
			Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
			Track track = trackRepository.findById(idTrack).orElse(null);
			likeRepository.save(new Like(usuario, track));
			tieneLike = true;
		}
		return tieneLike;
	}

	/**
	 * Comprueba si un usuario ya ha dado like a un track. Se usa al cargar el
	 * reproductor para pintar el corazón relleno o vacío.
	 *
	 * @param idUsuario ID del usuario
	 * @param idTrack   ID del track
	 * @return true si el usuario ya ha dado like, false en caso contrario
	 */
	public boolean tieneLike(Integer idUsuario, Integer idTrack) {
		return likeRepository.existsByUsuarioIdUsuarioAndTrackIdTrack(idUsuario, idTrack);
	}

	/**
	 * Devuelve el número total de likes de un track. Se usa al cargar el
	 * reproductor para mostrar el contador real.
	 *
	 * @param idTrack ID del track
	 * @return Número total de likes del track
	 */
	public Integer contarLikes(Integer idTrack) {
		return likeRepository.countByTrackIdTrack(idTrack);
	}

	/**
	 * Devuelve el número total de likes recibidos en todos los tracks de un
	 * productor. Se usa para mostrar las estadísticas globales del productor en su
	 * perfil.
	 *
	 * @param idUsuario ID del productor
	 * @return Número total de likes recibidos por el productor
	 */
	public Integer contarLikesByProductor(Integer idUsuario) {
		return likeRepository.countByTrackUsuarioIdUsuario(idUsuario);
	}

	/**
	 * Devuelve el número total de likes publicados en todos los tracks que ha
	 * escuchado un usuario. Se usa para mostrar las estadísticas globales del
	 * usuario en su perfil.
	 *
	 * @param idUsuario ID del usuario
	 * @return Número total de likes publicados por el usuario
	 */
	public Integer contarLikesByUsuario(Integer idUsuario) {
		return likeRepository.countByUsuarioIdUsuario(idUsuario);
	}

//    /**
//     * Devuelve los tracks a los que un usuario ha dado like, ordenados del más reciente al más antiguo.
//     * Se usa para mostrar la sección "Mis favoritos" en el perfil del usuario.
//     * Devuelve FavoritoData en lugar de TrackResumen para incluir la fecha del like
//     * y seguir el patrón DTO del proyecto — no exponer entidades directamente al frontend.
//     * Incluye el número real de likes de cada track consultando el repositorio por cada uno.
//     *
//     * @param idUsuario ID del usuario
//     * @return Lista de FavoritoData con los tracks favoritos del usuario
//     */
//    public List<FavoritoData> getFavoritosByUsuario(Integer idUsuario) {
//        List<Like> likes = likeRepository.findByUsuarioIdUsuarioOrderByFecLikeDesc(idUsuario);
//        List<FavoritoData> resultado = new ArrayList<>();
//        for (Like like : likes) {
//            Track track = like.getTrack();
//            Integer totalLikes = likeRepository.countByTrackIdTrack(track.getIdTrack());
//            // Sanitizamos la portada igual que en TrackResumen — '@' como separador de URL
//            String portada = track.getPortada() != null
//                    ? track.getPortada().replace(File.separatorChar, '@')
//                    : "portadas/portada_default.jpg";
//            // Formateamos la duración igual que en TrackResumen
//            String duracion = track.getDuracion().format(
//                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
//
//            resultado.add(new FavoritoData(
//                    track.getIdTrack(),
//                    track.getTitulo(),
//                    track.getUsuario().getNombre(),
//                    portada,
//                    duracion,
//                    totalLikes,
//                    like.getFecLike()
//            ));
//        }
//        return resultado;
//    }
}