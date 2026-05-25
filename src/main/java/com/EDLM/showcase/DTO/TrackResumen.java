package com.EDLM.showcase.DTO;

import java.io.File;
import com.EDLM.showcase.entities.Track;

/**
 * DTO (Data Transfer Object) que representa un resumen de un track para mostrar
 * en listados como el Top 5 o las recomendaciones.
 * Se construye a partir de la entidad {@link Track} y datos agregados adicionales
 * (likes y comentarios) para evitar múltiples llamadas al frontend.
 *
 * <p>A diferencia de la entidad Track, este DTO:</p>
 * <ul>
 *   <li>Formatea la duración como String "HH:mm:ss" listo para mostrar</li>
 *   <li>Sanitiza la ruta de la portada reemplazando el separador de sistema
 *       por '@' para que sea segura en URLs</li>
 *   <li>Incluye directamente el nombre e id del productor sin exponer la entidad Usuario completa</li>
 *   <li>Incluye el número de likes y comentarios ya calculados</li>
 * </ul>
 *
 * @author Elvis David Lara Manrique
 * @version 1.0
 * @since 1.0
 * @see Track
 */
public class TrackResumen {

    /** Identificador único del track. */
    private Integer idTrack;

    /** Título del track. */
    private String titulo;

    /** Género musical del track. */
    private String genero;

    /**
     * Duración del track formateada como String en formato "HH:mm:ss".
     * Se convierte desde LocalTime en el constructor para que el frontend
     * pueda mostrarlo directamente sin transformaciones adicionales.
     */
    private String duracion;

    /**
     * Ruta de la imagen de portada del track.
     * El separador de directorios del sistema operativo (/ o \) se reemplaza
     * por '@' para que la ruta sea segura al usarla en una URL de la API.
     * Si el track no tiene portada, se asigna "portadas/portada_default.jpg".
     */
    private String portada;

    /** Número total de likes del track. */
    private Integer likes;

    /** Nombre del productor que subió el track. */
    private String nombreProductor;

    /** ID del productor que subió el track. */
    private Integer idProductor;

    /** Número total de comentarios del track. */
    private Integer comentarios;
    
    /** Fecha de subida del track — necesaria para ordenar por fecha en los filtros avanzados. */
    private java.time.LocalDateTime fecSubida;

    /** Número de reproducciones del track — necesario para ordenar por reproducciones en los filtros avanzados. */
    private Integer contReprod;

    /**
     * Constructor que construye el resumen a partir de la entidad Track
     * y los datos agregados de likes y comentarios.
     *
     * @param track      La entidad Track de la que se extraen los datos
     * @param likes      El número de likes calculado externamente (desde LikeRepository)
     * @param comentarios El número de comentarios calculado externamente (desde ComentarioRepository)
     */
    public TrackResumen(Track track, Integer likes, Integer comentarios) {
        this.idTrack = track.getIdTrack();
        this.titulo = track.getTitulo();
        this.genero = track.getGenero();
        this.duracion = track.getDuracion().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.portada = track.getPortada() != null
                ? track.getPortada().replace(File.separatorChar, '@')
                : "portadas/portada_default.jpg";
        this.likes = likes;
        this.nombreProductor = track.getUsuario().getNombre();
        this.idProductor = track.getUsuario().getIdUsuario();
        this.comentarios = comentarios;
        this.fecSubida = track.getFecSubida();
        this.contReprod = track.getContReprod();
    }

    /**
     * Obtiene el identificador único del track.
     * @return El id del track
     */
    public Integer getIdTrack() {
        return idTrack;
    }

    /**
     * Obtiene el título del track.
     * @return El título del track
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtiene el género musical del track.
     * @return El género del track
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Obtiene la duración del track formateada como "HH:mm:ss".
     * @return La duración del track
     */
    public String getDuracion() {
        return duracion;
    }

    /**
     * Obtiene la ruta de la portada del track sanitizada para uso en URLs.
     * @return La ruta de la portada
     */
    public String getPortada() {
        return portada;
    }

    /**
     * Obtiene el número de likes del track.
     * @return El número de likes
     */
    public Integer getLikes() {
        return likes;
    }

    /**
     * Obtiene el nombre del productor que subió el track.
     * @return El nombre del productor
     */
    public String getNombreProductor() {
        return nombreProductor;
    }

    /**
     * Obtiene el ID del productor que subió el track.
     * @return El id del productor
     */
    public Integer getIdProductor() {
        return idProductor;
    }

    /**
     * Obtiene el número de comentarios del track.
     * @return El número de comentarios
     */
    public Integer getComentarios() {
        return comentarios;
    }
    
    /**
     * Obtiene la fecha de subida del track.
     * @return La fecha de subida
     */
    public java.time.LocalDateTime getFecSubida() {
        return fecSubida;
    }

    /**
     * Obtiene el número de reproducciones del track.
     * @return El número de reproducciones
     */
    public Integer getContReprod() {
        return contReprod;
    }
}