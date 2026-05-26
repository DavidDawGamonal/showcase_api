package com.EDLM.showcase.entities;

import java.time.LocalDate;

import com.EDLM.showcase.enums.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Representa la entidad de usuario en el sistema de showcase para productores
 * musicales. Esta clase mapea la tabla 'usuarios' en la base de datos y
 * contiene toda la información relevante de un usuario, incluyendo sus datos
 * personales y su rol en el sistema.
 * 
 * <p>
 * Los usuarios pueden ser de dos tipos según su {@link Rol}:
 * </p>
 * <ul>
 * <li><b>USUARIO</b>: Consumidor de contenido musical</li>
 * <li><b>PRODUCTOR</b>: Creador de contenido musical</li>
 * </ul>
 * 
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see com.EDLM.showcase.enums.Rol
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /**
     * Identificador único del usuario.
     * Este valor es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    /**
     * Nombre del usuario.
     * Este campo es obligatorio y no puede ser nulo en la base de datos.
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Apellido del usuario.
     * Este campo es opcional y puede ser nulo.
     */
    @Column(nullable = true)
    private String apellido;

    /**
     * Correo electrónico del usuario.
     * Debe ser único en toda la tabla y es obligatorio.
     * Se utiliza como identificador para el inicio de sesión.
     */
    @Column(unique = true, nullable = false)
    private String email;
    
    
    /**
     * Contraseña del usuario (almacenada como hash).
     * Se recomienda usar BCrypt u otro algoritmo de hashing antes de persistir.
     * La longitud de 255 es suficiente para cualquier hash estándar (BCrypt ~60 chars).
     */
    @Column(nullable = false, length = 255)
    private String password;


	/**
     * Fecha de alta del usuario en el sistema.
     * Se establece automáticamente a la fecha actual mediante {@link #onCreate()}.
     * No es necesario proporcionarla al crear un usuario.
     */
    @Column(name = "fec_alta", nullable = false)
    private LocalDate fecAlta;

    /**
     * Rol del usuario en el sistema.
     * Determina los permisos y funcionalidades disponibles.
     * Se almacena como String en la base de datos gracias a {@link Enumerated}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    /**
     * Constructor por defecto requerido por JPA.
     * Crea una instancia vacía de Usuario.
     */
    public Usuario() {
    }

    /**
     * Constructor completo para crear un usuario con todos los campos obligatorios.
     * La fecha de alta se establece automáticamente a la fecha actual.
     *
     * @param nombre   El nombre del usuario (no puede ser nulo)
     * @param email    El email del usuario (debe ser único)
     * @param password La contraseña del usuario (debe llegar ya hasheada)
     * @param rol      El rol del usuario en el sistema
     */
    public Usuario(String nombre,String apellido, String email, String password, Rol rol) {
        this.nombre = nombre;
        this.apellido=apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }
    
    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Método que se ejecuta automáticamente antes de persistir la entidad por primera vez.
     * Establece la fecha de alta a la fecha actual si no se ha proporcionado.
     */
    @PrePersist
    protected void onCreate() {
        if (fecAlta == null) {
            fecAlta = LocalDate.now();
        }
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return El ID del usuario
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el identificador único del usuario.
     *
     * @param idUsuario El nuevo ID para el usuario
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre El nuevo nombre para el usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del usuario.
     *
     * @return El apellido del usuario (puede ser null)
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del usuario.
     *
     * @param apellido El nuevo apellido para el usuario
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return El email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email El nuevo email para el usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Obtiene la contraseña hasheada del usuario.
     *
     * @return La contraseña del usuario (en formato hash)
     */
    public String getPassword() {
    	return password;
    }
    
    /**
     * Establece la contraseña del usuario.
     * IMPORTANTE: Esta contraseña debe llegar ya hasheada (ej: BCrypt).
     * Nunca almacenar contraseñas en texto plano.
     *
     * @param password La contraseña hasheada del usuario
     */
    public void setPassword(String password) {
    	this.password = password;
    }

    /**
     * Obtiene la fecha de alta del usuario.
     * Este valor se establece automáticamente al crear el usuario.
     *
     * @return La fecha de alta del usuario
     */
    public LocalDate getFecAlta() {
        return fecAlta;
    }

    /**
     * Establece la fecha de alta del usuario.
     * Normalmente no es necesario usar este método ya que se establece automáticamente.
     *
     * @param fecAlta La nueva fecha de alta para el usuario
     */
    public void setFecAlta(LocalDate fecAlta) {
        this.fecAlta = fecAlta;
    }

    /**
     * Obtiene el rol del usuario en el sistema.
     *
     * @return El rol del usuario ({@link Rol#USUARIO} o {@link Rol#PRODUCTOR})
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario en el sistema.
     *
     * @param rol El nuevo rol para el usuario
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Devuelve una representación en cadena del objeto Usuario.
     * Nota: la password NO se incluye por seguridad.
     * 
     * @return String con la información básica del usuario
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", fecAlta=" + fecAlta +
                ", rol=" + rol +
                '}';
    }
}
