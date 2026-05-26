package com.EDLM.showcase.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.EDLM.showcase.entities.Playlist;
import com.EDLM.showcase.entities.Track;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.entities.PlaylistTrack;
import com.EDLM.showcase.repositories.ComentarioRepository;
import com.EDLM.showcase.repositories.LikeRepository;
import com.EDLM.showcase.repositories.PlaylistRepository;
import com.EDLM.showcase.repositories.PlaylistTrackRepository;
import com.EDLM.showcase.repositories.TrackRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;
import com.EDLM.showcase.services.PlaylistService;

/**
 * Tests unitarios para {@link PlaylistService}.
 *
 * <p>
 * Se mockean los seis repositorios que usa el service. Se verifican los casos
 * principales de crearPlaylist, eliminarPlaylist, añadirTrack y quitarTrack.
 * </p>
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see PlaylistService
 * @see PlaylistRepository
 * @see PlaylistTrackRepository
 */
@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTest {

	/** Mock del repositorio de playlists. */
	@Mock
	private PlaylistRepository playlistRepository;

	/** Mock del repositorio de relaciones playlist-track. */
	@Mock
	private PlaylistTrackRepository playlistTrackRepository;

	/** Mock del repositorio de tracks. */
	@Mock
	private TrackRepository trackRepository;

	/** Mock del repositorio de usuarios. */
	@Mock
	private UsuarioRepository usuarioRepository;

	/** Mock del repositorio de likes — para contar likes de cada track. */
	@Mock
	private LikeRepository likeRepository;

	/**
	 * Mock del repositorio de comentarios — para contar comentarios de cada track.
	 */
	@Mock
	private ComentarioRepository comentarioRepository;

	/**
	 * Instancia del service bajo test con sus dependencias mockeadas inyectadas.
	 */
	@InjectMocks
	private PlaylistService playlistService;

	// ── crearPlaylist ─────────────────────────────────────────────────────────

	/**
	 * Verifica que {@link PlaylistService#crearPlaylist(Integer, String)} crea la
	 * playlist y devuelve {@code true} cuando no existe otra con el mismo nombre.
	 */
	@Test
	void crearPlaylist_crea_cuandoNombreNoExiste() {
		// GIVEN
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1);

		when(playlistRepository.existsByNombreAndUsuarioIdUsuario("MisRaps", 1)).thenReturn(false);
		when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));

		// WHEN
		boolean resultado = playlistService.crearPlaylist(1, "MisRaps");

		// THEN
		assertTrue(resultado);
		verify(playlistRepository, times(1)).save(any(Playlist.class));
	}

	/**
	 * Verifica que {@link PlaylistService#crearPlaylist(Integer, String)} devuelve
	 * {@code false} y no guarda nada cuando ya existe una playlist con ese nombre.
	 */
	@Test
	void crearPlaylist_devuelveFalse_cuandoNombreYaExiste() {
		// GIVEN
		when(playlistRepository.existsByNombreAndUsuarioIdUsuario("MisRaps", 1)).thenReturn(true);

		// WHEN
		boolean resultado = playlistService.crearPlaylist(1, "MisRaps");

		// THEN
		assertFalse(resultado);
		verify(playlistRepository, never()).save(any());
	}

	// ── eliminarPlaylist ──────────────────────────────────────────────────────

	/**
	 * Verifica que {@link PlaylistService#eliminarPlaylist(Integer)} delega
	 * correctamente en el repositorio llamando a deleteById.
	 */
	@Test
	void eliminarPlaylist_llamaDeleteById() {
		// WHEN
		playlistService.eliminarPlaylist(5);

		// THEN
		verify(playlistRepository, times(1)).deleteById(5);
	}

	// ── añadirTrack ───────────────────────────────────────────────────────────

	/**
	 * Verifica que {@link PlaylistService#añadirTrack(Integer, Integer)} añade el
	 * track y devuelve {@code true} cuando el track no estaba en la playlist.
	 */
	@Test
	void añadirTrack_añade_cuandoNoEstaEnPlaylist() {
		// GIVEN
		Playlist playlist = new Playlist();
		Track track = new Track();

		when(playlistTrackRepository.existsByIdIdPlaylistAndIdIdTrack(1, 10)).thenReturn(false);
		when(playlistRepository.findById(any())).thenReturn(Optional.of(playlist));
		when(trackRepository.findById(any())).thenReturn(Optional.of(track));

		// WHEN
		boolean resultado = playlistService.añadirTrack(1, 10);

		// THEN
		assertTrue(resultado);
		verify(playlistTrackRepository, times(1)).save(any(PlaylistTrack.class));
	}

	/**
	 * Verifica que {@link PlaylistService#añadirTrack(Integer, Integer)} devuelve
	 * {@code false} y no guarda nada cuando el track ya estaba en la playlist.
	 */
	@Test
	void añadirTrack_devuelveFalse_cuandoYaEstaEnPlaylist() {
		// GIVEN
		when(playlistTrackRepository.existsByIdIdPlaylistAndIdIdTrack(1, 10)).thenReturn(true);

		// WHEN
		boolean resultado = playlistService.añadirTrack(1, 10);

		// THEN
		assertFalse(resultado);
		verify(playlistTrackRepository, never()).save(any());
	}

	// ── quitarTrack ───────────────────────────────────────────────────────────

	/**
	 * Verifica que {@link PlaylistService#quitarTrack(Integer, Integer)} delega
	 * correctamente en el repositorio llamando a deleteByIdIdPlaylistAndIdIdTrack.
	 */
	@Test
	void quitarTrack_llamaDelete() {
		// WHEN
		playlistService.quitarTrack(1, 10);

		// THEN
		verify(playlistTrackRepository, times(1)).deleteByIdIdPlaylistAndIdIdTrack(1, 10);
	}
}