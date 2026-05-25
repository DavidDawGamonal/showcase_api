package com.EDLM.showcase.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.EDLM.showcase.DTO.DescargaData;
import com.EDLM.showcase.services.DescargaService;
import com.EDLM.showcase.services.TrackService;

/**
 * Controlador REST que expone los endpoints relacionados con las descargas.
 * Base URL: /descargas
 *
 * @author Elvis David Lara Manrique <a href=
 *         "mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@CrossOrigin(origins = "*")
@RestController
public class DescargaController {

	private DescargaService descargaService;
	private TrackService trackService;

	/**
	 * Constructor con inyección de dependencias.
	 *
	 * @param descargaService Servicio de descargas
	 * @param trackService    Servicio de tracks — necesario para obtener la ruta
	 *                        del archivo
	 */
	public DescargaController(DescargaService descargaService, TrackService trackService) {
		this.descargaService = descargaService;
		this.trackService = trackService;
	}

	/**
	 * Registra la descarga en BD y devuelve el archivo de audio para que el
	 * navegador lo descargue. La cabecera Content-Disposition: attachment fuerza la
	 * descarga en lugar de la reproducción en el navegador.
	 *
	 * @param id        ID del track a descargar
	 * @param idUsuario ID del usuario que realiza la descarga
	 * @return El archivo de audio con cabecera de descarga
	 * @throws IOException Si no se puede leer el archivo
	 */
	@GetMapping("/descargas/descargar/{id}")
	public ResponseEntity<Resource> descargar(@PathVariable Integer id, @RequestParam Integer idUsuario)
			throws IOException {
		// Registramos la descarga en BD
		descargaService.registrarDescarga(idUsuario, id);

		// Obtenemos la ruta del archivo igual que en getInstrumental
		String rutaBase = System.getProperty("user.dir");//Obtiene la ruta absoluta del directorio donde está ejecutándose el servidor
		String ruta = trackService.getInstrumental(id);//Consulta la BD y devuelve la ruta relativa del archivo guardada en la columna ruta de la tabla tracks
		Path rutaCompleta = Paths.get(rutaBase, ruta);//Une la ruta base del servidor con la ruta relativa del archivo para obtener la ruta completa en disco
		Resource resource = new UrlResource(rutaCompleta.toUri());//Convierte la ruta del archivo a un objeto Resource que Spring puede servir como respuesta HTTP — es la forma que tiene Spring de enviar archivos al cliente.
		String contentType = Files.probeContentType(rutaCompleta);//Detecta automáticamente el tipo MIME del archivo según su extensión — audio/mpeg para mp3, audio/wav para wav, etc. Si no puede detectarlo devuelve null y se usa audio/mpeg por defecto.

		// Content-Disposition: attachment fuerza la descarga en lugar de reproducción
		// El nombre del archivo descargado será el nombre real del archivo en disco
		return ResponseEntity.ok()//Construye la respuesta HTTP con tres partes
				.contentType(MediaType.parseMediaType(contentType != null ? contentType : "audio/mpeg"))//indica al navegador qué tipo de archivo es
				.header("Content-Disposition", "attachment; filename=\"" + rutaCompleta.getFileName() + "\"")// le dice al navegador que descargue el archivo en lugar de abrirlo, y con qué nombre guardarlo
				.body(resource);// el contenido del archivo en sí
	}

	/**
	 * Devuelve el número total de descargas realizadas por un usuario. Se usa para
	 * las estadísticas del perfil del usuario.
	 *
	 * @param idUsuario ID del usuario
	 * @return Número total de descargas del usuario
	 */
	@GetMapping("/descargas/contarByUsuario")
	public Integer contarDescargasByUsuario(@RequestParam Integer idUsuario) {
		return descargaService.contarDescargasByUsuario(idUsuario);
	}

	/**
	 * Devuelve el número total de descargas de un track.
	 *
	 * @param idTrack ID del track
	 * @return Número total de descargas del track
	 */
	@GetMapping("/descargas/contarByTrack")
	public Integer contarDescargasByTrack(@RequestParam Integer idTrack) {
		return descargaService.contarDescargasByTrack(idTrack);
	}

	/**
	 * Devuelve el historial de descargas de un usuario ordenado por fecha
	 * descendente. Se usa para mostrar la lista de tracks descargados en el perfil
	 * del usuario.
	 *
	 * @param idUsuario ID del usuario
	 * @return Lista de descargas del usuario
	 */
	@GetMapping("/descargas/historial")
	public List<DescargaData> getDescargasByUsuario(@RequestParam Integer idUsuario) {
		return descargaService.getDescargasByUsuario(idUsuario);
	}
}