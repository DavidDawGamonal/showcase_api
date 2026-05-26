package com.EDLM.showcase.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.enums.Rol;
import com.EDLM.showcase.repositories.UsuarioRepository;
import com.EDLM.showcase.services.UsuarioService;

/**
 * Tests unitarios para {@link UsuarioService}.
 *
 * <p>
 * Se usa {@link ExtendWith} con {@link MockitoExtension} para inicializar los
 * mocks automáticamente sin necesidad de llamar a
 * {@code MockitoAnnotations.openMocks(this)} en un {@code @BeforeEach}.
 * </p>
 *
 * <p>
 * <b>Nota sobre BCryptPasswordEncoder:</b> el mock de
 * {@code BCryptPasswordEncoder} declarado con {@code @Mock} no se usa
 * efectivamente en estos tests. El motivo es que {@link UsuarioService}
 * instancia su propio {@code BCryptPasswordEncoder} internamente (no lo recibe
 * por constructor), por lo que {@code @InjectMocks} no puede sustituirlo por el
 * mock. Como solución, se crea una instancia real de
 * {@code BCryptPasswordEncoder} en el propio test para generar el hash, y se
 * deja que el service use el suyo interno. El mock queda declarado pero sin
 * efecto — puede eliminarse sin consecuencias.
 * </p>
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see UsuarioService
 * @see UsuarioRepository
 */
@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

	/**
	 * Mock del repositorio de usuarios — sustituye la BD real durante los tests.
	 */
	@Mock
	private UsuarioRepository usuarioRepository;

	/**
	 * Mock de BCryptPasswordEncoder declarado para inyección, pero sin efecto real.
	 * Ver nota en el Javadoc de la clase.
	 */
	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Instancia del service bajo test con sus dependencias mockeadas inyectadas.
	 */
	@InjectMocks
	private UsuarioService usuarioService;

	/**
	 * Verifica que {@link UsuarioService#checkUser(Usuario)} devuelve el usuario
	 * completo cuando el email existe en la base de datos y la contraseña en texto
	 * plano coincide con el hash BCrypt almacenado.
	 *
	 * <p>
	 * El hash se genera en el propio test con una instancia real de
	 * {@link BCryptPasswordEncoder} para garantizar que el service pueda
	 * verificarlo correctamente con su encoder interno.
	 * </p>
	 */
	@Test
	void checkUserUsuarioYPasswordCorrectos() {
		// GIVEN — preparación de datos de entrada y comportamiento esperado del
		// repositorio
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String passwordPlana = "123";
		String passwordHasheada = encoder.encode(passwordPlana);

		// Usuario con las credenciales que introduce el usuario en el formulario de
		// login
		Usuario usuario = new Usuario();
		usuario.setEmail("test@email.com");
		usuario.setPassword(passwordPlana);

		// Usuario que simula el registro almacenado en la base de datos
		Usuario usuarioDevuelto = new Usuario();
		usuarioDevuelto.setPassword(passwordHasheada);
		usuarioDevuelto.setEmail("test@email.com");
		usuarioDevuelto.setNombre("Manuel");
		usuarioDevuelto.setApellido("García");
		usuarioDevuelto.setRol(Rol.PRODUCTOR);

		when(usuarioRepository.findByEmail("test@email.com")).thenReturn(Optional.of(usuarioDevuelto));

		// WHEN — ejecución del método bajo test
		Usuario actual = usuarioService.checkUser(usuario);

		// THEN — verificación del resultado
		assertNotNull(actual);
		assertEquals(usuarioDevuelto.getEmail(), actual.getEmail());
		assertEquals(usuarioDevuelto.getNombre(), actual.getNombre());
		assertEquals(usuarioDevuelto.getApellido(), actual.getApellido());
		assertEquals(usuarioDevuelto.getRol(), actual.getRol());
	}

	/**
	 * Verifica que {@link UsuarioService#checkUser(Usuario)} devuelve {@code null}
	 * cuando el email no existe en la base de datos.
	 */
	@Test
	void checkUserEmailNoExiste() {
		// GIVEN
		Usuario usuario = new Usuario();
		usuario.setEmail("noexiste@email.com");
		usuario.setPassword("123");

		when(usuarioRepository.findByEmail("noexiste@email.com")).thenReturn(Optional.empty());

		// WHEN
		Usuario actual = usuarioService.checkUser(usuario);

		// THEN
		assertNull(actual);
	}

	/**
	 * Verifica que {@link UsuarioService#checkUser(Usuario)} devuelve {@code null}
	 * cuando el email existe pero la contraseña no coincide con el hash almacenado.
	 */
	@Test
	void checkUserPasswordIncorrecta() {
		// GIVEN
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String passwordHasheada = encoder.encode("passwordCorrecta");

		Usuario usuario = new Usuario();
		usuario.setEmail("test@email.com");
		usuario.setPassword("passwordIncorrecta");

		Usuario usuarioDevuelto = new Usuario();
		usuarioDevuelto.setEmail("test@email.com");
		usuarioDevuelto.setPassword(passwordHasheada);

		when(usuarioRepository.findByEmail("test@email.com")).thenReturn(Optional.of(usuarioDevuelto));

		// WHEN
		Usuario actual = usuarioService.checkUser(usuario);

		// THEN
		assertNull(actual);
	}
}