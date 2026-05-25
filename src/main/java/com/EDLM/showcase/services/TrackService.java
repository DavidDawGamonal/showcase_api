package com.EDLM.showcase.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.EDLM.showcase.DTO.DataTrack;
import com.EDLM.showcase.DTO.FavoritoData;
import com.EDLM.showcase.DTO.FiltroTrack;
import com.EDLM.showcase.DTO.TrackResumen;
import com.EDLM.showcase.entities.Like;
import com.EDLM.showcase.entities.Track;
import com.EDLM.showcase.entities.Usuario;
import com.EDLM.showcase.repositories.ComentarioRepository;
import com.EDLM.showcase.repositories.LikeRepository;
import com.EDLM.showcase.repositories.TrackRepository;
import com.EDLM.showcase.repositories.UsuarioRepository;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los tracks musicales.
 * Proporciona operaciones de subida de archivos, consulta y búsqueda de tracks.
 *
 * <p>Gestiona dos tipos de archivos en disco:</p>
 * <ul>
 *   <li>Instrumentales: archivos de audio almacenados en la carpeta "instrumentales"</li>
 *   <li>Portadas: imágenes de portada almacenadas en la carpeta "portadas"</li>
 * </ul>
 *
 * @author Elvis David Lara Manrique
 * @version 1.0
 * @since 1.0
 * @see Track
 * @see TrackRepository
 */
@Service
public class TrackService {

    /** Repositorio JPA para acceder a los datos de los tracks. */
    private TrackRepository trackRepository;

    /** Repositorio JPA para acceder a los datos de los usuarios. */
    private UsuarioRepository usuarioRepository;

    /** Repositorio JPA para contar los likes de cada track. */
    private LikeRepository likeRepository;

    /** Repositorio JPA para contar los comentarios de cada track. */
    private ComentarioRepository comentarioRepository;

    /** Ruta relativa de la carpeta donde se almacenan los instrumentales. */
    private String ruta;

    /** Ruta relativa de la carpeta donde se almacenan las portadas. */
    private String rutaPortadas;

    /** Nombre del archivo de portada por defecto cuando el productor no sube ninguna. */
    private static final String PORTADA_DEFAULT = "portada_default.jpg";

    /**
     * Constructor que inyecta los repositorios necesarios e inicializa las rutas de almacenamiento.
     *
     * @param trackRepository      Repositorio de tracks
     * @param usuarioRepository    Repositorio de usuarios
     * @param likeRepository       Repositorio de likes
     * @param comentarioRepository Repositorio de comentarios
     */
    public TrackService(TrackRepository trackRepository, UsuarioRepository usuarioRepository,
            LikeRepository likeRepository, ComentarioRepository comentarioRepository) {
        this.trackRepository = trackRepository;
        this.usuarioRepository = usuarioRepository;
        this.likeRepository = likeRepository;
        this.comentarioRepository = comentarioRepository;
        this.ruta = "instrumentales";
        this.rutaPortadas = "portadas";
    }

    /**
     * Sube un nuevo track al sistema: guarda el instrumental y la portada en disco
     * y persiste el track en la base de datos.
     *
     * @param dataTrack DTO con los datos del track y los archivos a subir
     * @return true si el track se subió y guardó correctamente, false si hubo algún error
     */
    public boolean uploadTrack(DataTrack dataTrack) {
        boolean retorno = false;
        String rutaArchivo;
        String rutaPortada;
        try {
            Usuario usuario = usuarioRepository.findById(dataTrack.getUsuario()).get();

            rutaArchivo = subirInstrumental(usuario, dataTrack);
            rutaPortada = subirPortada(usuario, dataTrack);

            if (rutaArchivo != null && rutaPortada != null) {
                Track track = new Track(dataTrack.getTitulo(), dataTrack.getGenero(), dataTrack.getDuracion(),
                        dataTrack.getDescripcion(), rutaPortada,
                        rutaArchivo,
                        usuario);

                Track trackGuardado = trackRepository.save(track);
                retorno = trackGuardado.getIdTrack() != 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            retorno = false;
        }
        return retorno;
    }

    /**
     * Guarda la imagen de portada del track en disco.
     * Si el productor no sube portada, asigna la portada por defecto.
     * Los espacios en el nombre del archivo se reemplazan por guiones para evitar
     * problemas en rutas y URLs.
     *
     * @param usuario   El usuario productor que sube el track
     * @param dataTrack DTO con los datos del track incluyendo el archivo de portada
     * @return La ruta relativa donde se guardó la portada, o null si hubo un error
     */
    private String subirPortada(Usuario usuario, DataTrack dataTrack) {
        String rutaBase = System.getProperty("user.dir");
        File fichero;
        String rutaFichero;
        String nombreArchivo;
        File carpeta;
        try {
            boolean tienePortada = dataTrack.getPortada() != null && !dataTrack.getPortada().isEmpty();

            if (tienePortada) {
                // Los espacios en el nombre se sustituyen por guiones para evitar problemas en rutas
                nombreArchivo = dataTrack.getPortada().getOriginalFilename().replace(' ', '-');
                rutaFichero = rutaPortadas + File.separator + usuario.getIdUsuario() + File.separator + nombreArchivo;
                carpeta = new File(rutaBase, rutaPortadas + File.separator + usuario.getIdUsuario());
                if (!carpeta.exists()) {
                    carpeta.mkdirs();
                }
                fichero = new File(carpeta, nombreArchivo);
                dataTrack.getPortada().transferTo(fichero);
            } else {
                // Si no hay portada, se asigna la imagen por defecto
                rutaFichero = rutaPortadas + File.separator + PORTADA_DEFAULT;
                carpeta = new File(rutaBase, rutaPortadas);
                fichero = new File(carpeta, PORTADA_DEFAULT);
            }

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            rutaFichero = null;
        }
        return rutaFichero;
    }

    /**
     * Guarda el archivo de audio instrumental del track en disco.
     * Crea la carpeta del usuario si no existe y almacena el archivo en ella.
     *
     * @param usuario   El usuario productor que sube el track
     * @param dataTrack DTO con los datos del track incluyendo el archivo de audio
     * @return La ruta relativa donde se guardó el instrumental, o null si hubo un error
     */
    private String subirInstrumental(Usuario usuario, DataTrack dataTrack) {
        String rutaBase = System.getProperty("user.dir");
        String rutaFichero;
        try {
            rutaFichero = ruta + File.separator + usuario.getIdUsuario();
            // Crear carpeta del usuario si no existe
            File carpetaInstrumentales = new File(rutaBase, rutaFichero);
            if (!carpetaInstrumentales.exists()) {
                carpetaInstrumentales.mkdirs();
            }

            // Guardar el archivo en disco
            String nombreArchivo = dataTrack.getInstrumental().getOriginalFilename();
            File ficheroInstrumental = new File(carpetaInstrumentales, nombreArchivo);

            dataTrack.getInstrumental().transferTo(ficheroInstrumental);
            // Se añade el nombre del archivo a rutaFichero para completar la ruta
            // que se guardará en BD y que permite reproducir el audio posteriormente
            rutaFichero = rutaFichero + File.separator + nombreArchivo;

        } catch (IllegalStateException e) {
            e.printStackTrace();
            rutaFichero = null;
        } catch (IOException e) {
            e.printStackTrace();
            rutaFichero = null;
        }

        return rutaFichero;
    }

    /**
     * Obtiene el listado de tracks según los filtros proporcionados.
     * Lógica de filtrado:
     * - Si hay idUsuario y género: carga los top 5 del productor con ese género;
     *   si no llega a 5, completa con tracks del mismo género de otros productores.
     * - Si hay idUsuario sin género: carga los top 5 del productor por likes.
     * - Si no hay idUsuario: carga el top 5 global por likes.
     * Cada TrackResumen incluye el número de likes y comentarios calculados.
     *
     * @param filtro DTO con los criterios de filtrado (idUsuario y/o género)
     * @return Lista de TrackResumen con los tracks que cumplen el filtro
     */
    public List<TrackResumen> getListadoTracks(FiltroTrack filtro) {
        List<Track> tracks;
        if (filtro.getIdUsuario() > 0) {
            if (filtro.getGenero() != null) {
                tracks = trackRepository.findTop5ByLikesAndUserAndGenero(filtro.getGenero(), filtro.getIdUsuario(),
                        PageRequest.of(0, 5));
                if (tracks.size() < 5) {
                    tracks.addAll(trackRepository.findTop5ByLikesWithoutUserAndGenero(filtro.getGenero(),
                            filtro.getIdUsuario(), PageRequest.of(0, 5 - tracks.size())));
                }
            } else {
                tracks = trackRepository.findTop5ByLikesAndUser(filtro.getIdUsuario(), PageRequest.of(0, 5));
            }
        } else {
            tracks = trackRepository.findTop5ByLikes(PageRequest.of(0, 5));
        }
        return tracks.stream().map(t -> new TrackResumen(t, likeRepository.countByTrackIdTrack(t.getIdTrack()),
                comentarioRepository.countByTrackIdTrack(t.getIdTrack()))).collect(Collectors.toList());
    }

    /**
     * Obtiene la ruta del archivo instrumental de un track por su id.
     *
     * @param id El id del track
     * @return La ruta del archivo instrumental en disco
     */
    public String getInstrumental(Integer id) {
        return trackRepository.findById(id).get().getRuta();
    }

    /**
     * Busca tracks cuyo título contenga el texto indicado (insensible a mayúsculas).
     * Cada TrackResumen incluye el número de likes y comentarios calculados.
     *
     * @param titulo Texto a buscar en el título de los tracks
     * @return Lista de TrackResumen con los tracks que coinciden con la búsqueda
     */
    public List<TrackResumen> buscarPorTitulo(String titulo) {
        List<Track> tracks = trackRepository.findByTituloContainingIgnoreCase(titulo);
        return tracks.stream().map(t -> new TrackResumen(t, likeRepository.countByTrackIdTrack(t.getIdTrack()),
                comentarioRepository.countByTrackIdTrack(t.getIdTrack()))).collect(Collectors.toList());
    }

//	public TrackResumen getTrackPorId(Integer id) {
//		Track t = trackRepository.findById(id).get();
//		return new TrackResumen(t, likeRepository.countByTrackIdTrack(t.getIdTrack()),
//				comentarioRepository.countByTrackIdTrack(t.getIdTrack()));
//	}

    /**
     * Sustituye la de aquí encima
     * Obtiene un track por su id y lo devuelve como TrackResumen.
     * Lanza una excepción HTTP 404 si el track no existe, en lugar de un
     * NullPointerException como hacía la versión anterior (comentada arriba).
     *
     * @param id El id del track a buscar
     * @return TrackResumen con los datos del track, sus likes y sus comentarios
     * @throws ResponseStatusException con HTTP 404 si el track no existe
     */
    public TrackResumen getTrackPorId(Integer id) {
        Track t = trackRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Track no encontrado"));

        return new TrackResumen(t,
            likeRepository.countByTrackIdTrack(t.getIdTrack()),
            comentarioRepository.countByTrackIdTrack(t.getIdTrack()));
    }

    /**
     * Obtiene los 10 tracks subidos más recientemente, ordenados por fecha de subida descendente.
     * Cada TrackResumen incluye el número de likes y comentarios calculados.
     *
     * PENDIENTE — filtrar por última semana: requiere nueva query en TrackRepository
     * findByFecSubidaAfter(@Param("desde") LocalDateTime desde, Pageable pageable)
     * para filtrar en BD antes del PageRequest. Las líneas comentadas son un primer
     * paso pero insuficientes solas.
     *
     * @return Lista de hasta 10 TrackResumen con las últimas subidas
     */
    public List<TrackResumen> getUltimasSubidas() {
    	//LocalDateTime hacunasemana = LocalDateTime.now().minusWeeks(1);
        return trackRepository.findAllByOrderByFecSubidaDesc(PageRequest.of(0, 10)).stream()
        		//.filter(track -> track.getFecSubida().isAfter(hacunasemana))
                .map(track -> new TrackResumen(track, likeRepository.countByTrackIdTrack(track.getIdTrack()),
                        comentarioRepository.countByTrackIdTrack(track.getIdTrack())))
                .collect(Collectors.toList());
    }

    /**
     * Incrementa en 1 el contador de reproducciones de un track.
     *
     * @param idTrack El id del track cuyas reproducciones se van a incrementar
     */
    public void setReproducciones(Integer idTrack) {
        Track track = trackRepository.findById(idTrack).orElse(null);
        if (track != null) {
            track.setContReprod(track.getContReprod() + 1);
            trackRepository.save(track);
        }
    }
    
    /**
     * Devuelve el número total de reproducciones de todos los tracks de un productor.
     * Se usa para mostrar las estadísticas globales del productor en su perfil.
     *
     * @param idUsuario ID del productor
     * @return Número total de reproducciones de todos los tracks del productor
     */
    public Integer sumReproduccionesByProductor(Integer idUsuario) {
        return trackRepository.sumReproduccionesByProductor(idUsuario);
    }
    
    /**
     * Devuelve el número total de tracks subidos por un productor.
     * Se usa para mostrar las estadísticas globales del productor en su perfil.
     *
     * @param idUsuario ID del productor
     * @return Número total de tracks del productor
     */
    public Integer contarTracksByProductor(Integer idUsuario) {
        return trackRepository.countByUsuarioIdUsuario(idUsuario);
    }
    
    /**
     * Busca tracks aplicando filtros avanzados con filtrado en memoria.
     * Se usa exclusivamente desde la sección "Explorar" de principal.html.
     *
     * <p>A diferencia de getListadoTracks(), este método no limita los resultados a 5
     * y permite filtrar por cualquier combinación de criterios dinámicos.
     * El filtrado en memoria es válido para el volumen de datos del proyecto y evita
     * tener que crear una query específica por cada combinación posible de filtros.</p>
     *
     * <p>Orden de filtrado:</p>
     * <ol>
     *   <li>Filtros sobre la entidad Track (género, idUsuario, nombreProductor, fechas)</li>
     *   <li>Conversión a TrackResumen para obtener likes y comentarios reales</li>
     *   <li>Filtros sobre TrackResumen (minLikes, minComentarios)</li>
     *   <li>Ordenación según el criterio del filtro</li>
     * </ol>
     *
     * @param filtro DTO con los criterios de filtrado y ordenación
     * @return Lista de TrackResumen con los tracks que cumplen todos los filtros aplicados
     */
    public List<TrackResumen> buscarConFiltros(FiltroTrack filtro) {
        // Cargamos todos los tracks y filtramos en memoria
        List<Track> tracks = trackRepository.findAll();

        // Filtro por género
        if (filtro.getGenero() != null && !filtro.getGenero().isEmpty()) {
            tracks = tracks.stream()
                    .filter(t -> filtro.getGenero().equalsIgnoreCase(t.getGenero()))
                    .collect(Collectors.toList());
        }

        // Filtro por idUsuario (productor)
        if (filtro.getIdUsuario() != null && filtro.getIdUsuario() > 0) {
            tracks = tracks.stream()
                    .filter(t -> t.getUsuario().getIdUsuario() == filtro.getIdUsuario())
                    .collect(Collectors.toList());
        }

        // Filtro por nombre del productor — búsqueda parcial case-insensitive
        if (filtro.getNombreProductor() != null && !filtro.getNombreProductor().isEmpty()) {
            tracks = tracks.stream()
                    .filter(t -> t.getUsuario().getNombre().toLowerCase()
                            .contains(filtro.getNombreProductor().toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filtro por fecha de subida desde (inclusive)
        if (filtro.getFechaDesde() != null) {
            tracks = tracks.stream()
                    .filter(t -> !t.getFecSubida().toLocalDate().isBefore(filtro.getFechaDesde()))
                    .collect(Collectors.toList());
        }

        // Filtro por fecha de subida hasta (inclusive)
        if (filtro.getFechaHasta() != null) {
            tracks = tracks.stream()
                    .filter(t -> !t.getFecSubida().toLocalDate().isAfter(filtro.getFechaHasta()))
                    .collect(Collectors.toList());
        }

        // Convertimos a TrackResumen para poder filtrar por likes y comentarios reales
        // Los likes y comentarios no están en la entidad Track — se calculan desde los repositorios
        List<TrackResumen> resultado = tracks.stream()
                .map(t -> new TrackResumen(t,
                        likeRepository.countByTrackIdTrack(t.getIdTrack()),
                        comentarioRepository.countByTrackIdTrack(t.getIdTrack())))
                .collect(Collectors.toList());

        // Filtro por likes mínimos — se aplica después de convertir porque likes no está en la entidad
        if (filtro.getMinLikes() != null) {
            resultado = resultado.stream()
                    .filter(t -> t.getLikes() >= filtro.getMinLikes())
                    .collect(Collectors.toList());
        }

        // Filtro por comentarios mínimos — igual que likes, no está en la entidad
        if (filtro.getMinComentarios() != null) {
            resultado = resultado.stream()
                    .filter(t -> t.getComentarios() >= filtro.getMinComentarios())
                    .collect(Collectors.toList());
        }

        // Ordenación según el criterio del filtro — por defecto ordena por likes
        String orden = filtro.getOrdenarPor();
        if ("comentarios".equals(orden)) {
            resultado.sort((a, b) -> b.getComentarios() - a.getComentarios());
        } else if ("fecha".equals(orden)) {
            resultado.sort((a, b) -> b.getFecSubida().compareTo(a.getFecSubida()));
        } else if ("reproducciones".equals(orden)) {
            resultado.sort((a, b) -> b.getContReprod() - a.getContReprod());
        } else {
            // Por defecto ordena por likes
            resultado.sort((a, b) -> b.getLikes() - a.getLikes());
        }

        return resultado;
    }
    
    /**
     * Devuelve los tracks a los que un usuario ha dado like, ordenados del más reciente al más antiguo.
     * Se usa para mostrar la sección "Mis favoritos" en el perfil del usuario.
     * Devuelve FavoritoData en lugar de TrackResumen para incluir la fecha del like
     * y seguir el patrón DTO del proyecto — no exponer entidades directamente al frontend.
     * Incluye el número real de likes de cada track consultando el repositorio por cada uno.
     *
     * @param idUsuario ID del usuario
     * @return Lista de FavoritoData con los tracks favoritos del usuario
     */
    public List<FavoritoData> getFavoritosByUsuario(Integer idUsuario) {
        List<Like> likes = likeRepository.findByUsuarioIdUsuarioOrderByFecLikeDesc(idUsuario);
        List<FavoritoData> resultado = new ArrayList<>();
        for (Like like : likes) {
            Track track = like.getTrack();
            Integer totalLikes = likeRepository.countByTrackIdTrack(track.getIdTrack());
            // Sanitizamos la portada igual que en TrackResumen — '@' como separador de URL
//            String portada = track.getPortada() != null
//                    ? track.getPortada().replace(File.separatorChar, '@')
//                    : "portadas@portada_default.jpg";
            // Formateamos la duración igual que en TrackResumen
            String duracion = track.getDuracion().format(
                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));

            resultado.add(new FavoritoData(
                    track.getIdTrack(),
                    track.getTitulo(),
                    track.getUsuario().getNombre(),
                    track.getPortada(),
                    duracion,
                    totalLikes,
                    like.getFecLike()
            ));
        }
        return resultado;
    }
}

