package com.EDLM.showcase.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.EDLM.showcase.entities.Descarga;
import com.EDLM.showcase.entities.Track;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.repositories.DescargaRepository;
import com.EDLM.showcase.repositories.LikeRepository;
import com.EDLM.showcase.repositories.TrackRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;
import com.EDLM.showcase.services.DescargaService;

/**
 * Tests unitarios para {@link DescargaService}.
 *
 * <p>
 * Se mockean los cuatro repositorios que usa el service. Se verifican los casos
 * principales de registrarDescarga, contarDescargasByUsuario y
 * contarDescargasByTrack. El método getDescargasByUsuario no se testea
 * unitariamente por depender del formateo de duración y portada de la entidad
 * Track — requeriría un entorno de integración.
 * </p>
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 * @see DescargaService
 * @see DescargaRepository
 */
@ExtendWith(MockitoExtension.class)
public class DescargaServiceTest {

	/** Mock del repositorio de descargas. */
	@Mock
	private DescargaRepository descargaRepository;

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
	 * Instancia del service bajo test con sus dependencias mockeadas inyectadas.
	 */
	@InjectMocks
	private DescargaService descargaService;

	// ── registrarDescarga ─────────────────────────────────────────────────────

	/**
	 * Verifica que {@link DescargaService#registrarDescarga(Integer, Integer)}
	 * persiste la descarga y devuelve {@code true}.
	 */
	@Test
	void registrarDescarga_guardaYDevuelveTrue() {
		// GIVEN
		Usuario usuario = new Usuario();
		Track track = new Track();

		when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
		when(trackRepository.findById(any())).thenReturn(Optional.of(track));

		// WHEN
		boolean resultado = descargaService.registrarDescarga(1, 10);

		// THEN
		assertTrue(resultado);
		verify(descargaRepository, times(1)).save(any(Descarga.class));
	}

	// ── contarDescargasByUsuario ──────────────────────────────────────────────

	/**
	 * Verifica que {@link DescargaService#contarDescargasByUsuario(Integer)} delega
	 * correctamente en el repositorio y devuelve el total de descargas.
	 */
	@Test
	void contarDescargasByUsuario_devuelveTotal() {
		// GIVEN
		when(descargaRepository.countByUsuarioIdUsuario(1)).thenReturn(8);

		// WHEN
		Integer resultado = descargaService.contarDescargasByUsuario(1);

		// THEN
		assertEquals(8, resultado);
	}

	// ── contarDescargasByTrack ────────────────────────────────────────────────

	/**
	 * Verifica que {@link DescargaService#contarDescargasByTrack(Integer)} delega
	 * correctamente en el repositorio y devuelve el total de descargas.
	 */
	@Test
	void contarDescargasByTrack_devuelveTotal() {
		// GIVEN
		when(descargaRepository.countByTrackIdTrack(10)).thenReturn(3);

		// WHEN
		Integer resultado = descargaService.contarDescargasByTrack(10);

		// THEN
		assertEquals(3, resultado);
	}
}