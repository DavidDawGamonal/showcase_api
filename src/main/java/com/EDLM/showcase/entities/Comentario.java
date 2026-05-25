package com.EDLM.showcase.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Representa la entidad de comentario en el sistema de showcase para productores musicales.
 * Esta clase mapea la tabla 'comentarios' en la base de datos y permite a los usuarios
 * comentar sobre los tracks musicales.
 * 
 * <p>Características principales de un comentario:</p>
 * <ul>
 *   <li>Cada comentario pertenece a un {@link Usuario} y a un {@link Track}</li>
 *   <li>Soporta comentarios anidados (respuestas) mediante la auto-referencia id_comentPadre</li>
 *   <li>Si se elimina un usuario o un track, sus comentarios se eliminan en cascada</li>
 *   <li>Almacena la fecha y hora exacta de creación del comentario</li>
 * </ul>
 * 
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see com.EDLM.showcase.entities.Usuario
 * @see com.EDLM.showcase.entities.Track
 */
@Entity
@Table(name = "comentarios")
public class Comentario {
    
    /**
     * Identificador único del comentario.
     * Este valor es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coment")
    private Integer idComent;
    
    /**
     * Usuario que realizó el comentario.
     * Establece una relación muchos-a-uno con la entidad Usuario.
     * La eliminación en cascada está definida a nivel de base de datos.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(
                    name = "fk_comentarios_usuario"
                ))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;
    
    /**
     * Track al que pertenece el comentario.
     * Establece una relación muchos-a-uno con la entidad Track.
     * La eliminación en cascada está definida a nivel de base de datos.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_track", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(
                    name = "fk_comentarios_track"
                ))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Track track;
    
    /**
     * Contenido textual del comentario.
     * Se almacena como tipo TEXT en la base de datos para permitir
     * comentarios de longitud extendida.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;
    
    /**
     * Fecha y hora en que se realizó el comentario.
     * Por defecto se establece la fecha y hora actual del sistema.
     */
    @Column(name = "fec_coment", nullable = false)
    private LocalDateTime fecComent;
    
    /**
     * Comentario padre en caso de que este comentario sea una respuesta.
     * Auto-referencia que permite crear hilos de comentarios anidados.
     * Si es null, significa que es un comentario principal.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coment_padre")
    @JsonIgnore
    private Comentario comentarioPadre;
    
    /**
     * Lista de respuestas a este comentario.
     * Relación uno-a-muchos con la misma entidad para obtener
     * todos los comentarios que son respuesta a este.
     */
    @OneToMany(mappedBy = "comentarioPadre", cascade = CascadeType.ALL, 
               fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties("comentarioPadre")
    private List<Comentario> respuestas = new ArrayList<>();
    
    /**
     * Constructor por defecto requerido por JPA.
     * Crea una instancia vacía de Comentario.
     */
    public Comentario() {
    }
    
    /**
     * Constructor para crear un comentario principal (sin comentario padre).
     * La fecha del comentario se establece automáticamente a la fecha/hora actual.
     * 
     * @param usuario El usuario que realiza el comentario
     * @param track El track sobre el que se comenta
     * @param contenido El texto del comentario
     */
    public Comentario(Usuario usuario, Track track, String contenido) {
        this.usuario = usuario;
        this.track = track;
        this.contenido = contenido;
        this.fecComent = LocalDateTime.now();
        this.comentarioPadre = null;
    }
    
    /**
     * Constructor para crear una respuesta a otro comentario.
     * La fecha del comentario se establece automáticamente a la fecha/hora actual.
     * 
     * @param usuario El usuario que realiza el comentario
     * @param track El track sobre el que se comenta
     * @param contenido El texto del comentario
     * @param comentarioPadre El comentario al que se está respondiendo
     */
    public Comentario(Usuario usuario, Track track, String contenido, Comentario comentarioPadre) {
        this.usuario = usuario;
        this.track = track;
        this.contenido = contenido;
        this.fecComent = LocalDateTime.now();
        this.comentarioPadre = comentarioPadre;
    }
    
    /**
     * Método que se ejecuta automáticamente antes de persistir la entidad por primera vez.
     * Establece la fecha del comentario a la fecha/hora actual si no se ha proporcionado.
     */
    @PrePersist
    protected void onCreate() {
        if (fecComent == null) {
            fecComent = LocalDateTime.now();
        }
    }
    
    /**
     * Obtiene el identificador único del comentario.
     * 
     * @return El ID del comentario
     */
    public Integer getIdComent() {
        return idComent;
    }
    
    /**
     * Establece el identificador único del comentario.
     * 
     * @param idComent El nuevo ID para el comentario
     */
    public void setIdComent(Integer idComent) {
        this.idComent = idComent;
    }
    
    /**
     * Obtiene el usuario que realizó el comentario.
     * 
     * @return El usuario autor del comentario
     */
    public Usuario getUsuario() {
        return usuario;
    }
    
    /**
     * Establece el usuario autor del comentario.
     * 
     * @param usuario El nuevo usuario autor
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    /**
     * Obtiene el track al que pertenece el comentario.
     * 
     * @return El track comentado
     */
    public Track getTrack() {
        return track;
    }
    
    /**
     * Establece el track al que pertenece el comentario.
     * 
     * @param track El nuevo track comentado
     */
    public void setTrack(Track track) {
        this.track = track;
    }
    
    /**
     * Obtiene el contenido textual del comentario.
     * 
     * @return El contenido del comentario
     */
    public String getContenido() {
        return contenido;
    }
    
    /**
     * Establece el contenido textual del comentario.
     * 
     * @param contenido El nuevo contenido para el comentario
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    /**
     * Obtiene la fecha y hora en que se realizó el comentario.
     * 
     * @return La fecha y hora del comentario
     */
    public LocalDateTime getFecComent() {
        return fecComent;
    }
    
    /**
     * Establece la fecha y hora del comentario.
     * Normalmente no es necesario usar este método ya que se establece automáticamente.
     * 
     * @param fecComent La nueva fecha y hora para el comentario
     */
    public void setFecComent(LocalDateTime fecComent) {
        this.fecComent = fecComent;
    }
    
    /**
     * Obtiene el comentario padre (si este comentario es una respuesta).
     * 
     * @return El comentario padre, o null si es un comentario principal
     */
    public Comentario getComentarioPadre() {
        return comentarioPadre;
    }
    
    /**
     * Establece el comentario padre para este comentario.
     * 
     * @param comentarioPadre El nuevo comentario padre
     */
    public void setComentarioPadre(Comentario comentarioPadre) {
        this.comentarioPadre = comentarioPadre;
    }
    
    /**
     * Obtiene la lista de respuestas a este comentario.
     * 
     * @return Lista de comentarios que son respuesta a este
     */
    public List<Comentario> getRespuestas() {
        return respuestas;
    }
    
    /**
     * Establece la lista de respuestas a este comentario.
     * 
     * @param respuestas La nueva lista de respuestas
     */
    public void setRespuestas(List<Comentario> respuestas) {
        this.respuestas = respuestas;
    }
    
    /**
     * Método auxiliar para añadir una respuesta a este comentario.
     * Establece automáticamente la relación bidireccional.
     * 
     * @param respuesta La respuesta a añadir
     */
    public void addRespuesta(Comentario respuesta) {
        respuestas.add(respuesta);
        respuesta.setComentarioPadre(this);
    }
    
    /**
     * Método auxiliar para eliminar una respuesta de este comentario.
     * 
     * @param respuesta La respuesta a eliminar
     */
    public void removeRespuesta(Comentario respuesta) {
        respuestas.remove(respuesta);
        respuesta.setComentarioPadre(null);
    }
    
    /**
     * Verifica si este comentario es un comentario principal (no tiene padre).
     * 
     * @return true si es un comentario principal, false si es una respuesta
     */
    public boolean isComentarioPrincipal() {
        return comentarioPadre == null;
    }
    
    /**
     * Verifica si este comentario tiene respuestas.
     * 
     * @return true si tiene al menos una respuesta, false en caso contrario
     */
    public boolean tieneRespuestas() {
        return respuestas != null && !respuestas.isEmpty();
    }
    
    /**
     * Devuelve una representación en cadena del objeto Comentario.
     * 
     * @return String con la información básica del comentario
     */
    @Override
    public String toString() {
        return "Comentario{" +
                "idComent=" + idComent +
                ", usuario=" + (usuario != null ? usuario.getIdUsuario() : null) +
                ", track=" + (track != null ? track.getIdTrack() : null) +
                ", contenido='" + (contenido != null ? contenido.substring(0, Math.min(contenido.length(), 50)) + "..." : null) + '\'' +
                ", fecComent=" + fecComent +
                ", idComentPadre=" + (comentarioPadre != null ? comentarioPadre.getIdComent() : null) +
                ", numRespuestas=" + (respuestas != null ? respuestas.size() : 0) +
                '}';
    }
}