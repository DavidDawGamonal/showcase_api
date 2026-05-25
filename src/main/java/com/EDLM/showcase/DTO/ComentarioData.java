package com.EDLM.showcase.DTO;

import java.util.Date;
import java.util.List;

/**
 * DTO (Data Transfer Object) para transferir los datos de un comentario al frontend.
 * Se usa en lugar de la entidad Comentario para evitar exponer datos sensibles
 * y evitar problemas de serialización con las relaciones lazy de JPA.
 *
 * <p>Contiene solo los campos necesarios para mostrar un comentario en la vista:</p>
 * <ul>
 *   <li>El id del comentario</li>
 *   <li>Los datos básicos del usuario que lo escribió ({@link DataUsuario})</li>
 *   <li>El contenido textual</li>
 *   <li>La fecha de publicación</li>
 *   <li>La lista de respuestas anidadas (puede estar vacía)</li>
 * </ul>
 *
 * @author Elvis David Lara Manrique
 * @version 1.0
 * @since 1.0
 * @see DataUsuario
 */
public class ComentarioData {

    /** Identificador único del comentario. */
    private Integer idComentario;
    
    /** Título del track sobre el que se realizó el comentario. */
    private String tituloTrack;

    /**
     * Datos básicos del usuario que realizó el comentario.
     * Se usa DataUsuario en lugar de Usuario para no exponer
     * campos sensibles como la contraseña.
     */
    private DataUsuario usuario;

    /** Contenido textual del comentario. */
    private String contenido;

    /** Fecha y hora en que se publicó el comentario. */
    private Date fecha;

    /**
     * Lista de respuestas anidadas a este comentario.
     * Puede ser una lista vacía si no tiene respuestas.
     */
    private List<ComentarioData> respuestas;

    /**
     * Obtiene el identificador único del comentario.
     * @return El id del comentario
     */
    public Integer getIdComentario() {
        return idComentario;
    }

    /**
     * Establece el identificador único del comentario.
     * @param idComentario El nuevo id del comentario
     */
    public void setIdComentario(Integer idComentario) {
        this.idComentario = idComentario;
    }
    
    /**
     * Obtiene el título del track sobre el que se realizó el comentario.
     * Se usa en el perfil del usuario para mostrar a qué track pertenece cada comentario.
     * @return El título del track
     */
    public String getTituloTrack() {
        return tituloTrack;
    }

    /**
     * Establece el título del track sobre el que se realizó el comentario.
     * @param tituloTrack El título del track a asignar
     */
    public void setTituloTrack(String tituloTrack) {
        this.tituloTrack = tituloTrack;
    }

    /**
     * Obtiene los datos del usuario que realizó el comentario.
     * @return El objeto DataUsuario con los datos básicos del usuario
     */
    public DataUsuario getUsuario() {
        return usuario;
    }

    /**
     * Establece los datos del usuario que realizó el comentario.
     * @param usuario El objeto DataUsuario a asignar
     */
    public void setUsuario(DataUsuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el contenido textual del comentario.
     * @return El contenido del comentario
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Establece el contenido textual del comentario.
     * @param contenido El nuevo contenido del comentario
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Obtiene la fecha de publicación del comentario.
     * @return La fecha del comentario
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de publicación del comentario.
     * @param fecha La nueva fecha del comentario
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene la lista de respuestas anidadas a este comentario.
     * @return Lista de ComentarioData con las respuestas
     */
    public List<ComentarioData> getRespuestas() {
        return respuestas;
    }

    /**
     * Establece la lista de respuestas anidadas a este comentario.
     * @param respuestas La nueva lista de respuestas
     */
    public void setRespuestas(List<ComentarioData> respuestas) {
        this.respuestas = respuestas;
    }
    
}
