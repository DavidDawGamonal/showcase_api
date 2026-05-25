package com.EDLM.showcase.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EDLM.showcase.DTO.PlaylistData;
import com.EDLM.showcase.services.PlaylistService;

/**
 * Controlador REST que expone los endpoints relacionados con las playlists.
 * Base URL: /playlists
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@CrossOrigin(origins = "*")
@RestController
public class PlaylistController {

    private PlaylistService playlistService;

    /**
     * Constructor con inyección de dependencias del servicio de playlists.
     *
     * @param playlistService Servicio de playlists
     */
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    /**
     * Crea una nueva playlist para un usuario.
     * Devuelve false si ya existe una playlist con ese nombre para ese usuario.
     *
     * @param idUsuario ID del usuario propietario
     * @param nombre    Nombre de la nueva playlist
     * @return true si se creó correctamente, false si ya existe
     */
    @PostMapping("/playlists/crear")
    public boolean crearPlaylist(@RequestParam Integer idUsuario,
                                  @RequestParam String nombre) {
        return playlistService.crearPlaylist(idUsuario, nombre);
    }

    /**
     * Elimina una playlist por su id.
     * Los tracks de la playlist se eliminan en cascada.
     *
     * @param idPlaylist ID de la playlist a eliminar
     */
    @DeleteMapping("/playlists/eliminar")
    public void eliminarPlaylist(@RequestParam Integer idPlaylist) {
        playlistService.eliminarPlaylist(idPlaylist);
    }

    /**
     * Añade un track a una playlist.
     * Devuelve false si el track ya estaba en la playlist.
     *
     * @param idPlaylist ID de la playlist
     * @param idTrack    ID del track a añadir
     * @return true si se añadió correctamente, false si ya estaba
     */
    @PostMapping("/playlists/añadirTrack")
    public boolean añadirTrack(@RequestParam Integer idPlaylist,
                                @RequestParam Integer idTrack) {
        return playlistService.añadirTrack(idPlaylist, idTrack);
    }

    /**
     * Quita un track de una playlist.
     *
     * @param idPlaylist ID de la playlist
     * @param idTrack    ID del track a quitar
     */
    @DeleteMapping("/playlists/quitarTrack")
    public void quitarTrack(@RequestParam Integer idPlaylist,
                             @RequestParam Integer idTrack) {
        playlistService.quitarTrack(idPlaylist, idTrack);
    }

    /**
     * Devuelve todas las playlists de un usuario con sus tracks.
     *
     * @param idUsuario ID del usuario
     * @return Lista de PlaylistData con las playlists del usuario y sus tracks
     */
    @GetMapping("/playlists/byUsuario")
    public List<PlaylistData> getPlaylistsByUsuario(@RequestParam Integer idUsuario) {
        return playlistService.getPlaylistsByUsuario(idUsuario);
    }
}