package com.EDLM.showcase.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.EDLM.showcase.DTO.FiltroTrack;
import com.EDLM.showcase.DTO.TrackResumen;
import com.EDLM.showcase.entities.Track;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.repositories.ComentarioRepository;
import com.EDLM.showcase.repositories.LikeRepository;
import com.EDLM.showcase.repositories.TrackRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;
import com.EDLM.showcase.services.TrackService;

/**
 * Tests unitarios para {@link TrackService}.
 *
 * <p>
 * Se mockean los cuatro repositorios que usa el service. Los métodos que
 * dependen del sistema de ficheros ({@code uploadTrack},
 * {@code subirInstrumental}, {@code subirPortada}) no se testean unitariamente
 * — requieren un entorno de integración con disco real.
 * </p>
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see TrackService
 * @see TrackRepository
 * @see LikeRepository
 * @see ComentarioRepository
 */
@ExtendWith(MockitoExtension.class)
public class TrackServiceTest {

	/** Mock del repositorio de tracks — sustituye la BD real durante los tests. */
	@Mock
	private TrackRepository trackRepository;

	/** Mock del repositorio de usuarios. */
	@Mock
	private UsuarioRepository usuarioRepository;

	/** Mock del repositorio de likes — usado para contar likes por track. */
	@Mock
	private LikeRepository likeRepository;

	/**
	 * Mock del repositorio de comentarios — usado para contar comentarios por
	 * track.
	 */
	@Mock
	private ComentarioRepository comentarioRepository;

	/**
	 * Instancia del service bajo test con sus dependencias mockeadas inyectadas.
	 */
	@InjectMocks
	private TrackService trackService;

	// ── Helpers ───────────────────────────────────────────────────────────────

	/**
	 * Crea un {@link Track} de prueba con los datos mínimos necesarios.
	 *
	 * @param id      Identificador del track
	 * @param titulo  Título del track
	 * @param genero  Género musical del track
	 * @param usuario Productor propietario del track
	 * @return Track instanciado con los datos proporcionados
	 */
	private Track crearTrack(int id, String titulo, String genero, Usuario usuario) {
		Track track = new Track();
		track.setIdTrack(id);
		track.setTitulo(titulo);
		track.setGenero(genero);
		track.setFecSubida(LocalDateTime.now());
		track.setContReprod(0);
		track.setUsuario(usuario);
		track.setDuracion(java.time.LocalTime.of(0, 2, 30)); // duración de prueba: 00:02:30
		return track;
	}

	/**
	 * Crea un {@link Usuario} de prueba con los datos mínimos necesarios.
	 *
	 * @param id     Identificador del usuario
	 * @param nombre Nombre del usuario
	 * @return Usuario instanciado con los datos proporcionados
	 */
	private Usuario crearUsuario(int id, String nombre) {
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(id);
		usuario.setNombre(nombre);
		return usuario;
	}

	// ── getTrackPorId ─────────────────────────────────────────────────────────

	/**
	 * Verifica que {@link TrackService#getTrackPorId(Integer)} devuelve un
	 * {@link TrackResumen} correcto cuando el track existe en la base de datos.
	 */
	@Test
	void getTrackPorId_trackExiste() {
		// GIVEN
		Usuario usuario = crearUsuario(1, "Roberto");
		Track track = crearTrack(10, "rapBlues", "rap", usuario);

		when(trackRepository.findById(10)).thenReturn(Optional.of(track));
		when(likeRepository.countByTrackIdTrack(10)).thenReturn(5);
		when(comentarioRepository.countByTrackIdTrack(10)).thenReturn(3);

		// WHEN
		TrackResumen resultado = trackService.getTrackPorId(10);

		// THEN
		assertNotNull(resultado);
		assertEquals("rapBlues", resultado.getTitulo());
		assertEquals(5, resultado.getLikes());
		assertEquals(3, resultado.getComentarios());
	}

	/**
	 * Verifica que {@link TrackService#getTrackPorId(Integer)} lanza una
	 * {@link ResponseStatusException} con HTTP 404 cuando el track no existe.
	 */
	@Test
	void getTrackPorId_trackNoExiste() {
		// GIVEN
		when(trackRepository.findById(99)).thenReturn(Optional.empty());

		// WHEN + THEN
		assertThrows(ResponseStatusException.class, () -> trackService.getTrackPorId(99));
	}

	// ── setReproducciones ─────────────────────────────────────────────────────

	/**
	 * Verifica que {@link TrackService#setReproducciones(Integer)} incrementa en 1
	 * el contador de reproducciones del track y guarda el cambio en BD.
	 */
	@Test
	void setReproducciones_incrementaContador() {
		// GIVEN
		Track track = new Track();
		track.setIdTrack(1);
		track.setContReprod(10);

		when(trackRepository.findById(1)).thenReturn(Optional.of(track));

		// WHEN
		trackService.setReproducciones(1);

		// THEN
		assertEquals(11, track.getContReprod());
		verify(trackRepository, times(1)).save(track);
	}

	/**
	 * Verifica que {@link TrackService#setReproducciones(Integer)} no hace nada
	 * cuando el track no existe — no lanza excepción ni llama a save.
	 */
	@Test
	void setReproducciones_trackNoExiste_noHaceNada() {
		// GIVEN
		when(trackRepository.findById(99)).thenReturn(Optional.empty());

		// WHEN
		trackService.setReproducciones(99);

		// THEN
		verify(trackRepository, never()).save(any());
	}

	// ── buscarPorTitulo ───────────────────────────────────────────────────────

	/**
	 * Verifica que {@link TrackService#buscarPorTitulo(String)} devuelve los tracks
	 * cuyo título contiene el texto buscado.
	 */
	@Test
	void buscarPorTitulo_devuelveResultados() {
		// GIVEN
		Usuario usuario = crearUsuario(1, "Roberto");
		Track track = crearTrack(1, "rapBlues", "rap", usuario);

		when(trackRepository.findByTituloContainingIgnoreCase("rap")).thenReturn(List.of(track));
		when(likeRepository.countByTrackIdTrack(1)).thenReturn(2);
		when(comentarioRepository.countByTrackIdTrack(1)).thenReturn(1);

		// WHEN
		List<TrackResumen> resultado = trackService.buscarPorTitulo("rap");

		// THEN
		assertEquals(1, resultado.size());
		assertEquals("rapBlues", resultado.get(0).getTitulo());
	}

	/**
	 * Verifica que {@link TrackService#buscarPorTitulo(String)} devuelve una lista
	 * vacía cuando no hay tracks que coincidan.
	 */
	@Test
	void buscarPorTitulo_sinResultados() {
		// GIVEN
		when(trackRepository.findByTituloContainingIgnoreCase("xyz")).thenReturn(List.of());

		// WHEN
		List<TrackResumen> resultado = trackService.buscarPorTitulo("xyz");

		// THEN
		assertTrue(resultado.isEmpty());
	}

	// ── contarTracksByProductor ───────────────────────────────────────────────

	/**
	 * Verifica que {@link TrackService#contarTracksByProductor(Integer)} delega
	 * correctamente en el repositorio y devuelve el valor esperado.
	 */
	@Test
	void contarTracksByProductor_devuelveConteo() {
		// GIVEN
		when(trackRepository.countByUsuarioIdUsuario(1)).thenReturn(7);

		// WHEN
		Integer resultado = trackService.contarTracksByProductor(1);

		// THEN
		assertEquals(7, resultado);
	}

	// ── buscarConFiltros ──────────────────────────────────────────────────────

	/**
	 * Verifica que {@link TrackService#buscarConFiltros(FiltroTrack)} filtra
	 * correctamente por género devolviendo solo los tracks del género indicado.
	 */
	@Test
	void buscarConFiltros_filtraPorGenero() {
		// GIVEN
		Usuario usuario = crearUsuario(1, "Roberto");
		Track trackRap = crearTrack(1, "rapBlues", "rap", usuario);
		Track trackSoul = crearTrack(2, "soulTrack", "soul", usuario);

		when(trackRepository.findAll()).thenReturn(List.of(trackRap, trackSoul));
		when(likeRepository.countByTrackIdTrack(anyInt())).thenReturn(0);
		when(comentarioRepository.countByTrackIdTrack(anyInt())).thenReturn(0);

		FiltroTrack filtro = new FiltroTrack();
		filtro.setGenero("rap");

		// WHEN
		List<TrackResumen> resultado = trackService.buscarConFiltros(filtro);

		// THEN
		assertEquals(1, resultado.size());
		assertEquals("rapBlues", resultado.get(0).getTitulo());
	}

	/**
	 * Verifica que {@link TrackService#buscarConFiltros(FiltroTrack)} devuelve
	 * todos los tracks cuando no se aplica ningún filtro.
	 */
	@Test
	void buscarConFiltros_sinFiltros_devuelveTodos() {
		// GIVEN
		Usuario usuario = crearUsuario(1, "Roberto");
		Track t1 = crearTrack(1, "track1", "rap", usuario);
		Track t2 = crearTrack(2, "track2", "soul", usuario);

		when(trackRepository.findAll()).thenReturn(List.of(t1, t2));
		when(likeRepository.countByTrackIdTrack(anyInt())).thenReturn(0);
		when(comentarioRepository.countByTrackIdTrack(anyInt())).thenReturn(0);

		FiltroTrack filtro = new FiltroTrack();

		// WHEN
		List<TrackResumen> resultado = trackService.buscarConFiltros(filtro);

		// THEN
		assertEquals(2, resultado.size());
	}
}