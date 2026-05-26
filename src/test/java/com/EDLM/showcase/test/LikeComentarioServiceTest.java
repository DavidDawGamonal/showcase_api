package com.EDLM.showcase.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.EDLM.showcase.entities.Comentario;
import com.EDLM.showcase.entities.LikeComentario;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.repositories.ComentarioRepository;
import com.EDLM.showcase.repositories.LikeComentarioRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;
import com.EDLM.showcase.services.LikeComentarioService;

/**
 * Tests unitarios para {@link LikeComentarioService}.
 *
 * <p>
 * Se mockean los tres repositorios que usa el service. Se verifican los casos
 * principales de toggleLikeComentario (dar y quitar), tieneLikeComentario,
 * contarLikesComentario y getUsuariosLike.
 * </p>
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see LikeComentarioService
 * @see LikeComentarioRepository
 */
@ExtendWith(MockitoExtension.class)
public class LikeComentarioServiceTest {

	/** Mock del repositorio de likes de comentarios. */
	@Mock
	private LikeComentarioRepository likeComentarioRepository;

	/** Mock del repositorio de usuarios. */
	@Mock
	private UsuarioRepository usuarioRepository;

	/** Mock del repositorio de comentarios. */
	@Mock
	private ComentarioRepository comentarioRepository;

	/**
	 * Instancia del service bajo test con sus dependencias mockeadas inyectadas.
	 */
	@InjectMocks
	private LikeComentarioService likeComentarioService;

	// ── toggleLikeComentario ──────────────────────────────────────────────────

	/**
	 * Verifica que
	 * {@link LikeComentarioService#toggleLikeComentario(Integer, Integer)} crea el
	 * like y devuelve {@code true} cuando el usuario aún no lo había dado.
	 */
	@Test
	void toggleLikeComentario_creaLike_cuandoNoExiste() {
		// GIVEN
		Usuario usuario = new Usuario();
		Comentario comentario = new Comentario();

		when(likeComentarioRepository.existsByUsuarioIdUsuarioAndComentarioIdComent(1, 5)).thenReturn(false);
		when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
		when(comentarioRepository.findById(any())).thenReturn(Optional.of(comentario));

		// WHEN
		boolean resultado = likeComentarioService.toggleLikeComentario(1, 5);

		// THEN
		assertTrue(resultado);
		verify(likeComentarioRepository, times(1)).save(any(LikeComentario.class));
	}

	/**
	 * Verifica que
	 * {@link LikeComentarioService#toggleLikeComentario(Integer, Integer)} elimina
	 * el like y devuelve {@code false} cuando el usuario ya lo había dado.
	 */
	@Test
	void toggleLikeComentario_eliminaLike_cuandoYaExiste() {
		// GIVEN
		when(likeComentarioRepository.existsByUsuarioIdUsuarioAndComentarioIdComent(1, 5)).thenReturn(true);

		// WHEN
		boolean resultado = likeComentarioService.toggleLikeComentario(1, 5);

		// THEN
		assertFalse(resultado);
		verify(likeComentarioRepository, times(1)).deleteByUsuarioIdUsuarioAndComentarioIdComent(1, 5);
	}

	// ── tieneLikeComentario ───────────────────────────────────────────────────

	/**
	 * Verifica que
	 * {@link LikeComentarioService#tieneLikeComentario(Integer, Integer)} devuelve
	 * {@code true} cuando el usuario ya ha dado like al comentario.
	 */
	@Test
	void tieneLikeComentario_devuelveTrue_cuandoExiste() {
		// GIVEN
		when(likeComentarioRepository.existsByUsuarioIdUsuarioAndComentarioIdComent(1, 5)).thenReturn(true);

		// WHEN
		boolean resultado = likeComentarioService.tieneLikeComentario(1, 5);

		// THEN
		assertTrue(resultado);
	}

	/**
	 * Verifica que
	 * {@link LikeComentarioService#tieneLikeComentario(Integer, Integer)} devuelve
	 * {@code false} cuando el usuario no ha dado like al comentario.
	 */
	@Test
	void tieneLikeComentario_devuelveFalse_cuandoNoExiste() {
		// GIVEN
		when(likeComentarioRepository.existsByUsuarioIdUsuarioAndComentarioIdComent(1, 5)).thenReturn(false);

		// WHEN
		boolean resultado = likeComentarioService.tieneLikeComentario(1, 5);

		// THEN
		assertFalse(resultado);
	}

	// ── contarLikesComentario ─────────────────────────────────────────────────

	/**
	 * Verifica que {@link LikeComentarioService#contarLikesComentario(Integer)}
	 * delega correctamente en el repositorio y devuelve el total de likes.
	 */
	@Test
	void contarLikesComentario_devuelveTotal() {
		// GIVEN
		when(likeComentarioRepository.countByComentarioIdComent(5)).thenReturn(4);

		// WHEN
		Integer resultado = likeComentarioService.contarLikesComentario(5);

		// THEN
		assertEquals(4, resultado);
	}

	// ── getUsuariosLike ───────────────────────────────────────────────────────

	/**
	 * Verifica que {@link LikeComentarioService#getUsuariosLike(Integer)} devuelve
	 * la lista de nombres de usuarios que han dado like al comentario.
	 */
	@Test
	void getUsuariosLike_devuelveNombres() {
		// GIVEN
		when(likeComentarioRepository.findNombresByComentarioIdComent(5)).thenReturn(List.of("Manuel", "Roberto"));

		// WHEN
		List<String> resultado = likeComentarioService.getUsuariosLike(5);

		// THEN
		assertEquals(2, resultado.size());
		assertEquals("Manuel", resultado.get(0));
		assertEquals("Roberto", resultado.get(1));
	}
}