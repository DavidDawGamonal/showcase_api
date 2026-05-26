package com.EDLM.showcase.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.EDLM.showcase.entities.Like;
import com.EDLM.showcase.entities.Track;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.repositories.LikeRepository;
import com.EDLM.showcase.repositories.TrackRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;
import com.EDLM.showcase.services.LikeService;

/**
 * Tests unitarios para {@link LikeService}.
 *
 * <p>
 * Se mockean los tres repositorios que usa el service. Se verifican los casos
 * principales de toggleLike (dar y quitar), tieneLike, contarLikes,
 * contarLikesByProductor y contarLikesByUsuario.
 * </p>
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see LikeService
 * @see LikeRepository
 */
@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

	/** Mock del repositorio de likes — sustituye la BD real durante los tests. */
	@Mock
	private LikeRepository likeRepository;

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
	private LikeService likeService;

	// ── toggleLike ────────────────────────────────────────────────────────────

	/**
	 * Verifica que {@link LikeService#toggleLike(Integer, Integer)} crea el like y
	 * devuelve {@code true} cuando el usuario aún no lo había dado.
	 */
	@Test
	void toggleLike_creaLike_cuandoNoExiste() {
		// GIVEN
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1);
		Track track = new Track();
		track.setIdTrack(10);

		when(likeRepository.findByUsuarioIdUsuarioAndTrackIdTrack(1, 10)).thenReturn(Optional.empty());
		when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
		when(trackRepository.findById(any())).thenReturn(Optional.of(track));

		// WHEN
		boolean resultado = likeService.toggleLike(1, 10);

		// THEN
		assertTrue(resultado);
		verify(likeRepository, times(1)).save(any(Like.class));
	}

	/**
	 * Verifica que {@link LikeService#toggleLike(Integer, Integer)} elimina el like
	 * y devuelve {@code false} cuando el usuario ya lo había dado.
	 */
	@Test
	void toggleLike_eliminaLike_cuandoYaExiste() {
		// GIVEN
		Like like = new Like();
		when(likeRepository.findByUsuarioIdUsuarioAndTrackIdTrack(1, 10)).thenReturn(Optional.of(like));

		// WHEN
		boolean resultado = likeService.toggleLike(1, 10);

		// THEN
		assertFalse(resultado);
		verify(likeRepository, times(1)).delete(like);
	}

	// ── tieneLike ─────────────────────────────────────────────────────────────

	/**
	 * Verifica que {@link LikeService#tieneLike(Integer, Integer)} devuelve
	 * {@code true} cuando el usuario ya ha dado like al track.
	 */
	@Test
	void tieneLike_devuelveTrue_cuandoExiste() {
		// GIVEN
		when(likeRepository.existsByUsuarioIdUsuarioAndTrackIdTrack(1, 10)).thenReturn(true);

		// WHEN
		boolean resultado = likeService.tieneLike(1, 10);

		// THEN
		assertTrue(resultado);
	}

	/**
	 * Verifica que {@link LikeService#tieneLike(Integer, Integer)} devuelve
	 * {@code false} cuando el usuario no ha dado like al track.
	 */
	@Test
	void tieneLike_devuelveFalse_cuandoNoExiste() {
		// GIVEN
		when(likeRepository.existsByUsuarioIdUsuarioAndTrackIdTrack(1, 10)).thenReturn(false);

		// WHEN
		boolean resultado = likeService.tieneLike(1, 10);

		// THEN
		assertFalse(resultado);
	}

	// ── contarLikes ───────────────────────────────────────────────────────────

	/**
	 * Verifica que {@link LikeService#contarLikes(Integer)} delega correctamente en
	 * el repositorio y devuelve el número total de likes del track.
	 */
	@Test
	void contarLikes_devuelveTotal() {
		// GIVEN
		when(likeRepository.countByTrackIdTrack(10)).thenReturn(6);

		// WHEN
		Integer resultado = likeService.contarLikes(10);

		// THEN
		assertEquals(6, resultado);
	}

	// ── contarLikesByProductor ────────────────────────────────────────────────

	/**
	 * Verifica que {@link LikeService#contarLikesByProductor(Integer)} delega
	 * correctamente en el repositorio y devuelve el total de likes del productor.
	 */
	@Test
	void contarLikesByProductor_devuelveTotal() {
		// GIVEN
		when(likeRepository.countByTrackUsuarioIdUsuario(1)).thenReturn(42);

		// WHEN
		Integer resultado = likeService.contarLikesByProductor(1);

		// THEN
		assertEquals(42, resultado);
	}

	// ── contarLikesByUsuario ──────────────────────────────────────────────────

	/**
	 * Verifica que {@link LikeService#contarLikesByUsuario(Integer)} delega
	 * correctamente en el repositorio y devuelve el total de likes del usuario.
	 */
	@Test
	void contarLikesByUsuario_devuelveTotal() {
		// GIVEN
		when(likeRepository.countByUsuarioIdUsuario(1)).thenReturn(15);

		// WHEN
		Integer resultado = likeService.contarLikesByUsuario(1);

		// THEN
		assertEquals(15, resultado);
	}
}