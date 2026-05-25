package com.EDLM.showcase.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.EDLM.showcase.DTO.ComentarioData;
import com.EDLM.showcase.DTO.DataUsuario;
import com.EDLM.showcase.entities.Comentario;
import com.EDLM.showcase.entities.Track;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.repositories.ComentarioRepository;
import com.EDLM.showcase.repositories.TrackRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los comentarios.
 * Permite crear comentarios principales, listarlos por track y contarlos.
 * 
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Service
public class ComentarioService {

	private TrackRepository trackRepository;
	private ComentarioRepository comentarioRepository;
	private UsuarioRepository usuarioRepository;

	/**
	 * Constructor con inyección de dependencias de los tres repositorios
	 * necesarios.
	 *
	 * @param usuarioRepository    Repositorio de usuarios
	 * @param trackRepository      Repositorio de tracks
	 * @param comentarioRepository Repositorio de comentarios
	 */
	public ComentarioService(UsuarioRepository usuarioRepository, TrackRepository trackRepository,
			ComentarioRepository comentarioRepository) {
		this.usuarioRepository = usuarioRepository;
		this.trackRepository = trackRepository;
		this.comentarioRepository = comentarioRepository;
	}

	/**
	 * Crea un comentario principal (sin comentario padre) sobre un track.
	 *
	 * @param idTrack   ID del track sobre el que se comenta
	 * @param idUsuario ID del usuario que realiza el comentario
	 * @param contenido Texto del comentario
	 * @return true si el comentario se ha guardado correctamente
	 */
	public boolean crearComentario(int idTrack, int idUsuario, String contenido) {
		Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
		Track track = trackRepository.findById(idTrack).orElse(null);
		Comentario comentario = new Comentario(usuario, track, contenido);
		comentarioRepository.save(comentario);
		return true;
	}

	/**
	 * Devuelve la lista de comentarios principales de un track con sus respuestas anidadas.
	 * Cada comentario se convierte a ComentarioData para no exponer la entidad directamente.
	 * Solo devuelve comentarios de primer nivel (comentarioPadre == null) — las respuestas
	 * se cargan para cada comentario y se incluyen en el campo respuestas del DTO.
	 * El password del usuario se omite intencionadamente — no tiene sentido exponer el hash.
	 *
	 * @param idTrack ID del track del que se quieren obtener los comentarios
	 * @return Lista de ComentarioData con los comentarios principales y sus respuestas
	 */
	public List<ComentarioData> getComentariosTrack(Integer idTrack) {
	    List<Comentario> comentarios = comentarioRepository.findByTrackIdTrackAndComentarioPadreIsNull(idTrack);
	    List<ComentarioData> resultado = new ArrayList<>();

	    for (Comentario comentario : comentarios) {
	        DataUsuario dataUsuario = new DataUsuario(comentario.getUsuario().getNombre(),
	                comentario.getUsuario().getApellido(), comentario.getUsuario().getEmail(), null,
	                comentario.getUsuario().getRol());

	        ComentarioData comentarioData = new ComentarioData();
	        comentarioData.setIdComentario(comentario.getIdComent());
	        comentarioData.setUsuario(dataUsuario);
	        comentarioData.setContenido(comentario.getContenido());
	        comentarioData.setFecha(java.sql.Timestamp.valueOf(comentario.getFecComent()));

	        // Cargamos las respuestas de este comentario
	        List<Comentario> respuestas = comentarioRepository
	                .findByComentarioPadreIdComentOrderByFecComentAsc(comentario.getIdComent());
	        List<ComentarioData> respuestasData = new ArrayList<>();
	        for (Comentario respuesta : respuestas) {
	            DataUsuario dataUsuarioRespuesta = new DataUsuario(
	                    respuesta.getUsuario().getNombre(),
	                    respuesta.getUsuario().getApellido(),
	                    respuesta.getUsuario().getEmail(),
	                    null,
	                    respuesta.getUsuario().getRol());
	            ComentarioData respuestaData = new ComentarioData();
	            respuestaData.setIdComentario(respuesta.getIdComent());
	            respuestaData.setUsuario(dataUsuarioRespuesta);
	            respuestaData.setContenido(respuesta.getContenido());
	            respuestaData.setFecha(java.sql.Timestamp.valueOf(respuesta.getFecComent()));
	            respuestasData.add(respuestaData);
	        }
	        comentarioData.setRespuestas(respuestasData);
	        resultado.add(comentarioData);
	    }

	    return resultado;
	}

	/**
	 * Devuelve el número de comentarios de un track.
	 *
	 * @param idTrack ID del track
	 * @return Número total de comentarios del track
	 */
	public Integer contarComentarios(Integer idTrack) {
		return comentarioRepository.countByTrackIdTrack(idTrack);
	}

	/**
	 * Devuelve el número total de comentarios recibidos en todos los tracks de un
	 * productor. Se usa para mostrar las estadísticas globales del productor en su
	 * perfil.
	 *
	 * @param idUsuario ID del productor
	 * @return Número total de comentarios recibidos por el productor
	 */
	public Integer contarComentariosByProductor(Integer idUsuario) {
		return comentarioRepository.contarComentariosByProductor(idUsuario);
	}

	/**
	 * Devuelve el número total de comentarios publicados en todos los tracks. Se
	 * usa para mostrar las estadísticas globales del usuario en su perfil.
	 * 
	 * @param idUsuario ID del usuario
	 * @return Número total de comentarios publicados por el usuario
	 */
	public Integer contarComentariosByUsuario(Integer idUsuario) {
		return comentarioRepository.countByUsuarioIdUsuario(idUsuario);
	}
	
	/**
	 * Devuelve los comentarios escritos por un usuario ordenados del más reciente al más antiguo.
	 * Se usa para mostrar la sección "Mis comentarios" en el perfil del usuario.
	 * Convierte cada Comentario a ComentarioData para no exponer la entidad directamente.
	 * Incluye el título del track para poder mostrarlo junto al comentario en el perfil.
	 * El password del usuario se omite intencionadamente — no tiene sentido exponer el hash.
	 *
	 * @param idUsuario ID del usuario
	 * @return Lista de ComentarioData con los comentarios escritos por el usuario
	 */
	public List<ComentarioData> getComentariosByUsuario(Integer idUsuario) {
	    List<Comentario> comentarios = comentarioRepository.findByUsuarioIdUsuarioOrderByFecComentDesc(idUsuario);
	    List<ComentarioData> resultado = new ArrayList<>();
	    for (Comentario comentario : comentarios) {
	        DataUsuario dataUsuario = new DataUsuario(
	                comentario.getUsuario().getNombre(),
	                comentario.getUsuario().getApellido(),
	                comentario.getUsuario().getEmail(),
	                null, // password = null, no tiene sentido exponer el hash
	                comentario.getUsuario().getRol());
	        ComentarioData comentarioData = new ComentarioData();
	        comentarioData.setIdComentario(comentario.getIdComent());
	        comentarioData.setUsuario(dataUsuario);
	        comentarioData.setContenido(comentario.getContenido());
	        comentarioData.setFecha(java.sql.Timestamp.valueOf(comentario.getFecComent()));
	        // Añadimos el título del track para mostrarlo en el perfil del usuario
	        comentarioData.setTituloTrack(comentario.getTrack().getTitulo());
	        resultado.add(comentarioData);
	    }
	    return resultado;
	}
	
	/**
	 * Crea una respuesta a un comentario existente.
	 * Busca el comentario padre y lo asigna al nuevo comentario.
	 *
	 * @param idComentPadre ID del comentario al que se responde
	 * @param idUsuario     ID del usuario que responde
	 * @param contenido     Texto de la respuesta
	 * @return true si la respuesta se ha guardado correctamente
	 */
	public boolean responderComentario(int idComentPadre, int idUsuario, String contenido) {
	    Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
	    Comentario padre = comentarioRepository.findById(idComentPadre).orElse(null);
	    if (padre == null) return false;
	    Comentario respuesta = new Comentario(usuario, padre.getTrack(), contenido);
	    respuesta.setComentarioPadre(padre);
	    comentarioRepository.save(respuesta);
	    return true;
	}

}