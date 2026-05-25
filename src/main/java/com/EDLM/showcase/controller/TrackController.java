package com.EDLM.showcase.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.EDLM.showcase.DTO.DataTrack;
import com.EDLM.showcase.DTO.FavoritoData;
import com.EDLM.showcase.DTO.FiltroTrack;
import com.EDLM.showcase.DTO.TrackResumen;
import com.EDLM.showcase.services.TrackService;

/**
 * Controlador REST que expone los endpoints relacionados con los tracks.
 * Gestiona la subida, listado, búsqueda, reproducción y descarga de tracks,
 * así como el servicio de archivos de audio e imágenes de portada.
 * Base URL: /tracks
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@CrossOrigin(origins = "*")
@RestController
public class TrackController {

    private TrackService trackService;

    /**
     * Constructor con inyección de dependencias del servicio de tracks.
     *
     * @param trackService Servicio de tracks
     */
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    /**
     * Sube un nuevo track al sistema.
     * Recibe los datos del track como multipart/form-data incluyendo
     * el archivo de audio y opcionalmente la imagen de portada.
     *
     * @param track DTO con los datos del track y los archivos adjuntos
     * @return true si el track se ha subido correctamente, false si hay un error
     */
    @PostMapping("/tracks/uploadTrack")
    public boolean uploadTrack(@ModelAttribute DataTrack track) {
        return trackService.uploadTrack(track);
    }

    /**
     * Devuelve el listado de tracks ordenados por número de likes.
     * Acepta filtros opcionales por género e idUsuario (productor).
     * Se usa en el top 5 de principal.html, en las recomendaciones
     * del reproductor y en "Mis instrumentales" del perfilProductor.
     *
     * @param filtro DTO con los filtros opcionales (genero, idUsuario)
     * @return Lista de TrackResumen con los tracks que cumplen el filtro
     */
    @GetMapping("/tracks/getListadoTracks")
    public List<TrackResumen> getListadoTracks(@ModelAttribute FiltroTrack filtro) {
        return trackService.getListadoTracks(filtro);
    }

    /**
     * Actualiza el contador de reproducciones de un track (versión legacy).
     * Usar /tracks/reproducir/{idTrack} en su lugar.
     *
     * @param idTrack ID del track reproducido
     */
    @PostMapping("tracks/setReproducciones")
    public void setReproducciones(@RequestParam Integer idTrack) {
        trackService.setReproducciones(idTrack);
    }

    /**
     * Sirve la imagen de portada de un track desde disco.
     * La ruta llega con '@' como separador de directorios — se reemplaza
     * por File.separatorChar antes de construir el Path en disco.
     * El patrón {ruta:.+} captura también puntos, necesario para extensiones como .jpg.
     *
     * @param ruta Ruta relativa de la portada con '@' como separador
     * @return La imagen de portada con el Content-Type correcto
     * @throws IOException Si no se puede leer el archivo
     */
    @GetMapping("/tracks/getPortada/{ruta:.+}")
    public ResponseEntity<Resource> getPortada(@PathVariable String ruta) throws IOException {
        String rutaBase = System.getProperty("user.dir");
        // Reemplazamos '@' por el separador del sistema operativo para reconstruir la ruta real
        Path rutaCompleta = Paths.get(rutaBase, ruta.replace('@', File.separatorChar));
        Resource resource = new UrlResource(rutaCompleta.toUri());
        String contentType = Files.probeContentType(rutaCompleta);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : "image/jpeg"))
                .body(resource);
    }

    /**
     * Sirve el archivo de audio instrumental de un track desde disco.
     * Recibe el id del track, consulta la ruta en BD y sirve el archivo.
     * Detecta el Content-Type con Files.probeContentType(); si no puede
     * determinarlo usa audio/mpeg por defecto.
     *
     * @param id ID del track cuyo instrumental se quiere reproducir
     * @return El archivo de audio con el Content-Type correcto
     * @throws IOException Si no se puede leer el archivo
     */
    @GetMapping("/tracks/getInstrumental/{id}")
    public ResponseEntity<Resource> getInstrumental(@PathVariable Integer id) throws IOException {
        String rutaBase = System.getProperty("user.dir");
        String ruta = trackService.getInstrumental(id);
        Path rutaCompleta = Paths.get(rutaBase, ruta);
        Resource resource = new UrlResource(rutaCompleta.toUri());
        String contentType = Files.probeContentType(rutaCompleta);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : "audio/mpeg"))
                .body(resource);
    }

    /**
     * Busca tracks por título de forma parcial e insensible a mayúsculas.
     * Se usa en el buscador con desplegable de principal.html y reproductor.html.
     * Acepta @RequestParam porque no hay parámetro en la ruta.
     *
     * @param titulo Texto de búsqueda — se buscan tracks cuyo título lo contenga
     * @return Lista de TrackResumen con los tracks que coinciden
     */
    @GetMapping("/tracks/buscar")
    public List<TrackResumen> buscarTracks(@RequestParam String titulo) {
        return trackService.buscarPorTitulo(titulo);
    }

    /**
     * Devuelve los datos de un track concreto por su id.
     * Se usa al cargar el reproductor para obtener título, productor, género y portada.
     *
     * @param id ID del track a obtener
     * @return TrackResumen con los datos del track
     */
    @GetMapping("/tracks/getTrack/{id}")
    public TrackResumen getTrackPorId(@PathVariable Integer id) {
        return trackService.getTrackPorId(id);
    }

    /**
     * Devuelve los últimos tracks subidos al sistema.
     * Se usa en la sección "Nuevas subidas" de principal.html.
     *
     * @return Lista de TrackResumen con los tracks más recientes
     */
    @GetMapping("/tracks/getUltimasSubidas")
    public List<TrackResumen> getUltimasSubidas() {
        return trackService.getUltimasSubidas();
    }

    /**
     * Devuelve el número total de reproducciones de todos los tracks de un productor.
     * Se usa para mostrar las estadísticas globales del productor en su perfil.
     *
     * @param idUsuario ID del productor
     * @return Número total de reproducciones de todos los tracks del productor
     */
    @GetMapping("/tracks/sumReproduccionesByProductor")
    public Integer sumReproduccionesByProductor(@RequestParam Integer idUsuario) {
        return trackService.sumReproduccionesByProductor(idUsuario);
    }

    /**
     * Devuelve el número total de tracks subidos por un productor.
     * Se usa para mostrar las estadísticas globales del productor en su perfil.
     *
     * @param idUsuario ID del productor
     * @return Número total de tracks del productor
     */
    @GetMapping("/tracks/contarTracksByProductor")
    public Integer contarTracksByProductor(@RequestParam Integer idUsuario) {
        return trackService.contarTracksByProductor(idUsuario);
    }

    /**
     * Incrementa en 1 el contador de reproducciones de un track.
     * Se llama desde el frontend cada vez que el evento "play" del audio se dispara.
     * Cubre todos los casos: play manual, siguiente, anterior y repetición.
     *
     * @param idTrack ID del track reproducido
     */
    @PostMapping("/tracks/reproducir/{idTrack}")
    public void incrementarReproducciones(@PathVariable Integer idTrack) {
        trackService.setReproducciones(idTrack);
    }
    
    /**
     * Busca tracks aplicando filtros avanzados desde la sección "Explorar" de principal.html.
     * A diferencia de getListadoTracks, no limita los resultados a 5 y permite
     * filtrar por cualquier combinación de género, productor, likes, comentarios y fechas.
     *
     * @param filtro DTO con los criterios de filtrado y ordenación
     * @return Lista de TrackResumen con los tracks que cumplen todos los filtros aplicados
     */
    @GetMapping("/tracks/buscarConFiltros")
    public List<TrackResumen> buscarConFiltros(@ModelAttribute FiltroTrack filtro) {
        return trackService.buscarConFiltros(filtro);
    }
    
    /**
	 * Devuelve los tracks a los que un usuario ha dado like, ordenados del más reciente al más antiguo.
	 * Devuelve FavoritoData en lugar de TrackResumen para incluir la fecha del like
	 * y seguir el patrón DTO del proyecto.
	 *
	 * @param idUsuario ID del usuario
	 * @return Lista de FavoritoData con los tracks favoritos del usuario
	 */
	@GetMapping("/tracks/favoritos")
	public List<FavoritoData> getFavoritosByUsuario(@RequestParam Integer idUsuario) {
	    return trackService.getFavoritosByUsuario(idUsuario);
	}
}