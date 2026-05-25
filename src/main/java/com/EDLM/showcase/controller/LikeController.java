package com.EDLM.showcase.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.EDLM.showcase.DTO.FavoritoData;
import com.EDLM.showcase.services.LikeService;

/**
 * Controlador REST que expone los endpoints relacionados con los likes. Base
 * URL: /likes
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@CrossOrigin(origins = "*")
@RestController
public class LikeController {

	private LikeService likeService;

	/**
	 * Constructor con inyección de dependencias del servicio de likes.
	 *
	 * @param likeService Servicio de likes
	 */
	public LikeController(LikeService likeService) {
		this.likeService = likeService;
	}

	/**
	 * Alterna el like de un usuario sobre un track. Si el usuario ya había dado
	 * like lo elimina, si no lo crea.
	 *
	 * @param idUsuario ID del usuario que da o quita el like
	 * @param idTrack   ID del track sobre el que se actúa
	 * @return true si el like acaba de crearse, false si acaba de eliminarse
	 */
	@PostMapping("/likes/toggleLike")
	public boolean toggleLike(@RequestParam Integer idUsuario, @RequestParam Integer idTrack) {
		System.out.println("userId recibido: " + idUsuario);
		return likeService.toggleLike(idUsuario, idTrack);
	}

	/**
	 * Comprueba si un usuario ya ha dado like a un track.
	 *
	 * @param idUsuario ID del usuario
	 * @param idTrack   ID del track
	 * @return true si el usuario ya ha dado like, false en caso contrario
	 */
	@GetMapping("/likes/tieneLike")
	public boolean tieneLike(@RequestParam Integer idUsuario, @RequestParam Integer idTrack) {
		return likeService.tieneLike(idUsuario, idTrack);
	}
	
	/**
	 * Devuelve el número total de likes de un track.
	 *
	 * @param idTrack ID del track
	 * @return Número total de likes del track
	 */
	@GetMapping("/likes/contarLikes")
	public Integer contarLikes(@RequestParam Integer idTrack) {
		return likeService.contarLikes(idTrack);
	}
	
	/**
	 * Devuelve el número total de likes recibidos en todos los tracks de un productor.
	 *
	 * @param idUsuario ID del productor
	 * @return Número total de likes recibidos por el productor
	 */
	@GetMapping("/likes/contarLikesByProductor")
	public Integer contarLikesByProductor(@RequestParam Integer idUsuario) {
	    return likeService.contarLikesByProductor(idUsuario);
	}
	
	/**
	 * Devuelve el número total de likes publicados en todos los tracks escuchados por un usuario.
	 *
	 * @param idUsuario ID del usuario
	 * @return Número total de likes publicados por el usuario
	 */
	@GetMapping("/likes/contarLikesDados")
	public Integer contarLikesDados (@RequestParam Integer idUsuario) {
		return likeService.contarLikesByUsuario(idUsuario);
	}
	
//	/**
//	 * Devuelve los tracks a los que un usuario ha dado like, ordenados del más reciente al más antiguo.
//	 * Devuelve FavoritoData en lugar de TrackResumen para incluir la fecha del like
//	 * y seguir el patrón DTO del proyecto.
//	 *
//	 * @param idUsuario ID del usuario
//	 * @return Lista de FavoritoData con los tracks favoritos del usuario
//	 */
//	@GetMapping("/likes/favoritos")
//	public List<FavoritoData> getFavoritosByUsuario(@RequestParam Integer idUsuario) {
//	    return likeService.getFavoritosByUsuario(idUsuario);
//	}
	
	

}