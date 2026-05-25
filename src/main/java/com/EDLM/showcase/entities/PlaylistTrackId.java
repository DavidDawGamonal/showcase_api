package com.EDLM.showcase.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Clase que representa la clave primaria compuesta de la entidad {@link PlaylistTrack}.
 * JPA requiere una clase separada para claves primarias compuestas anotada con @Embeddable.
 * Debe implementar Serializable y definir equals() y hashCode() para que JPA
 * pueda comparar correctamente las claves.
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Embeddable
public class PlaylistTrackId implements Serializable {

    /** ID de la playlist — primera parte de la clave compuesta. */
    @Column(name = "id_playlist")
    private Integer idPlaylist;

    /** ID del track — segunda parte de la clave compuesta. */
    @Column(name = "id_track")
    private Integer idTrack;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public PlaylistTrackId() {
    }

    /**
     * Constructor con los dos campos de la clave compuesta.
     *
     * @param idPlaylist ID de la playlist
     * @param idTrack    ID del track
     */
    public PlaylistTrackId(Integer idPlaylist, Integer idTrack) {
        this.idPlaylist = idPlaylist;
        this.idTrack = idTrack;
    }

    /**
     * Obtiene el ID de la playlist.
     * @return El ID de la playlist
     */
    public Integer getIdPlaylist() {
        return idPlaylist;
    }

    /**
     * Establece el ID de la playlist.
     * @param idPlaylist El nuevo ID de la playlist
     */
    public void setIdPlaylist(Integer idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    /**
     * Obtiene el ID del track.
     * @return El ID del track
     */
    public Integer getIdTrack() {
        return idTrack;
    }

    /**
     * Establece el ID del track.
     * @param idTrack El nuevo ID del track
     */
    public void setIdTrack(Integer idTrack) {
        this.idTrack = idTrack;
    }

    /**
     * Dos claves son iguales si tienen el mismo idPlaylist e idTrack.
     * Necesario para que JPA compare correctamente las claves primarias.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistTrackId that = (PlaylistTrackId) o;
        return Objects.equals(idPlaylist, that.idPlaylist) &&
               Objects.equals(idTrack, that.idTrack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlaylist, idTrack);
    }
}