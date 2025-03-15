package com.oscar.vivero;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.servicios.ServiciosCestaCompra;
import com.oscar.vivero.servicios.ServiciosEjemplar;
import com.oscar.vivero.servicios.ServiciosPedido;
import com.oscar.vivero.servicios.ServiciosPlanta;

@Controller
public class PedidoController {

	@Autowired
	private ServiciosPedido servPedido;

	@Autowired
	private ServiciosEjemplar servEjemplar;

	@Autowired
	private ServiciosPlanta servPlanta;

	@Autowired
	private ServiciosCestaCompra servCesta;

	@GetMapping("/RealizarPedido")
	public String mostrarRealizarPedido(Model model) {
		// Obtener todas las plantas con sus ejemplares disponibles
		List<Planta> plantas = servPlanta.obtenerPlantasConEjemplares();

		// Filtrar los ejemplares disponibles (marcados como 'disponible' = true)
		for (Planta planta : plantas) {
			// Filtramos los ejemplares que están disponibles
			long cantidadDisponible = planta.getEjemplares().stream().filter(Ejemplar::isDisponible).count(); // Contamos
																												// cuántos
																												// ejemplares
																												// están
																												// disponibles

			// Establecemos la cantidad disponible en la planta
			planta.setCantidadDisponible(cantidadDisponible);
		}

		// Crear un nuevo objeto Pedido con un mapa vacío de cantidades
		Pedido pedido = new Pedido();
		Map<String, Integer> cantidades = new HashMap<>();
		for (Planta planta : plantas) {
			cantidades.put(planta.getCodigo(), 0); // Inicializamos las cantidades en 0
		}
		pedido.setCantidades(cantidades);

		// Agregar las plantas y el objeto Pedido al modelo
		model.addAttribute("plantas", plantas);
		model.addAttribute("pedido", pedido); // El objeto Pedido que contiene el mapa de cantidades

		// Retornamos el nombre de la vista para mostrar el formulario
		return "RealizarPedido"; // Nombre de la vista (RealizarPedido.html)
	}

	@PostMapping("/CamposPedido")
	public String procesarPedido(@ModelAttribute Pedido pedido, Model model) {
		if (pedido.getCantidades() != null && !pedido.getCantidades().isEmpty()) {
			try {

				pedido.getCantidades().forEach((codigoPlanta, cantidad) -> {
					Planta planta = servPlanta.buscarPlantaPorCodigo(codigoPlanta);
					if (planta != null) {

						long cantidadDisponible = planta.getCantidadDisponible();
						if (cantidad > 0 && cantidad <= cantidadDisponible) {

							List<Ejemplar> ejemplaresDisponibles = planta.getEjemplares().stream()
									.filter(Ejemplar::isDisponible).collect(Collectors.toList());

							// Si hay ejemplares suficientes disponibles, los agregamos al pedido
							for (int i = 0; i < cantidad; i++) {
								if (!ejemplaresDisponibles.isEmpty()) {
									Ejemplar ejemplar = ejemplaresDisponibles.remove(0);
									pedido.agregarEjemplar(ejemplar, 1);

									// Crear anotación de ejemplar en el pedido
									Mensaje anotacion = new Mensaje();
									anotacion.setEjemplar(ejemplar);
									anotacion.setFechahora(new java.sql.Date(System.currentTimeMillis()));
									pedido.getAnotacion().add(anotacion);
								}
							}
						} else {
							// Si la cantidad solicitada es mayor que la disponible, lanzamos un error
							throw new RuntimeException("La cantidad solicitada para la planta "
									+ planta.getNombrecomun() + " excede la cantidad disponible.");
						}
					}
				});

				// Guardamos el pedido
				servPedido.insertarPedido(pedido);

				// Agregamos un mensaje de éxito
				model.addAttribute("mensajeExito", "Pedido realizado con éxito.");
			} catch (Exception e) {
				// Si ocurre un error, mostramos el mensaje de error
				model.addAttribute("mensajeError", "Ocurrió un error al procesar el pedido: " + e.getMessage());
			}
		} else {
			// Si no se seleccionó ninguna cantidad, mostramos un mensaje de error
			model.addAttribute("mensajeError", "Debe seleccionar al menos un ejemplar.");
		}

		// Regresamos a la vista de la cesta de compra con el pedido y los ejemplares
		// agregados
		model.addAttribute("pedido", pedido);
		model.addAttribute("ejemplaresEnPedido", pedido.getEjemplares());

		return "CestaCompra"; // Vista que muestra el resumen del pedido
	}

	
	@PostMapping("/añadirACesta")
	public String añadirACesta(@RequestParam("plantaId") String plantaId, @RequestParam("cantidad") Integer cantidad, Model model) {
	    // Buscar la planta
	    Planta planta = servPlanta.buscarPlantaPorCodigo(plantaId);

	    if (planta == null || cantidad == null || cantidad <= 0) {
	        model.addAttribute("error", "No se pudo añadir a la cesta. Datos incorrectos.");
	        return "redirect:/RealizarPedido"; // Redirigir a la vista de compra
	    }

	    // Agregar la planta a la cesta
	    servCesta.agregarPlanta(planta, cantidad);

	    model.addAttribute("success", "El producto ha sido añadido a la cesta con éxito.");

	    return "redirect:/CestaCompra"; // Redirigir a la vista de la cesta
	}

}
