package com.EDLM.showcase.DTO;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) que encapsula los filtros para buscar tracks.
 * Se usa para pasar los criterios de búsqueda desde el controller al service
 * de forma limpia, en lugar de pasar múltiples parámetros sueltos.
 *
 * <p>Filtros disponibles:</p>
 * <ul>
 *   <li>genero: filtra tracks por género musical</li>
 *   <li>idUsuario: filtra tracks por el productor que los subió</li>
 *   <li>nombreProductor: filtra tracks por nombre del productor (búsqueda parcial)</li>
 *   <li>minLikes: filtra tracks con al menos N likes</li>
 *   <li>minComentarios: filtra tracks con al menos N comentarios</li>
 *   <li>fechaDesde: filtra tracks subidos a partir de esta fecha</li>
 *   <li>fechaHasta: filtra tracks subidos hasta esta fecha</li>
 *   <li>ordenarPor: criterio de ordenación (likes, comentarios, fecha, reproducciones)</li>
 * </ul>
 *
 * <p>Todos los campos son opcionales — si son null no se aplica ese filtro.</p>
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.1
 * @since 1.0
 */
public class FiltroTrack {

    /** Género musical por el que filtrar. null = sin filtro. */
    private String genero;

    /** ID del productor por el que filtrar. null = sin filtro. */
    private Integer idUsuario;

    /** Nombre del productor — búsqueda parcial case-insensitive. null = sin filtro. */
    private String nombreProductor;

    /** Número mínimo de likes. null = sin filtro. */
    private Integer minLikes;

    /** Número mínimo de comentarios. null = sin filtro. */
    private Integer minComentarios;

    /** Fecha de subida mínima (inclusive). null = sin filtro. */
    private LocalDate fechaDesde;

    /** Fecha de subida máxima (inclusive). null = sin filtro. */
    private LocalDate fechaHasta;

    /**
     * Criterio de ordenación. Valores posibles:
     * "likes" (por defecto), "comentarios", "fecha", "reproducciones".
     * null = ordena por likes.
     */
    private String ordenarPor;

    /**
     * Constructor vacío requerido por Spring para el binding de @ModelAttribute.
     */
    public FiltroTrack() {
    }

    /**
     * Constructor con los campos originales — mantiene compatibilidad
     * con el código existente que construye FiltroTrack directamente.
     *
     * @param genero    Género musical a filtrar, o null
     * @param idUsuario ID del productor a filtrar, o null
     */
    public FiltroTrack(String genero, Integer idUsuario) {
        this.genero = genero;
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el género musical del filtro.
     * @return El género, o null si no se aplica filtro de género
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Establece el género musical del filtro.
     * @param genero El nuevo género a filtrar
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Obtiene el ID del productor del filtro.
     * @return El id del usuario, o null si no se aplica filtro de usuario
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID del productor del filtro.
     * @param idUsuario El nuevo id de usuario a filtrar
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el nombre del productor del filtro.
     * @return El nombre del productor, o null si no se aplica filtro
     */
    public String getNombreProductor() {
        return nombreProductor;
    }

    /**
     * Establece el nombre del productor del filtro.
     * @param nombreProductor El nombre a filtrar
     */
    public void setNombreProductor(String nombreProductor) {
        this.nombreProductor = nombreProductor;
    }

    /**
     * Obtiene el número mínimo de likes del filtro.
     * @return El mínimo de likes, o null si no se aplica filtro
     */
    public Integer getMinLikes() {
        return minLikes;
    }

    /**
     * Establece el número mínimo de likes del filtro.
     * @param minLikes El mínimo de likes a filtrar
     */
    public void setMinLikes(Integer minLikes) {
        this.minLikes = minLikes;
    }

    /**
     * Obtiene el número mínimo de comentarios del filtro.
     * @return El mínimo de comentarios, o null si no se aplica filtro
     */
    public Integer getMinComentarios() {
        return minComentarios;
    }

    /**
     * Establece el número mínimo de comentarios del filtro.
     * @param minComentarios El mínimo de comentarios a filtrar
     */
    public void setMinComentarios(Integer minComentarios) {
        this.minComentarios = minComentarios;
    }

    /**
     * Obtiene la fecha de subida mínima del filtro.
     * @return La fecha desde, o null si no se aplica filtro
     */
    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    /**
     * Establece la fecha de subida mínima del filtro.
     * @param fechaDesde La fecha mínima de subida
     */
    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    /**
     * Obtiene la fecha de subida máxima del filtro.
     * @return La fecha hasta, o null si no se aplica filtro
     */
    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    /**
     * Establece la fecha de subida máxima del filtro.
     * @param fechaHasta La fecha máxima de subida
     */
    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    /**
     * Obtiene el criterio de ordenación del filtro.
     * @return El criterio de ordenación, o null para ordenar por likes
     */
    public String getOrdenarPor() {
        return ordenarPor;
    }

    /**
     * Establece el criterio de ordenación del filtro.
     * @param ordenarPor El nuevo criterio de ordenación
     */
    public void setOrdenarPor(String ordenarPor) {
        this.ordenarPor = ordenarPor;
    }
}