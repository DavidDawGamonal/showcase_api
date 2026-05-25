package com.EDLM.showcase.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EDLM.showcase.services.LikeComentarioService;

/**
 * Controlador REST que expone los endpoints relacionados con los likes de comentarios.
 * Base URL: /likesComentarios
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@CrossOrigin(origins = "*")
@RestController
public class LikeComentarioController {

    private LikeComentarioService likeComentarioService;

    /**
     * Constructor con inyección de dependencias del servicio de likes de comentarios.
     *
     * @param likeComentarioService Servicio de likes de comentarios
     */
    public LikeComentarioController(LikeComentarioService likeComentarioService) {
        this.likeComentarioService = likeComentarioService;
    }

    /**
     * Alterna el like de un usuario sobre un comentario.
     * Si el usuario ya había dado like lo elimina, si no lo crea.
     *
     * @param idUsuario ID del usuario que da o quita el like
     * @param idComent  ID del comentario sobre el que se actúa
     * @return true si el like acaba de crearse, false si acaba de eliminarse
     */
    @PostMapping("/likesComentarios/toggleLike")
    public boolean toggleLikeComentario(
            @RequestParam Integer idUsuario,
            @RequestParam Integer idComent) {
        return likeComentarioService.toggleLikeComentario(idUsuario, idComent);
    }

    /**
     * Comprueba si un usuario ya ha dado like a un comentario.
     *
     * @param idUsuario ID del usuario
     * @param idComent  ID del comentario
     * @return true si el usuario ya ha dado like, false en caso contrario
     */
    @GetMapping("/likesComentarios/tieneLike")
    public boolean tieneLikeComentario(
            @RequestParam Integer idUsuario,
            @RequestParam Integer idComent) {
        return likeComentarioService.tieneLikeComentario(idUsuario, idComent);
    }

    /**
     * Devuelve el número total de likes de un comentario.
     *
     * @param idComent ID del comentario
     * @return Número total de likes del comentario
     */
    @GetMapping("/likesComentarios/contarLikes")
    public Integer contarLikesComentario(@RequestParam Integer idComent) {
        return likeComentarioService.contarLikesComentario(idComent);
    }
    
    /**
     * Devuelve los nombres de los usuarios que han dado like a un comentario.
     *
     * @param idComent ID del comentario
     * @return Lista de nombres de usuarios
     */
    @GetMapping("/likesComentarios/usuarios")
    public List<String> getUsuariosLike(@RequestParam Integer idComent) {
        return likeComentarioService.getUsuariosLike(idComent);
    }
}