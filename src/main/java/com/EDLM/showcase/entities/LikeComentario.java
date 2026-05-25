package com.EDLM.showcase.entities;

import java.time.LocalDateTime;

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
 * Entidad que representa un like dado por un usuario a un comentario.
 * Mapea la tabla 'likes_comentarios' en la base de datos.
 *
 * <p>Un usuario solo puede dar like una vez a cada comentario —
 * la constraint unique_like_coment lo garantiza tanto en BD como en la lógica del service.</p>
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "likes_comentarios",
       uniqueConstraints = @UniqueConstraint(
           name = "unique_like_coment",
           columnNames = {"id_usuario", "id_coment"}
       ))
public class LikeComentario {

    /** Identificador único del like. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_like_coment")
    private Integer idLikeComent;

    /** Usuario que dio el like. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(name = "fk_likes_coment_usuario"))
    private Usuario usuario;

    /** Comentario que recibió el like. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coment", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(name = "fk_likes_coment_comentario"))
    private Comentario comentario;

    /** Fecha y hora en que se dio el like. */
    @Column(name = "fec_like", nullable = false)
    private LocalDateTime fecLike;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public LikeComentario() {
    }

    /**
     * Constructor para crear un like a un comentario.
     * La fecha se establece automáticamente.
     *
     * @param usuario    El usuario que da el like
     * @param comentario El comentario que recibe el like
     */
    public LikeComentario(Usuario usuario, Comentario comentario) {
        this.usuario = usuario;
        this.comentario = comentario;
    }

    /**
     * Establece la fecha del like automáticamente al persistir.
     */
    @PrePersist
    protected void onCreate() {
        if (fecLike == null) {
            fecLike = LocalDateTime.now();
        }
    }

    /**
     * Obtiene el id del like.
     * @return El id del like
     */
    public Integer getIdLikeComent() {
        return idLikeComent;
    }

    /**
     * Establece el id del like.
     * @param idLikeComent El nuevo id
     */
    public void setIdLikeComent(Integer idLikeComent) {
        this.idLikeComent = idLikeComent;
    }

    /**
     * Obtiene el usuario que dio el like.
     * @return El usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario que dio el like.
     * @param usuario El nuevo usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el comentario que recibió el like.
     * @return El comentario
     */
    public Comentario getComentario() {
        return comentario;
    }

    /**
     * Establece el comentario que recibió el like.
     * @param comentario El nuevo comentario
     */
    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }

    /**
     * Obtiene la fecha en que se dio el like.
     * @return La fecha del like
     */
    public LocalDateTime getFecLike() {
        return fecLike;
    }

    /**
     * Establece la fecha en que se dio el like.
     * @param fecLike La nueva fecha
     */
    public void setFecLike(LocalDateTime fecLike) {
        this.fecLike = fecLike;
    }
}