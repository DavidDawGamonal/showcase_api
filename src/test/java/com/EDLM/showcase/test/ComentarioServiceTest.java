package com.EDLM.showcase.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.EDLM.showcase.entities.Comentario;
import com.EDLM.showcase.entities.Track;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.enums.Rol;
import com.EDLM.showcase.repositories.ComentarioRepository;
import com.EDLM.showcase.repositories.TrackRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;
import com.EDLM.showcase.services.ComentarioService;

/**
 * Tests unitarios para {@link ComentarioService}.
 *
 * <p>
 * Se mockean los tres repositorios que usa el service. Se verifican los casos
 * principales de crearComentario, responderComentario, contarComentarios,
 * contarComentariosByProductor y contarComentariosByUsuario. Los métodos
 * getComentariosTrack y getComentariosByUsuario no se testean unitariamente por
 * la complejidad de construir la jerarquía de entidades necesaria.
 * </p>
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see ComentarioService
 * @see ComentarioRepository
 */
@ExtendWith(MockitoExtension.class)
public class ComentarioServiceTest {

	/** Mock del repositorio de comentarios. */
	@Mock
	private ComentarioRepository comentarioRepository;

	/** Mock del repositorio de tracks. */
	@Mock
	private TrackRepository trackRepository;

	/** Mock del repositorio de usuarios. */
	@Mock
	private UsuarioRepository usuarioRepository;

	/**
	 * Instancia del service bajo test con sus dependencias mockeadas inyectadas.
	 */
	@InjectMocks
	private ComentarioService comentarioService;

	// ── Helpers ───────────────────────────────────────────────────────────────

	/**
	 * Crea un {@link Usuario} de prueba con los datos mínimos necesarios.
	 */
	private Usuario crearUsuario(int id, String nombre) {
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(id);
		usuario.setNombre(nombre);
		usuario.setApellido("Apellido");
		usuario.setEmail("test@email.com");
		usuario.setRol(Rol.USUARIO);
		return usuario;
	}

	/**
	 * Crea un {@link Track} de prueba con los datos mínimos necesarios.
	 */
	private Track crearTrack(int id, String titulo) {
		Track track = new Track();
		track.setIdTrack(id);
		track.setTitulo(titulo);
		return track;
	}

	/**
	 * Crea un {@link Comentario} de prueba con los datos mínimos necesarios.
	 */
	private Comentario crearComentario(int id, Usuario usuario, Track track, String contenido) {
		Comentario comentario = new Comentario(usuario, track, contenido);
		comentario.setIdComent(id);
		comentario.setFecComent(LocalDateTime.now());
		return comentario;
	}

	// ── crearComentario ───────────────────────────────────────────────────────

	/**
	 * Verifica que {@link ComentarioService#crearComentario(int, int, String)}
	 * persiste el comentario y devuelve {@code true}.
	 */
	@Test
	void crearComentario_guardaYDevuelveTrue() {
	    // GIVEN
	    Usuario usuario = crearUsuario(1, "Manuel");
	    Track track = crearTrack(10, "rapBlues");

	    lenient().when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
	    lenient().when(trackRepository.findById(any())).thenReturn(Optional.of(track));

	    // WHEN
	    boolean resultado = comentarioService.crearComentario(10, 1, "Buen track");

	    // THEN
	    assertTrue(resultado);
	    verify(comentarioRepository, times(1)).save(any(Comentario.class));
	}

	// ── responderComentario ───────────────────────────────────────────────────

	/**
	 * Verifica que {@link ComentarioService#responderComentario(int, int, String)}
	 * persiste la respuesta y devuelve {@code true} cuando el comentario padre
	 * existe.
	 */
	@Test
	void responderComentario_guardaYDevuelveTrue_cuandoPadreExiste() {
	    // GIVEN
	    Usuario usuario = crearUsuario(1, "Manuel");
	    Track track = crearTrack(10, "rapBlues");
	    Comentario padre = crearComentario(5, usuario, track, "Comentario original");

	    lenient().when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
	    when(comentarioRepository.findById(any())).thenReturn(Optional.of(padre));

	    // WHEN
	    boolean resultado = comentarioService.responderComentario(5, 1, "Gracias!");

	    // THEN
	    assertTrue(resultado);
	    verify(comentarioRepository, times(1)).save(any(Comentario.class));
	}

	/**
	 * Verifica que {@link ComentarioService#responderComentario(int, int, String)}
	 * devuelve {@code false} cuando el comentario padre no existe.
	 */
	@Test
	void responderComentario_devuelveFalse_cuandoPadreNoExiste() {
		// GIVEN
		when(comentarioRepository.findById(any())).thenReturn(Optional.empty());

		// WHEN
		boolean resultado = comentarioService.responderComentario(99, 1, "Respuesta");

		// THEN
		assertFalse(resultado);
		verify(comentarioRepository, never()).save(any());
	}

	// ── contarComentarios ─────────────────────────────────────────────────────

	/**
	 * Verifica que {@link ComentarioService#contarComentarios(Integer)} delega
	 * correctamente en el repositorio y devuelve el total de comentarios.
	 */
	@Test
	void contarComentarios_devuelveTotal() {
		// GIVEN
		when(comentarioRepository.countByTrackIdTrack(10)).thenReturn(5);

		// WHEN
		Integer resultado = comentarioService.contarComentarios(10);

		// THEN
		assertEquals(5, resultado);
	}

	// ── contarComentariosByProductor ──────────────────────────────────────────

	/**
	 * Verifica que {@link ComentarioService#contarComentariosByProductor(Integer)}
	 * delega correctamente en el repositorio y devuelve el total de comentarios.
	 */
	@Test
	void contarComentariosByProductor_devuelveTotal() {
		// GIVEN
		when(comentarioRepository.contarComentariosByProductor(1)).thenReturn(12);

		// WHEN
		Integer resultado = comentarioService.contarComentariosByProductor(1);

		// THEN
		assertEquals(12, resultado);
	}

	// ── contarComentariosByUsuario ────────────────────────────────────────────

	/**
	 * Verifica que {@link ComentarioService#contarComentariosByUsuario(Integer)}
	 * delega correctamente en el repositorio y devuelve el total de comentarios.
	 */
	@Test
	void contarComentariosByUsuario_devuelveTotal() {
		// GIVEN
		when(comentarioRepository.countByUsuarioIdUsuario(1)).thenReturn(7);

		// WHEN
		Integer resultado = comentarioService.contarComentariosByUsuario(1);

		// THEN
		assertEquals(7, resultado);
	}
}