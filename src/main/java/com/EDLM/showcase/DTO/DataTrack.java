package com.EDLM.showcase.DTO;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

/**
 * DTO (Data Transfer Object) que representa los datos de un track enviados
 * desde el frontend al backend en la petición de subida.
 * Contiene únicamente los campos que el usuario introduce en el formulario;
 * el resto (fecha de subida, reproducciones, portada, ruta) los gestiona
 * el backend internamente.
 *
 * <p>Los campos comentados corresponden a datos que gestiona el backend
 * y que no deben enviarse desde el frontend.</p>
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
public class DataTrack {

    /** Título de la pista musical. */
    private String titulo;

    /** Género musical de la pista. */
    private String genero;

    /** Duración de la pista en formato HH:mm:ss. */
    private LocalTime duracion;

    /** Descripción opcional de la pista. */
    private String descripcion;

    /**
     * Archivo de audio instrumental subido desde el formulario.
     * Recibido como multipart/form-data desde el frontend.
     */
    private MultipartFile instrumental;

    /**
     * Imagen de portada subida desde el formulario.
     * Nullable — si es null el backend asigna una portada por defecto.
     */
    private MultipartFile portada;

    /** ID del usuario (productor) que sube la pista. */
    private int idUsuario;

    // Campos gestionados por el backend — no se reciben desde el frontend:
    // private LocalDateTime fecSubida;
    // private Integer contReprod;
    // private Integer idTrack;
    // private String ruta;

    /**
     * Constructor con los campos enviados desde el formulario de subida.
     * Los parámetros comentados corresponden a campos gestionados por el backend.
     *
     * @param titulo      Título de la pista musical
     * @param genero      Género musical de la pista
     * @param duracion    Duración de la pista en formato HH:mm:ss
     * @param fecSubida   Fecha de subida — gestionada por el backend, no se usa
     * @param contReprod  Contador de reproducciones — gestionado por el backend, no se usa
     * @param descripcion Descripción opcional de la pista
     * @param portada     Ruta de portada — gestionada por el backend, no se usa
     * @param ruta        Ruta del instrumental — gestionada por el backend, no se usa
     * @param usuario     ID del usuario (productor) que sube la pista
     * @param idTrack     ID del track — gestionado por el backend, no se usa
     */
    public DataTrack(String titulo, String genero, LocalTime duracion, LocalDateTime fecSubida, Integer contReprod,
            String descripcion, String portada, String ruta, int usuario, Integer idTrack) {
        this.titulo = titulo;
        this.genero = genero;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.idUsuario = usuario;
        // Los demás parámetros los gestiona el backend internamente
    }

    /**
     * Constructor vacío requerido por Spring para el binding de @ModelAttribute.
     */
    public DataTrack() {
    }

    /**
     * Obtiene la imagen de portada del track.
     * @return El archivo de portada, o null si no se ha subido ninguna
     */
    public MultipartFile getPortada() {
        return portada;
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
     * Obtiene la duración del track.
     * @return La duración en formato LocalTime
     */
    public LocalTime getDuracion() {
        return duracion;
    }

    /**
     * Obtiene la descripción del track.
     * @return La descripción del track, puede ser null
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene el ID del usuario (productor) que sube el track.
     * @return El ID del usuario
     */
    public int getUsuario() {
        return idUsuario;
    }

    /**
     * Obtiene el archivo de audio instrumental del track.
     * @return El archivo de audio
     */
    public MultipartFile getInstrumental() {
        return instrumental;
    }

    /**
     * Establece el archivo de audio instrumental del track.
     * @param instrumental El archivo de audio a asignar
     */
    public void setInstrumental(MultipartFile instrumental) {
        this.instrumental = instrumental;
    }

    /**
     * Establece la imagen de portada del track.
     * @param portada El archivo de portada a asignar
     */
    public void setPortada(MultipartFile portada) {
        this.portada = portada;
    }

    /**
     * Establece el título del track.
     * @param titulo El nuevo título
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Establece el género musical del track.
     * @param genero El nuevo género
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Establece la duración del track.
     * @param duracion La nueva duración
     */
    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }

    /**
     * Establece la descripción del track.
     * @param descripcion La nueva descripción
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Establece el ID del usuario (productor) que sube el track.
     * @param idUsuario El nuevo ID de usuario
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}