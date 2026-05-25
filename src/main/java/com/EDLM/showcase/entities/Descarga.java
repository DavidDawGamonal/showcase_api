package com.EDLM.showcase.entities;

import java.time.LocalDateTime;

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

/**
 * Representa la entidad de descarga en el sistema de showcase para productores musicales.
 * Esta clase mapea la tabla 'descargas' en la base de datos y registra cada vez que
 * un usuario descarga un track.
 *
 * <p>Características principales de una descarga:</p>
 * <ul>
 *   <li>Cada descarga pertenece a un {@link Usuario} y a un {@link Track}</li>
 *   <li>A diferencia de los likes, un usuario puede descargar el mismo track varias veces</li>
 *   <li>Si se elimina un usuario o un track, sus descargas se eliminan en cascada</li>
 *   <li>Almacena la fecha y hora exacta en que se realizó la descarga</li>
 * </ul>
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see com.EDLM.showcase.entities.Usuario
 * @see com.EDLM.showcase.entities.Track
 */
@Entity
@Table(name = "descargas")
public class Descarga {

    /**
     * Identificador único de la descarga.
     * Este valor es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_descarga")
    private Integer idDescarga;

    /**
     * Usuario que realizó la descarga.
     * Establece una relación muchos-a-uno con la entidad Usuario.
     * La eliminación en cascada está definida a nivel de base de datos.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(
                    name = "fk_descargas_usuario"
                ))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    /**
     * Track que fue descargado.
     * Establece una relación muchos-a-uno con la entidad Track.
     * La eliminación en cascada está definida a nivel de base de datos.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_track", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(
                    name = "fk_descargas_track"
                ))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Track track;

    /**
     * Fecha y hora en que se realizó la descarga.
     * Se establece automáticamente a la fecha/hora actual al persistir.
     */
    @Column(name = "fec_descarga", nullable = false)
    private LocalDateTime fecDescarga;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Descarga() {
    }

    /**
     * Constructor para crear una descarga con los campos esenciales.
     * La fecha de descarga se establece automáticamente a la fecha/hora actual.
     *
     * @param usuario El usuario que realiza la descarga
     * @param track   El track que se descarga
     */
    public Descarga(Usuario usuario, Track track) {
        this.usuario = usuario;
        this.track = track;
        this.fecDescarga = LocalDateTime.now();
    }

    /**
     * Método que se ejecuta automáticamente antes de persistir la entidad por primera vez.
     * Establece la fecha de descarga a la fecha/hora actual si no se ha proporcionado.
     */
    @PrePersist
    protected void onCreate() {
        if (fecDescarga == null) {
            fecDescarga = LocalDateTime.now();
        }
    }

    /**
     * Obtiene el identificador único de la descarga.
     *
     * @return El ID de la descarga
     */
    public Integer getIdDescarga() {
        return idDescarga;
    }

    /**
     * Establece el identificador único de la descarga.
     *
     * @param idDescarga El nuevo ID para la descarga
     */
    public void setIdDescarga(Integer idDescarga) {
        this.idDescarga = idDescarga;
    }

    /**
     * Obtiene el usuario que realizó la descarga.
     *
     * @return El usuario autor de la descarga
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario autor de la descarga.
     *
     * @param usuario El nuevo usuario autor
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el track que fue descargado.
     *
     * @return El track descargado
     */
    public Track getTrack() {
        return track;
    }

    /**
     * Establece el track descargado.
     *
     * @param track El nuevo track descargado
     */
    public void setTrack(Track track) {
        this.track = track;
    }

    /**
     * Obtiene la fecha y hora en que se realizó la descarga.
     *
     * @return La fecha y hora de la descarga
     */
    public LocalDateTime getFecDescarga() {
        return fecDescarga;
    }

    /**
     * Establece la fecha y hora de la descarga.
     * Normalmente no es necesario usar este método ya que se establece automáticamente.
     *
     * @param fecDescarga La nueva fecha y hora para la descarga
     */
    public void setFecDescarga(LocalDateTime fecDescarga) {
        this.fecDescarga = fecDescarga;
    }

    /**
     * Devuelve una representación en cadena del objeto Descarga.
     *
     * @return String con la información básica de la descarga
     */
    @Override
    public String toString() {
        return "Descarga{" +
                "idDescarga=" + idDescarga +
                ", idUsuario=" + (usuario != null ? usuario.getIdUsuario() : null) +
                ", idTrack=" + (track != null ? track.getIdTrack() : null) +
                ", fecDescarga=" + fecDescarga +
                '}';
    }
}