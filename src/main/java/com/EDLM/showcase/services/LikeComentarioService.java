package com.EDLM.showcase.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EDLM.showcase.entities.Comentario;
import com.EDLM.showcase.entities.LikeComentario;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.repositories.ComentarioRepository;
import com.EDLM.showcase.repositories.LikeComentarioRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los likes de
 * comentarios. Permite dar y quitar likes a comentarios y consultar su estado.
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see LikeComentario
 * @see LikeComentarioRepository
 */

@Service
public class LikeComentarioService {

	private LikeComentarioRepository likeComentarioRepository;
	private UsuarioRepository usuarioRepository;
	private ComentarioRepository comentarioRepository;

	/**
	 * Constructor con inyección de dependencias.
	 *
	 * @param likeComentarioRepository Repositorio de likes de comentarios
	 * @param usuarioRepository        Repositorio de usuarios
	 * @param comentarioRepository     Repositorio de comentarios
	 */
	public LikeComentarioService(LikeComentarioRepository likeComentarioRepository, UsuarioRepository usuarioRepository,
			ComentarioRepository comentarioRepository) {
		this.likeComentarioRepository = likeComentarioRepository;
		this.usuarioRepository = usuarioRepository;
		this.comentarioRepository = comentarioRepository;
	}

	/**
	 * Alterna el like de un usuario sobre un comentario. Si el usuario ya había
	 * dado like lo elimina, si no lo crea.
	 *
	 * @param idUsuario ID del usuario que da o quita el like
	 * @param idComent  ID del comentario sobre el que se actúa
	 * @return true si el like acaba de crearse, false si acaba de eliminarse
	 */
	@Transactional
	public boolean toggleLikeComentario(Integer idUsuario, Integer idComent) {
		if (likeComentarioRepository.existsByUsuarioIdUsuarioAndComentarioIdComent(idUsuario, idComent)) {
			// Ya tiene like — lo eliminamos
			likeComentarioRepository.deleteByUsuarioIdUsuarioAndComentarioIdComent(idUsuario, idComent);
			return false;
		} else {
			// No tiene like — lo creamos
			Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
			Comentario comentario = comentarioRepository.findById(idComent).orElse(null);
			likeComentarioRepository.save(new LikeComentario(usuario, comentario));
			return true;
		}
	}

	/**
	 * Comprueba si un usuario ya ha dado like a un comentario.
	 *
	 * @param idUsuario ID del usuario
	 * @param idComent  ID del comentario
	 * @return true si el usuario ya ha dado like, false en caso contrario
	 */
	public boolean tieneLikeComentario(Integer idUsuario, Integer idComent) {
		return likeComentarioRepository.existsByUsuarioIdUsuarioAndComentarioIdComent(idUsuario, idComent);
	}

	/**
	 * Devuelve el número total de likes de un comentario.
	 *
	 * @param idComent ID del comentario
	 * @return Número total de likes del comentario
	 */
	public Integer contarLikesComentario(Integer idComent) {
		return likeComentarioRepository.countByComentarioIdComent(idComent);
	}

	/**
	 * Devuelve los nombres de los usuarios que han dado like a un comentario.
	 *
	 * @param idComent ID del comentario
	 * @return Lista de nombres de usuarios
	 */
	public List<String> getUsuariosLike(Integer idComent) {
		return likeComentarioRepository.findNombresByComentarioIdComent(idComent);
	}
}