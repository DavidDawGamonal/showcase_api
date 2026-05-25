package com.EDLM.showcase.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entidad que representa la relación many-to-many entre {@link Playlist} y {@link Track}.
 * Mapea la tabla intermedia 'playlist_tracks' que almacena qué tracks pertenecen
 * a cada playlist y cuándo fueron añadidos.
 *
 * <p>Usa @EmbeddedId con {@link PlaylistTrackId} para la clave primaria compuesta
 * (id_playlist + id_track), lo que garantiza que un track no puede estar
 * duplicado en la misma playlist.</p>
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "playlist_tracks")
public class PlaylistTrack {

    /**
     * Clave primaria compuesta (id_playlist + id_track).
     * @EmbeddedId indica que la clave primaria está definida en una clase separada.
     */
    @EmbeddedId
    private PlaylistTrackId id;

    /**
     * Playlist a la que pertenece este track.
     * @MapsId("idPlaylist") vincula este campo con la parte idPlaylist de la clave compuesta.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPlaylist")
    @JoinColumn(name = "id_playlist", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(
                    name = "fk_playlist_tracks_playlist"
                ))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Playlist playlist;

    /**
     * Track añadido a la playlist.
     * @MapsId("idTrack") vincula este campo con la parte idTrack de la clave compuesta.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idTrack")
    @JoinColumn(name = "id_track", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(
                    name = "fk_playlist_tracks_track"
                ))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Track track;

    /**
     * Fecha y hora en que el track fue añadido a la playlist.
     * Se establece automáticamente a la fecha/hora actual al persistir.
     */
    @Column(name = "fec_aniadido", nullable = false)
    private LocalDateTime fecAñadido;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public PlaylistTrack() {
    }

    /**
     * Constructor para crear una relación playlist-track.
     * La fecha de añadido se establece automáticamente.
     *
     * @param playlist La playlist a la que se añade el track
     * @param track    El track que se añade
     */
    public PlaylistTrack(Playlist playlist, Track track) {
        this.playlist = playlist;
        this.track = track;
        this.id = new PlaylistTrackId(playlist.getIdPlaylist(), track.getIdTrack());
        this.fecAñadido = LocalDateTime.now();
    }

    /**
     * Método que se ejecuta automáticamente antes de persistir la entidad.
     * Establece la fecha de añadido si no se ha proporcionado.
     */
    @PrePersist
    protected void onCreate() {
        if (fecAñadido == null) {
            fecAñadido = LocalDateTime.now();
        }
    }

    /**
     * Obtiene la clave primaria compuesta.
     * @return La clave primaria compuesta
     */
    public PlaylistTrackId getId() {
        return id;
    }

    /**
     * Establece la clave primaria compuesta.
     * @param id La nueva clave primaria compuesta
     */
    public void setId(PlaylistTrackId id) {
        this.id = id;
    }

    /**
     * Obtiene la playlist.
     * @return La playlist
     */
    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * Establece la playlist.
     * @param playlist La nueva playlist
     */
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    /**
     * Obtiene el track.
     * @return El track
     */
    public Track getTrack() {
        return track;
    }

    /**
     * Establece el track.
     * @param track El nuevo track
     */
    public void setTrack(Track track) {
        this.track = track;
    }

    /**
     * Obtiene la fecha en que el track fue añadido a la playlist.
     * @return La fecha de añadido
     */
    public LocalDateTime getFecAñadido() {
        return fecAñadido;
    }

    /**
     * Establece la fecha en que el track fue añadido a la playlist.
     * @param fecAñadido La nueva fecha de añadido
     */
    public void setFecAñadido(LocalDateTime fecAñadido) {
        this.fecAñadido = fecAñadido;
    }
}