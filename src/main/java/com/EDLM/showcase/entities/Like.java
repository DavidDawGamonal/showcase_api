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
 * Representa la entidad de like (me gusta) en el sistema de showcase para productores musicales.
 * Esta clase mapea la tabla 'likes' en la base de datos y permite a los usuarios
 * expresar su gusto por los tracks musicales.
 * 
 * <p>Características principales de un like:</p>
 * <ul>
 *   <li>Cada like pertenece a un {@link Usuario} y a un {@link Track}</li>
 *   <li>La combinación usuario + track debe ser única (un usuario no puede dar like dos veces al mismo track)</li>
 *   <li>Si se elimina un usuario o un track, sus likes se eliminan en cascada</li>
 *   <li>Almacena la fecha y hora exacta en que se dio el like</li>
 * </ul>
 * 
 * <p>Esta entidad actúa como una tabla de relación many-to-many entre Usuario y Track
 * con información adicional (fecha del like).</p>
 * 
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see com.EDLM.showcase.entities.Usuario
 * @see com.EDLM.showcase.entities.Track
 */
@Entity
@Table(name = "likes",
       uniqueConstraints = @UniqueConstraint(
           name = "unique_usuario_track",
           columnNames = {"id_usuario", "id_track"}
       )
)
public class Like {
    
    /**
     * Identificador único del like.
     * Este valor es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_like")
    private Integer idLike;
    
    /**
     * Usuario que dio el like.
     * Establece una relación muchos-a-uno con la entidad Usuario.
     * La eliminación en cascada está definida a nivel de base de datos.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(
                    name = "fk_likes_usuario"
                ))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;
    
    /**
     * Track que recibió el like.
     * Establece una relación muchos-a-uno con la entidad Track.
     * La eliminación en cascada está definida a nivel de base de datos.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_track", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(
                    name = "fk_likes_track"
                ))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Track track;
    
    /**
     * Fecha y hora en que se dio el like.
     * Por defecto se establece la fecha y hora actual del sistema.
     */
    @Column(name = "fec_like", nullable = false)
    private LocalDateTime fecLike;
    
    /**
     * Constructor por defecto requerido por JPA.
     * Crea una instancia vacía de Like.
     */
    public Like() {
    }
    
    /**
     * Constructor para crear un like con los campos esenciales.
     * La fecha del like se establece automáticamente a la fecha/hora actual.
     * 
     * @param usuario El usuario que da el like
     * @param track El track que recibe el like
     */
    public Like(Usuario usuario, Track track) {
        this.usuario = usuario;
        this.track = track;
        this.fecLike = LocalDateTime.now();
    }
    
    /**
     * Constructor para crear un like con fecha personalizada.
     * Útil para pruebas o migraciones de datos.
     * 
     * @param usuario El usuario que da el like
     * @param track El track que recibe el like
     * @param fecLike La fecha y hora del like
     */
    public Like(Usuario usuario, Track track, LocalDateTime fecLike) {
        this.usuario = usuario;
        this.track = track;
        this.fecLike = fecLike;
    }
    
    /**
     * Método que se ejecuta automáticamente antes de persistir la entidad por primera vez.
     * Establece la fecha del like a la fecha/hora actual si no se ha proporcionado.
     */
    @PrePersist
    protected void onCreate() {
        if (fecLike == null) {
            fecLike = LocalDateTime.now();
        }
    }
    
    /**
     * Obtiene el identificador único del like.
     * 
     * @return El ID del like
     */
    public Integer getIdLike() {
        return idLike;
    }
    
    /**
     * Establece el identificador único del like.
     * 
     * @param idLike El nuevo ID para el like
     */
    public void setIdLike(Integer idLike) {
        this.idLike = idLike;
    }
    
    /**
     * Obtiene el usuario que dio el like.
     * 
     * @return El usuario autor del like
     */
    public Usuario getUsuario() {
        return usuario;
    }
    
    /**
     * Establece el usuario autor del like.
     * 
     * @param usuario El nuevo usuario autor
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    /**
     * Obtiene el track que recibió el like.
     * 
     * @return El track likeado
     */
    public Track getTrack() {
        return track;
    }
    
    /**
     * Establece el track que recibe el like.
     * 
     * @param track El nuevo track likeado
     */
    public void setTrack(Track track) {
        this.track = track;
    }
    
    /**
     * Obtiene la fecha y hora en que se dio el like.
     * 
     * @return La fecha y hora del like
     */
    public LocalDateTime getFecLike() {
        return fecLike;
    }
    
    /**
     * Establece la fecha y hora del like.
     * Normalmente no es necesario usar este método ya que se establece automáticamente.
     * 
     * @param fecLike La nueva fecha y hora para el like
     */
    public void setFecLike(LocalDateTime fecLike) {
        this.fecLike = fecLike;
    }
    
    /**
     * Compara este like con otro objeto para determinar igualdad.
     * Dos likes se consideran iguales si pertenecen al mismo usuario y al mismo track.
     * Esto es útil para validar la restricción de unicidad a nivel de aplicación.
     * 
     * @param o El objeto a comparar
     * @return true si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(usuario != null ? usuario.getIdUsuario() : null, 
                             like.usuario != null ? like.usuario.getIdUsuario() : null) &&
               Objects.equals(track != null ? track.getIdTrack() : null, 
                             like.track != null ? like.track.getIdTrack() : null);
    }
    
    /**
     * Genera un código hash basado en el usuario y track.
     * Consistente con el método equals.
     * 
     * @return El código hash del like
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            usuario != null ? usuario.getIdUsuario() : null,
            track != null ? track.getIdTrack() : null
        );
    }
    
    /**
     * Devuelve una representación en cadena del objeto Like.
     * 
     * @return String con la información básica del like
     */
    @Override
    public String toString() {
        return "Like{" +
                "idLike=" + idLike +
                ", idUsuario=" + (usuario != null ? usuario.getIdUsuario() : null) +
                ", idTrack=" + (track != null ? track.getIdTrack() : null) +
                ", fecLike=" + fecLike +
                '}';
    }
}