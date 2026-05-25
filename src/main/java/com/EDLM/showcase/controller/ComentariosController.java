package com.EDLM.showcase.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.EDLM.showcase.DTO.ComentarioData;
import com.EDLM.showcase.services.ComentarioService;

/**
 * Controlador REST que expone los endpoints relacionados con los comentarios.
 * Base URL: /comentarios
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@CrossOrigin(origins = "*")
@RestController
public class ComentariosController {

    private ComentarioService comentarioService;

    /**
     * Constructor con inyección de dependencias del servicio de comentarios.
     *
     * @param comentarioService Servicio de comentarios
     */
    public ComentariosController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    /**
     * Devuelve los comentarios principales de un track (sin respuestas anidadas).
     *
     * @param idTrack ID del track del que se quieren obtener los comentarios
     * @return Lista de ComentarioData con los comentarios principales del track
     */
    @GetMapping("/comentarios/getComentariosTrack")
    public List<ComentarioData> getComentariosTrack(@RequestParam Integer idTrack) {
        return comentarioService.getComentariosTrack(idTrack);
    }

    /**
     * Devuelve el número total de comentarios de un track.
     *
     * @param idTrack ID del track
     * @return Número total de comentarios del track
     */
    @GetMapping("/comentarios/contarComentarios")
    public Integer contarComentarios(@RequestParam Integer idTrack) {
        return comentarioService.contarComentarios(idTrack);
    }

    /**
     * Crea un comentario principal sobre un track.
     *
     * @param idTrack   ID del track sobre el que se comenta
     * @param idUsuario ID del usuario que realiza el comentario
     * @param contenido Texto del comentario
     * @return true si el comentario se ha creado correctamente
     */
    @PostMapping("/comentarios/crear")
    public boolean crearComentario(@RequestParam Integer idTrack,
                                    @RequestParam Integer idUsuario,
                                    @RequestParam String contenido) {
        return comentarioService.crearComentario(idTrack, idUsuario, contenido);
    }
    
    /**
     * Devuelve el número total de comentarios recibidos en todos los tracks de un productor.
     * Se usa para mostrar las estadísticas globales del productor en su perfil.
     *
     * @param idUsuario ID del productor
     * @return Número total de comentarios recibidos por el productor
     */
    @GetMapping("/comentarios/contarComentariosByProductor")
    public Integer contarComentariosByProductor(@RequestParam Integer idUsuario) {
        return comentarioService.contarComentariosByProductor(idUsuario);
    }
    
    /**
     * Devuelve el número total de comentarios publicados por un usuario en los tracks.
     * Se usa para mostrar las estadísticas globales del usuario en su perfil.
     *
     * @param idUsuario ID del usuario
     * @return Número total de comentarios publicados por el usuario
     */
    @GetMapping("/comentarios/contarComentariosByUsuario")
    public Integer contarComentariosByUsuario(@RequestParam Integer idUsuario) {
        return comentarioService.contarComentariosByUsuario(idUsuario);
    }
    
    /**
     * Devuelve los comentarios escritos por un usuario ordenados del más reciente al más antiguo.
     * Se usa para mostrar la sección "Mis comentarios" en el perfil del usuario.
     *
     * @param idUsuario ID del usuario
     * @return Lista de ComentarioData con los comentarios escritos por el usuario
     */
    @GetMapping("/comentarios/byUsuario")
    public List<ComentarioData> getComentariosByUsuario(@RequestParam Integer idUsuario) {
        return comentarioService.getComentariosByUsuario(idUsuario);
    }
    
    /**
     * Crea una respuesta a un comentario existente.
     * Solo puede responder a comentarios de primer nivel — no se permiten
     * respuestas a respuestas (máximo 1 nivel de anidamiento).
     *
     * @param idComentPadre ID del comentario al que se responde
     * @param idUsuario     ID del usuario que responde
     * @param contenido     Texto de la respuesta
     * @return true si la respuesta se ha guardado correctamente, false si el comentario padre no existe
     */
    @PostMapping("/comentarios/responder")
    public boolean responderComentario(
            @RequestParam int idComentPadre,
            @RequestParam int idUsuario,
            @RequestParam String contenido) {
        return comentarioService.responderComentario(idComentPadre, idUsuario, contenido);
    }
}