package com.EDLM.showcase.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.EDLM.showcase.DTO.DescargaData;
import com.EDLM.showcase.entities.Descarga;
import com.EDLM.showcase.entities.Track;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.repositories.DescargaRepository;
import com.EDLM.showcase.repositories.LikeRepository;
import com.EDLM.showcase.repositories.TrackRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;

/**
 * Servicio que gestiona la lógica de negocio relacionada con las descargas.
 * Registra cada descarga en la BD, cuenta las descargas por usuario y por track,
 * y devuelve el historial de descargas de un usuario.
 * Solo los usuarios registrados pueden descargar tracks.
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Service
public class DescargaService {

    private DescargaRepository descargaRepository;
    private TrackRepository trackRepository;
    private UsuarioRepository usuarioRepository;
    private LikeRepository likeRepository;

    /**
     * Constructor con inyección de dependencias de los tres repositorios necesarios.
     *
     * @param descargaRepository Repositorio de descargas
     * @param trackRepository    Repositorio de tracks
     * @param usuarioRepository  Repositorio de usuarios
     */
    public DescargaService(DescargaRepository descargaRepository, TrackRepository trackRepository,
            UsuarioRepository usuarioRepository, LikeRepository likeRepository) {
        this.descargaRepository = descargaRepository;
        this.trackRepository = trackRepository;
        this.usuarioRepository = usuarioRepository;
        this.likeRepository = likeRepository;
    }

    /**
     * Registra una descarga en la BD y devuelve el archivo del track.
     * Se llama cada vez que un usuario descarga un track.
     *
     * @param idUsuario ID del usuario que realiza la descarga
     * @param idTrack   ID del track que se descarga
     * @return true si la descarga se ha registrado correctamente
     */
    public boolean registrarDescarga(Integer idUsuario, Integer idTrack) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        Track track = trackRepository.findById(idTrack).orElse(null);
        descargaRepository.save(new Descarga(usuario, track));
        return true;
    }

    /**
     * Devuelve el número total de descargas realizadas por un usuario.
     * Se usa para mostrar las estadísticas del usuario en su perfil.
     *
     * @param idUsuario ID del usuario
     * @return Número total de descargas realizadas por el usuario
     */
    public Integer contarDescargasByUsuario(Integer idUsuario) {
        return descargaRepository.countByUsuarioIdUsuario(idUsuario);
    }

    /**
     * Devuelve el número total de descargas de un track.
     * Se usa para mostrar la popularidad de un track.
     *
     * @param idTrack ID del track
     * @return Número total de descargas del track
     */
    public Integer contarDescargasByTrack(Integer idTrack) {
        return descargaRepository.countByTrackIdTrack(idTrack);
    }

    /**
     * Devuelve el historial de descargas de un usuario ordenado por fecha descendente.
     * Se usa para mostrar la sección "Mis descargas" en el perfil del usuario.
     * Devuelve DescargaData en lugar de la entidad Descarga para evitar problemas
     * de serialización con las relaciones lazy de JPA y seguir el patrón DTO del proyecto.
     * Incluye el número real de likes de cada track consultando el repositorio por cada uno.
     *
     * @param idUsuario ID del usuario
     * @return Lista de DescargaData con el historial de descargas del usuario
     */
    public List<DescargaData> getDescargasByUsuario(Integer idUsuario) {
        List<Descarga> descargas = descargaRepository.findByUsuarioIdUsuarioOrderByFecDescargaDesc(idUsuario);
        List<DescargaData> resultado = new ArrayList<>();
        for (Descarga descarga : descargas) {
            Track track = descarga.getTrack();
            Integer totalLikes = likeRepository.countByTrackIdTrack(track.getIdTrack());
            // Sanitizamos la portada igual que en TrackResumen — '@' como separador de URL
            String portada = track.getPortada() != null
                    ? track.getPortada().replace(File.separatorChar, '@')
                    : "portadas/portada_default.jpg";
            // Formateamos la duración igual que en TrackResumen
            String duracion = track.getDuracion().format(
                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));

            resultado.add(new DescargaData(
                    track.getIdTrack(),
                    track.getTitulo(),
                    track.getUsuario().getNombre(),
                    portada,
                    duracion,
                    totalLikes,
                    descarga.getFecDescarga()
            ));
        }
        return resultado;
    }
}