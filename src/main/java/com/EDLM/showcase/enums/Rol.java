package com.EDLM.showcase.enums;

/**
 * Enumeración que define los roles disponibles en el sistema.
 * Determina los permisos y funcionalidades a las que tiene acceso cada usuario.
 *
 * <p>Roles disponibles:</p>
 * <ul>
 *   <li><b>USUARIO</b>: consumidor de contenido. Puede escuchar tracks,
 *       comentar y dar likes.</li>
 *   <li><b>PRODUCTOR</b>: creador de contenido. Tiene los mismos permisos
 *       que USUARIO y además puede subir y gestionar sus propios tracks.</li>
 * </ul>
 *
 * <p>Se almacena como String en la base de datos gracias a
 * {@code @Enumerated(EnumType.STRING)} en la entidad {@link Usuario}.</p>
 *
 * @author Elvis David Lara Manrique
 * @version 1.0
 * @since 1.0
 * @see Usuario
 */
public enum Rol {

    /** Consumidor de contenido musical. Puede escuchar, comentar y dar likes. */
    USUARIO,

    /** Creador de contenido musical. Puede subir y gestionar tracks además de las acciones de USUARIO. */
    PRODUCTOR
}
