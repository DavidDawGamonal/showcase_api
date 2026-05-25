package com.EDLM.showcase.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Representa la entidad de playlist en el sistema de showcase para productores musicales.
 * Esta clase mapea la tabla 'playlist' en la base de datos y permite a los usuarios
 * crear colecciones personalizadas con nombre propio.
 * 
 * <p>Características principales de una playlist:</p>
 * <ul>
 *   <li>Cada playlist pertenece a un {@link Usuario} (propietario)</li>
 *   <li>El nombre de la playlist debe ser único por usuario (un usuario no puede tener dos playlists con el mismo nombre)</li>
 *   <li>Almacena la fecha y hora de creación</li>
 * </ul>
 * 
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see com.EDLM.showcase.entities.Usuario
 */
@Entity
@Table(name = "playlist",
       uniqueConstraints = @UniqueConstraint(
           name = "unique_nombre_usuario",
           columnNames = {"nombre", "id_usuario"}
       )
)
public class Playlist {

    /**
     * Identificador único de la playlist.
     * Este valor es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_playlist")
    private Integer idPlaylist;

    /**
     * Nombre de la playlist.
     * Debe ser único por usuario y es obligatorio.
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Fecha y hora de creación de la playlist.
     * Se establece automáticamente a la fecha/hora actual mediante {@link #onCreate()}.
     */
    @Column(name = "fec_creacion", nullable = false)
    private LocalDateTime fecCreacion;

    /**
     * Usuario propietario de la playlist.
     * Establece una relación muchos-a-uno con la entidad Usuario.
     * La eliminación en cascada está definida a nivel de base de datos.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(
                    name = "fk_playlists_usuario"
                ))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    /**
     * Constructor por defecto requerido por JPA.
     * Crea una instancia vacía de Playlist.
     */
    public Playlist() {
    }

    /**
     * Constructor para crear una playlist con los campos esenciales.
     * La fecha de creación se establece automáticamente a la fecha/hora actual.
     *
     * @param nombre  El nombre de la playlist
     * @param usuario El usuario propietario de la playlist
     */
    public Playlist(String nombre, Usuario usuario) {
        this.nombre = nombre;
        this.usuario = usuario;
    }

    /**
     * Método que se ejecuta automáticamente antes de persistir la entidad por primera vez.
     * Establece la fecha de creación a la fecha/hora actual si no se ha proporcionado.
     */
    @PrePersist
    protected void onCreate() {
        if (fecCreacion == null) {
            fecCreacion = LocalDateTime.now();
        }
    }

    /**
     * Obtiene el identificador único de la playlist.
     *
     * @return El ID de la playlist
     */
    public Integer getIdPlaylist() {
        return idPlaylist;
    }

    /**
     * Establece el identificador único de la playlist.
     *
     * @param idPlaylist El nuevo ID para la playlist
     */
    public void setIdPlaylist(Integer idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    /**
     * Obtiene el nombre de la playlist.
     *
     * @return El nombre de la playlist
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la playlist.
     *
     * @param nombre El nuevo nombre para la playlist
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la fecha y hora de creación de la playlist.
     *
     * @return La fecha y hora de creación
     */
    public LocalDateTime getFecCreacion() {
        return fecCreacion;
    }

    /**
     * Establece la fecha y hora de creación de la playlist.
     * Normalmente no es necesario usar este método ya que se establece automáticamente.
     *
     * @param fecCreacion La nueva fecha y hora de creación
     */
    public void setFecCreacion(LocalDateTime fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    /**
     * Obtiene el usuario propietario de la playlist.
     *
     * @return El usuario propietario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario propietario de la playlist.
     *
     * @param usuario El nuevo usuario propietario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Compara esta playlist con otro objeto para determinar igualdad.
     * Dos playlists se consideran iguales si pertenecen al mismo usuario y tienen el mismo nombre.
     *
     * @param o El objeto a comparar
     * @return true si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(nombre, playlist.nombre) &&
               Objects.equals(usuario != null ? usuario.getIdUsuario() : null,
                             playlist.usuario != null ? playlist.usuario.getIdUsuario() : null);
    }

    /**
     * Genera un código hash basado en el nombre y el usuario.
     * Consistente con el método equals.
     *
     * @return El código hash de la playlist
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre, usuario != null ? usuario.getIdUsuario() : null);
    }

    /**
     * Devuelve una representación en cadena del objeto Playlist.
     *
     * @return String con la información básica de la playlist
     */
    @Override
    public String toString() {
        return "Playlist{" +
                "idPlaylist=" + idPlaylist +
                ", nombre='" + nombre + '\'' +
                ", fecCreacion=" + fecCreacion +
                ", idUsuario=" + (usuario != null ? usuario.getIdUsuario() : null) +
                '}';
    }
}
