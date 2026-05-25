package com.EDLM.showcase.DTO;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) que representa una descarga realizada por un usuario.
 * Se usa en lugar de la entidad Descarga para evitar exponer datos sensibles
 * y evitar problemas de serialización con las relaciones lazy de JPA.
 *
 * <p>Contiene los datos del track necesarios para mostrar el historial de descargas
 * más la fecha en que el usuario realizó la descarga.</p>
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
public class DescargaData {

    /** Identificador único del track. */
    private Integer idTrack;

    /** Título del track. */
    private String titulo;

    /** Nombre del productor que subió el track. */
    private String nombreProductor;

    /**
     * Ruta de la imagen de portada del track.
     * El separador de directorios del sistema operativo (/ o \) se reemplaza
     * por '@' para que la ruta sea segura al usarla en una URL de la API.
     */
    private String portada;

    /** Duración del track formateada como String en formato "HH:mm:ss". */
    private String duracion;

    /** Número total de likes del track. */
    private Integer likes;

    /**
     * Fecha y hora en que el usuario realizó la descarga.
     * Es el dato adicional que justifica la existencia de este DTO
     * frente a reutilizar TrackResumen.
     */
    private LocalDateTime fechaDescarga;

    /**
     * Constructor que construye el DescargaData a partir de los datos necesarios.
     *
     * @param idTrack          ID del track
     * @param titulo           Título del track
     * @param nombreProductor  Nombre del productor
     * @param portada          Ruta de la portada con '@' como separador
     * @param duracion         Duración en formato "HH:mm:ss"
     * @param likes            Número total de likes del track
     * @param fechaDescarga    Fecha en que el usuario realizó la descarga
     */
    public DescargaData(Integer idTrack, String titulo, String nombreProductor,
            String portada, String duracion, Integer likes, LocalDateTime fechaDescarga) {
        this.idTrack = idTrack;
        this.titulo = titulo;
        this.nombreProductor = nombreProductor;
        this.portada = portada;
        this.duracion = duracion;
        this.likes = likes;
        this.fechaDescarga = fechaDescarga;
    }

    /**
     * Obtiene el identificador único del track.
     * @return El id del track
     */
    public Integer getIdTrack() {
        return idTrack;
    }

    /**
     * Establece el identificador único del track.
     * @param idTrack El nuevo id del track
     */
    public void setIdTrack(Integer idTrack) {
        this.idTrack = idTrack;
    }

    /**
     * Obtiene el título del track.
     * @return El título del track
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del track.
     * @param titulo El nuevo título
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el nombre del productor que subió el track.
     * @return El nombre del productor
     */
    public String getNombreProductor() {
        return nombreProductor;
    }

    /**
     * Establece el nombre del productor.
     * @param nombreProductor El nuevo nombre del productor
     */
    public void setNombreProductor(String nombreProductor) {
        this.nombreProductor = nombreProductor;
    }

    /**
     * Obtiene la ruta de la portada del track sanitizada para uso en URLs.
     * @return La ruta de la portada con '@' como separador
     */
    public String getPortada() {
        return portada;
    }

    /**
     * Establece la ruta de la portada.
     * @param portada La nueva ruta de la portada
     */
    public void setPortada(String portada) {
        this.portada = portada;
    }

    /**
     * Obtiene la duración del track en formato "HH:mm:ss".
     * @return La duración del track
     */
    public String getDuracion() {
        return duracion;
    }

    /**
     * Establece la duración del track.
     * @param duracion La nueva duración
     */
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    /**
     * Obtiene el número total de likes del track.
     * @return El número de likes
     */
    public Integer getLikes() {
        return likes;
    }

    /**
     * Establece el número de likes del track.
     * @param likes El nuevo número de likes
     */
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    /**
     * Obtiene la fecha en que el usuario realizó la descarga.
     * @return La fecha de la descarga
     */
    public LocalDateTime getFechaDescarga() {
        return fechaDescarga;
    }

    /**
     * Establece la fecha en que el usuario realizó la descarga.
     * @param fechaDescarga La nueva fecha de la descarga
     */
    public void setFechaDescarga(LocalDateTime fechaDescarga) {
        this.fechaDescarga = fechaDescarga;
    }
}