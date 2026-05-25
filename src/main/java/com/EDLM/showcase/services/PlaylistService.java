package com.EDLM.showcase.services;


import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.EDLM.showcase.DTO.PlaylistData;
import com.EDLM.showcase.DTO.TrackResumen;
import com.EDLM.showcase.entities.Playlist;
import com.EDLM.showcase.entities.PlaylistTrack;
import com.EDLM.showcase.entities.Track;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.repositories.LikeRepository;
import com.EDLM.showcase.repositories.ComentarioRepository;
import com.EDLM.showcase.repositories.PlaylistRepository;
import com.EDLM.showcase.repositories.PlaylistTrackRepository;
import com.EDLM.showcase.repositories.TrackRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;

/**
 * Servicio que gestiona la lógica de negocio relacionada con las playlists.
 * Permite crear y eliminar playlists, añadir y quitar tracks de ellas,
 * y obtener las playlists de un usuario con sus tracks.
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Service
public class PlaylistService {

    private PlaylistRepository playlistRepository;
    private PlaylistTrackRepository playlistTrackRepository;
    private TrackRepository trackRepository;
    private UsuarioRepository usuarioRepository;
    private LikeRepository likeRepository;
    private ComentarioRepository comentarioRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param playlistRepository      Repositorio de playlists
     * @param playlistTrackRepository Repositorio de relaciones playlist-track
     * @param trackRepository         Repositorio de tracks
     * @param usuarioRepository       Repositorio de usuarios
     * @param likeRepository          Repositorio de likes — para contar likes de cada track
     * @param comentarioRepository    Repositorio de comentarios — para contar comentarios de cada track
     */
    public PlaylistService(PlaylistRepository playlistRepository,
            PlaylistTrackRepository playlistTrackRepository,
            TrackRepository trackRepository,
            UsuarioRepository usuarioRepository,
            LikeRepository likeRepository,
            ComentarioRepository comentarioRepository) {
        this.playlistRepository = playlistRepository;
        this.playlistTrackRepository = playlistTrackRepository;
        this.trackRepository = trackRepository;
        this.usuarioRepository = usuarioRepository;
        this.likeRepository = likeRepository;
        this.comentarioRepository = comentarioRepository;
    }

    /**
     * Crea una nueva playlist para un usuario.
     * Comprueba que el usuario no tenga ya una playlist con el mismo nombre
     * antes de persistir — la BD también tiene la restricción unique_nombre_usuario
     * pero es mejor validarlo en el service para devolver un mensaje claro.
     *
     * @param idUsuario ID del usuario propietario
     * @param nombre    Nombre de la nueva playlist
     * @return true si se creó correctamente, false si ya existe una playlist con ese nombre
     */
    public boolean crearPlaylist(Integer idUsuario, String nombre) {
        // Comprobamos que no exista ya una playlist con ese nombre para este usuario
        if (playlistRepository.existsByNombreAndUsuarioIdUsuario(nombre, idUsuario)) {
            return false;
        }
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        playlistRepository.save(new Playlist(nombre, usuario));
        return true;
    }

    /**
     * Elimina una playlist por su id.
     * Los tracks de la playlist se eliminan en cascada gracias a la FK de la BD.
     *
     * @param idPlaylist ID de la playlist a eliminar
     */
    public void eliminarPlaylist(Integer idPlaylist) {
        playlistRepository.deleteById(idPlaylist);
    }

    /**
     * Añade un track a una playlist.
     * Comprueba que el track no esté ya en la playlist antes de añadirlo.
     *
     * @param idPlaylist ID de la playlist
     * @param idTrack    ID del track a añadir
     * @return true si se añadió correctamente, false si el track ya estaba en la playlist
     */
    public boolean añadirTrack(Integer idPlaylist, Integer idTrack) {
        // Comprobamos que el track no esté ya en la playlist
        if (playlistTrackRepository.existsByIdIdPlaylistAndIdIdTrack(idPlaylist, idTrack)) {
            return false;
        }
        Playlist playlist = playlistRepository.findById(idPlaylist).orElse(null);
        Track track = trackRepository.findById(idTrack).orElse(null);
        playlistTrackRepository.save(new PlaylistTrack(playlist, track));
        return true;
    }

    /**
     * Quita un track de una playlist.
     *
     * @param idPlaylist ID de la playlist
     * @param idTrack    ID del track a quitar
     */
    @Transactional
    public void quitarTrack(Integer idPlaylist, Integer idTrack) {
        playlistTrackRepository.deleteByIdIdPlaylistAndIdIdTrack(idPlaylist, idTrack);
    }

    /**
     * Devuelve todas las playlists de un usuario con sus tracks.
     * Convierte cada Playlist a PlaylistData y cada track a TrackResumen
     * para no exponer las entidades directamente al frontend.
     *
     * @param idUsuario ID del usuario
     * @return Lista de PlaylistData con las playlists del usuario y sus tracks
     */
    public List<PlaylistData> getPlaylistsByUsuario(Integer idUsuario) {
        List<Playlist> playlists = playlistRepository.findByUsuarioIdUsuario(idUsuario);
        List<PlaylistData> resultado = new ArrayList<>();

        for (Playlist playlist : playlists) {
            // Obtenemos los tracks de esta playlist ordenados por fecha de añadido
            List<PlaylistTrack> playlistTracks = playlistTrackRepository
                    .findByIdIdPlaylistOrderByFecAñadidoDesc(playlist.getIdPlaylist());

            // Convertimos cada PlaylistTrack a TrackResumen
            List<TrackResumen> tracks = new ArrayList<>();
            for (PlaylistTrack pt : playlistTracks) {
                Track track = pt.getTrack();
                Integer likes = likeRepository.countByTrackIdTrack(track.getIdTrack());
                Integer comentarios = comentarioRepository.countByTrackIdTrack(track.getIdTrack());
                // Sanitizamos la portada igual que en TrackResumen
                tracks.add(new TrackResumen(track, likes, comentarios));
            }

            resultado.add(new PlaylistData(
                    playlist.getIdPlaylist(),
                    playlist.getNombre(),
                    playlist.getFecCreacion(),
                    tracks
            ));
        }
        return resultado;
    }
}