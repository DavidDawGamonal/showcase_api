package com.EDLM.showcase.entities;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
 * Representa la entidad de track (pista musical) en el sistema de showcase para
 * productores musicales. Esta clase mapea la tabla 'tracks' en la base de datos
 * y contiene toda la información relevante de una pista musical subida por un
 * productor.
 * 
 * <p>
 * Características principales de un track:
 * </p>
 * <ul>
 * <li>Cada track pertenece a un único {@link Usuario} (productor)</li>
 * <li>El título debe ser único por usuario (no puede haber dos tracks con el
 * mismo título para un mismo productor)</li>
 * <li>Almacena información como género, duración, fecha de subida y número de
 * reproducciones</li>
 * </ul>
 * 
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see com.EDLM.showcase.entities.Usuario
 */
@Entity
@Table(name = "tracks", uniqueConstraints = @UniqueConstraint(name = "unique_titulo_usuario", columnNames = { "titulo",
		"id_usuario" }))
public class Track {

	/**
	 * Identificador único del track. Este valor es generado automáticamente por la
	 * base de datos.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_track")
	private Integer idTrack;

	/**
	 * Título de la pista musical. Este campo es obligatorio y no puede ser nulo. La
	 * combinación título + id_usuario debe ser única.
	 */
	@Column(nullable = false, length = 100)
	private String titulo;

	/**
	 * Género musical de la pista (ej: Rock, Pop, Electrónica, etc.). Este campo es
	 * obligatorio y no puede ser nulo.
	 */
	@Column(nullable = false, length = 50)
	private String genero;

	/**
	 * Duración de la pista musical. Se almacena como tipo TIME en la base de datos
	 * (HH:MM:SS). Este campo es obligatorio.
	 */
	@Column(nullable = false)
	private LocalTime duracion;

	/**
	 * Fecha y hora de subida del track. Por defecto se establece la fecha y hora
	 * actual del sistema. Este campo es obligatorio.
	 */
	@Column(name = "fec_subida", nullable = false)
	private LocalDateTime fecSubida;

	/**
	 * Número total de reproducciones de la pista. Se inicializa a 0 cuando se crea
	 * un nuevo track. Este campo es obligatorio.
	 */
	@Column(name = "cont_reprod", nullable = false)
	private Integer contReprod = 0;

	/**
	 * Descripción detallada de la pista musical. Puede incluir información sobre la
	 * producción, instrumentación, inspiración o cualquier detalle relevante sobre
	 * la pista. Este campo es opcional y puede ser nulo.
	 */
	@Column(name = "descripcion", nullable = true, length = 500)
	private String descripcion;

	/**
	 * URL o ruta de la imagen de portada del track. Almacena la ubicación del
	 * archivo de imagen que representa visualmente la pista musical (formato JPG,
	 * PNG, etc.). Este campo es opcional, si no se proporciona se usará una imagen
	 * por defecto en la interfaz.
	 */
	@Column(name = "portada", nullable = true, length = 500)
	private String portada;

	/**
	 * Ruta de almacenamiento del archivo de audio. Contiene la ubicación física o
	 * URL donde se encuentra el archivo de audio (MP3, WAV, etc.). Este campo es
	 * obligatorio ya que sin él no se puede reproducir la pista. La ruta puede ser
	 * relativa al servidor o una URL externa.
	 */
	@Column(name = "ruta", nullable = false, length = 500)
	private String ruta;

	/**
	 * Usuario (productor) que ha subido este track. Establece una relación
	 * muchos-a-uno con la entidad Usuario. La clave foránea id_usuario referencia
	 * la tabla usuarios.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(name = "FK_id_usuario_usuario"))
	@JsonIgnore // Evita la recursión infinita al serializar JSON
	private Usuario usuario;

	/**
	 * Constructor por defecto requerido por JPA. Crea una instancia vacía de Track.
	 */
	public Track() {
	}

	/**
	 * Constructor para crear un track con los campos esenciales. La fecha de subida
	 * se establece automáticamente a la fecha/hora actual y las reproducciones
	 * comienzan en 0.
	 * 
	 * @param titulo   El título del track (no puede ser nulo)
	 * @param genero   El género musical del track
	 * @param duracion La duración del track
	 * @param usuario  El usuario (productor) que sube el track
	 */
	public Track(String titulo, String genero, LocalTime duracion, String descripcion, String portada, String ruta,
			Usuario usuario) {
		this.titulo = titulo;
		this.genero = genero;
		this.duracion = duracion;
		this.descripcion = descripcion;
		this.portada = portada;
		this.ruta = ruta;
		this.usuario = usuario;
		this.fecSubida = LocalDateTime.now();
		this.contReprod = 0;
	}

	/**
	 * Método que se ejecuta automáticamente antes de persistir la entidad por
	 * primera vez. Establece la fecha de subida a la fecha/hora actual si no se ha
	 * proporcionado.
	 */
	@PrePersist
	protected void onCreate() {
		if (fecSubida == null) {
			fecSubida = LocalDateTime.now();
		}
	}

	/**
	 * Obtiene el identificador único del track.
	 * 
	 * @return El ID del track
	 */
	public Integer getIdTrack() {
		return idTrack;
	}

	/**
	 * Establece el identificador único del track.
	 * 
	 * @param idTrack El nuevo ID para el track
	 */
	public void setIdTrack(Integer idTrack) {
		this.idTrack = idTrack;
	}

	/**
	 * Obtiene el título del track.
	 * 
	 * @return El título del track
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * Establece el título del track.
	 * 
	 * @param titulo El nuevo título para el track
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * Obtiene el género musical del track.
	 * 
	 * @return El género del track
	 */
	public String getGenero() {
		return genero;
	}

	/**
	 * Establece el género musical del track.
	 * 
	 * @param genero El nuevo género para el track
	 */
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/**
	 * Obtiene la duración del track.
	 * 
	 * @return La duración del track (formato HH:MM:SS)
	 */
	public LocalTime getDuracion() {
		return duracion;
	}

	/**
	 * Establece la duración del track.
	 * 
	 * @param duracion La nueva duración para el track
	 */
	public void setDuracion(LocalTime duracion) {
		this.duracion = duracion;
	}

	/**
	 * Obtiene la descripción del track.
	 * 
	 * @return La descripción del track, puede ser null si no se proporcionó
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Establece la descripción del track.
	 * 
	 * @param descripcion La nueva descripción para el track (puede ser null)
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Obtiene la ruta de la imagen de portada del track.
	 * 
	 * @return La URL o ruta de la portada, puede ser null si no se proporcionó
	 */
	public String getPortada() {
		return portada;
	}

	/**
	 * Establece la ruta de la imagen de portada del track.
	 * 
	 * @param portada La nueva ruta de la portada (puede ser null)
	 */
	public void setPortada(String portada) {
		this.portada = portada;
	}

	/**
	 * Obtiene la ruta del archivo de audio del track.
	 * 
	 * @return La ruta del archivo de audio (nunca será null)
	 */
	public String getRuta() {
		return ruta;
	}

	/**
	 * Establece la ruta del archivo de audio del track.
	 * 
	 * @param ruta La nueva ruta del archivo de audio (no puede ser null)
	 * @throws IllegalArgumentException si la ruta es null o está vacía
	 */
	public void setRuta(String ruta) {
		if (ruta == null || ruta.trim().isEmpty()) {
			throw new IllegalArgumentException("La ruta del archivo de audio es obligatoria");
		}
		this.ruta = ruta;
	}

	/**
	 * Obtiene la fecha y hora de subida del track.
	 * 
	 * @return La fecha y hora de subida
	 */
	public LocalDateTime getFecSubida() {
		return fecSubida;
	}

	/**
	 * Establece la fecha y hora de subida del track. Normalmente no es necesario
	 * usar este método ya que se establece automáticamente.
	 * 
	 * @param fecSubida La nueva fecha y hora de subida
	 */
	public void setFecSubida(LocalDateTime fecSubida) {
		this.fecSubida = fecSubida;
	}

	/**
	 * Obtiene el número de reproducciones del track.
	 * 
	 * @return El contador de reproducciones
	 */
	public Integer getContReprod() {
		return contReprod;
	}

	/**
	 * Establece el número de reproducciones del track.
	 * 
	 * @param contReprod El nuevo valor para el contador de reproducciones
	 */
	public void setContReprod(Integer contReprod) {
		this.contReprod = contReprod;
	}

	/**
	 * Incrementa en uno el contador de reproducciones del track. Método útil para
	 * cuando se reproduce una pista.
	 * 
	 * @return El nuevo valor del contador después del incremento
	 */
	public Integer incrementarReproducciones() {
		this.contReprod++;
		return this.contReprod;
	}

	/**
	 * Obtiene el usuario (productor) que subió este track.
	 * 
	 * @return El usuario propietario del track
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Establece el usuario (productor) propietario de este track.
	 * 
	 * @param usuario El nuevo usuario propietario
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Devuelve una representación en cadena del objeto Track.
	 * 
	 * @return String con la información básica del track
	 */
	@Override
	public String toString() {
		return "Track{" + "idTrack=" + idTrack + ", titulo='" + titulo + '\'' + ", genero='" + genero + '\''
				+ ", duracion=" + duracion + ", fecSubida=" + fecSubida + ", contReprod=" + contReprod + ", usuario="
				+ (usuario != null ? usuario.getIdUsuario() : null) + '}';
	}
}