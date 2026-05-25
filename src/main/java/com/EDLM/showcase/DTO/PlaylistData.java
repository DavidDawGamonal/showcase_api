package com.EDLM.showcase.DTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO (Data Transfer Object) que representa una playlist con sus tracks.
 * Se usa en lugar de la entidad Playlist para evitar problemas de serialización
 * con las relaciones lazy de JPA y seguir el patrón DTO del proyecto.
 *
 * <p>Contiene los datos básicos de la playlist más la lista de tracks
 * que contiene, representados como TrackResumen.</p>
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
public class PlaylistData {

    /** Identificador único de la playlist. */
    private Integer idPlaylist;

    /** Nombre de la playlist. */
    private String nombre;

    /**
     * Fecha y hora de creación de la playlist.
     */
    private LocalDateTime fecCreacion;

    /**
     * Lista de tracks que contiene la playlist.
     * Cada track se representa como TrackResumen para no exponer
     * la entidad Track directamente al frontend.
     */
    private List<TrackResumen> tracks;

    /**
     * Constructor con todos los campos.
     *
     * @param idPlaylist  ID de la playlist
     * @param nombre      Nombre de la playlist
     * @param fecCreacion Fecha de creación
     * @param tracks      Lista de tracks de la playlist
     */
    public PlaylistData(Integer idPlaylist, String nombre, LocalDateTime fecCreacion,
            List<TrackResumen> tracks) {
        this.idPlaylist = idPlaylist;
        this.nombre = nombre;
        this.fecCreacion = fecCreacion;
        this.tracks = tracks;
    }

    /**
     * Obtiene el identificador único de la playlist.
     * @return El id de la playlist
     */
    public Integer getIdPlaylist() {
        return idPlaylist;
    }

    /**
     * Establece el identificador único de la playlist.
     * @param idPlaylist El nuevo id de la playlist
     */
    public void setIdPlaylist(Integer idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    /**
     * Obtiene el nombre de la playlist.
     * @return El nombre de la playlist
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la playlist.
     * @param nombre El nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la fecha de creación de la playlist.
     * @return La fecha de creación
     */
    public LocalDateTime getFecCreacion() {
        return fecCreacion;
    }

    /**
     * Establece la fecha de creación de la playlist.
     * @param fecCreacion La nueva fecha de creación
     */
    public void setFecCreacion(LocalDateTime fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    /**
     * Obtiene la lista de tracks de la playlist.
     * @return Lista de TrackResumen con los tracks de la playlist
     */
    public List<TrackResumen> getTracks() {
        return tracks;
    }

    /**
     * Establece la lista de tracks de la playlist.
     * @param tracks La nueva lista de tracks
     */
    public void setTracks(List<TrackResumen> tracks) {
        this.tracks = tracks;
    }
}