package com.EDLM.showcase.services;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.enums.Rol;
import com.EDLM.showcase.repositories.UsuarioRepository;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los usuarios.
 * Proporciona operaciones de autenticación y registro, incluyendo el cifrado de
 * contraseñas con BCrypt.
 *
 * @author Elvis David Lara Manrique
 * @version 1.0
 * @since 1.0
 * @see Usuario
 * @see UsuarioRepository
 */
@Service
public class UsuarioService {

	/** Repositorio JPA para acceder a los datos de los usuarios. */
	private UsuarioRepository usuarioRepository;

	/**
	 * Herramienta de cifrado de contraseñas. BCrypt genera un hash distinto cada
	 * vez (salt aleatorio incluido), por lo que no se puede revertir — solo se
	 * puede verificar con matches().
	 */
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/**
	 * Constructor que inyecta el repositorio de usuarios. Spring lo resuelve
	 * automáticamente al arrancar la aplicación.
	 *
	 * @param usuarioRepository El repositorio JPA de usuarios
	 */
	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	/**
	 * Comprueba las credenciales de un usuario para el inicio de sesión. Busca al
	 * usuario por email y verifica que la contraseña proporcionada coincide con el
	 * hash almacenado en base de datos usando BCrypt.
	 *
	 * @param usuario Objeto Usuario con el email y la contraseña en texto plano a
	 *                verificar
	 * @return El objeto Usuario completo si las credenciales son correctas, null si
	 *         no lo son
	 */
	public Usuario checkUser(Usuario usuario) {
		Usuario loginUser = null;
		// Busca al usuario en la BD solo por email
		Optional<Usuario> bdUsuario = usuarioRepository.findByEmail(usuario.getEmail());

		// Si existe el email y la contraseña coincide con el hash, devuelve el usuario
		if (bdUsuario.isPresent()
				&& bdUsuario.map(u -> passwordEncoder.matches(usuario.getPassword(), u.getPassword())).get()) {
			loginUser = bdUsuario.get();
		}
		return loginUser;
	}

	/**
	 * Registra un nuevo usuario en el sistema. Cifra la contraseña con BCrypt antes
	 * de persistirla — nunca se almacena la contraseña en texto plano.
	 *
	 * @param nombre   Nombre del nuevo usuario
	 * @param apellido Apellido del nuevo usuario
	 * @param email    Email del nuevo usuario (debe ser único en la BD)
	 * @param password Contraseña en texto plano — se cifrará antes de guardarla
	 * @param rol      Rol del nuevo usuario (USUARIO o PRODUCTOR)
	 * @return true si el usuario se creó correctamente, false si hubo algún error
	 */
	public boolean createUser(String nombre, String apellido, String email, String password, Rol rol) {
		boolean resultado;
		try {
			// Cifra la contraseña antes de guardarla — jamás se guarda en texto plano
			String passwordCifrada = passwordEncoder.encode(password);
			Usuario usuario = new Usuario(nombre, apellido, email, passwordCifrada, rol);
			Usuario usuarioGuardado = usuarioRepository.save(usuario);
			// Si el id generado es distinto de 0, la inserción fue correcta
			if (usuarioGuardado.getIdUsuario() != 0) {
				resultado = true;
			} else {
				resultado = false;
			}
		} catch (Exception e) {
			resultado = false;
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * Elimina un usuario del sistema por su id. Gracias a las FK con ON DELETE
	 * CASCADE definidas en la BD, se eliminan automáticamente todos sus datos
	 * asociados: tracks, likes, comentarios, descargas y playlists.
	 *
	 * @param idUsuario ID del usuario a eliminar
	 */
	public void eliminarUsuario(Integer idUsuario) {
		usuarioRepository.deleteById(idUsuario);
	}
}
