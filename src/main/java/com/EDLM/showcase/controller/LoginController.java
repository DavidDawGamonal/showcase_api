package com.EDLM.showcase.controller;

import org.springframework.web.bind.annotation.*;

import com.EDLM.showcase.DTO.DataUsuario;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.services.UsuarioService;

/**
 * Controlador REST que gestiona la autenticación y el registro de usuarios.
 * Base URL: /
 *
 * <p>Pendiente de mejora:</p>
 * <ul>
 *   <li>checkLogin debería ser POST para no exponer credenciales en la URL</li>
 *   <li>Renombrar a LoginController (ya corregido) y separar en /api/login y /api/registro</li>
 *   <li>Ajustar @CrossOrigin al puerto del frontend real en producción</li>
 * </ul>
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
// @CrossOrigin(origins = "http://localhost:8080") // restringido al puerto del frontend — descomentar en producción
@CrossOrigin(origins = "*")
@RestController
public class LoginController {

    private UsuarioService usuarioService;

    /**
     * Constructor con inyección de dependencias del servicio de usuarios.
     *
     * @param usuarioService Servicio de usuarios
     */
    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Comprueba las credenciales del usuario y devuelve el objeto Usuario si son correctas.
     * Devuelve null si el email no existe o la contraseña no coincide.
     *
     * <p>PENDIENTE: cambiar a POST para no exponer email y password en la URL.</p>
     *
     * @param email    Email del usuario
     * @param password Contraseña en texto plano — se compara con el hash BCrypt en el service
     * @return El Usuario si las credenciales son válidas, null en caso contrario
     */
    @GetMapping("/checkLogin")
    // @PostMapping("/checkLogin") // pendiente de migrar a POST
    public Usuario checkLogin(@RequestParam String email, @RequestParam String password) {
        Usuario usuario = new Usuario(email, password);
        Usuario resultado = usuarioService.checkUser(usuario);
        return resultado;
    }
    
    /*
     * @GetMapping("/checkLogin")
    // @PostMapping("/checkLogin") // pendiente de migrar a POST
    public Usuario checkLogin(@ModelAttribute DataUsuario usuarioLogado) {
        Usuario usuario = new Usuario(usuarioLogado.getEmail(), usuarioLogado.getPassword());
        Usuario resultado = usuarioService.checkUser(usuario);
        return resultado;
    }
     * */

    /**
     * Registra un nuevo usuario en el sistema.
     * Recibe los datos del usuario como JSON en el cuerpo de la petición.
     * La contraseña se hashea con BCrypt en el service antes de persistirla.
     *
     * @param usuario DTO con los datos del nuevo usuario (nombre, apellido, email, password, rol)
     * @return true si el usuario se ha creado correctamente, false si ya existe o hay un error
     */
    @PostMapping("/createUser")
    public boolean createUser(@RequestBody DataUsuario usuario) {
        return usuarioService.createUser(usuario.getNombre(), usuario.getApellido(), usuario.getEmail(),
                usuario.getPassword(), usuario.getRol());
    }
    
    /**
     * Elimina la cuenta de un usuario del sistema.
     * Gracias a las FK con ON DELETE CASCADE, se eliminan también
     * todos sus datos asociados (tracks, likes, comentarios, descargas, playlists).
     *
     * @param idUsuario ID del usuario a eliminar
     */
    @DeleteMapping("/eliminarCuenta")
    public void eliminarCuenta(@RequestParam Integer idUsuario) {
        usuarioService.eliminarUsuario(idUsuario);
    }
}