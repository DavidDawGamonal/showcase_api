package com.EDLM.showcase.DTO;

import com.EDLM.showcase.enums.Rol;

/**
 * DTO (Data Transfer Object) para transferir los datos básicos de un usuario al frontend.
 * Se usa en lugar de la entidad Usuario para evitar exponer datos innecesarios
 * en las respuestas de la API.
 *
 * <p>PENDIENTE: Este DTO expone el campo password, lo que supone un riesgo de seguridad.
 * Hay que eliminar ese campo y su getter/setter en una revisión futura.</p>
 *
 * @author Elvis David Lara Manrique
 * @version 1.0
 * @since 1.0
 * @see Rol
 */
public class DataUsuario {

    /** Apellido del usuario. */
    private String apellido;

    /** Correo electrónico del usuario. */
    private String email;

    /**
     * Contraseña del usuario.
     * PENDIENTE: este campo no debería estar en un DTO — expone datos sensibles al frontend.
     * Eliminar en una revisión futura.
     */
    private String password;

    /** Rol del usuario en el sistema (USUARIO o PRODUCTOR). */
    private Rol rol;

    /** Nombre del usuario. */
    private String nombre;

    /**
     * Constructor completo de DataUsuario.
     * PENDIENTE: el parámetro password no debería incluirse en el DTO.
     *
     * @param nombre   Nombre del usuario
     * @param apellido Apellido del usuario
     * @param email    Email del usuario
     * @param password Contraseña del usuario (PENDIENTE: eliminar)
     * @param rol      Rol del usuario en el sistema
     */
    public DataUsuario(String nombre, String apellido, String email, String password, Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return El nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     * @param nombre El nuevo nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del usuario.
     * @return El apellido del usuario
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del usuario.
     * @param apellido El nuevo apellido del usuario
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el email del usuario.
     * @return El email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del usuario.
     * @param email El nuevo email del usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     * PENDIENTE: eliminar este getter — no debe exponerse en un DTO.
     * @return La contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * PENDIENTE: eliminar este setter — no debe exponerse en un DTO.
     * @param password La nueva contraseña del usuario
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el rol del usuario en el sistema.
     * @return El rol del usuario
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario en el sistema.
     * @param rol El nuevo rol del usuario
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
