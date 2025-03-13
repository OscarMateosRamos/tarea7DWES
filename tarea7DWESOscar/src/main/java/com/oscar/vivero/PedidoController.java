package com.oscar.vivero;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.modelo.Planta;
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

	@PostMapping("/CamposPedido")
	public String procesarPedido(@ModelAttribute Pedido pedido, Model model) {

		if (pedido.getCantidades() != null && !pedido.getCantidades().isEmpty()) {
			try {
				pedido.getCantidades().forEach((ejemplarCodigo, cantidad) -> {
					Long codigo = Long.valueOf(ejemplarCodigo);
					Ejemplar ejemplar = servEjemplar.buscarPorId(codigo);

					
					if (ejemplar != null && ejemplar.isDisponible() && cantidad > 0) {
						pedido.agregarEjemplar(ejemplar, cantidad);

						Mensaje anotacion = new Mensaje();
						anotacion.setEjemplar(ejemplar);
						anotacion.getEjemplar().setNombre(servEjemplar.obtenerNombrePorId(codigo));
						anotacion.setFechahora(new java.sql.Date(System.currentTimeMillis()));
						pedido.getAnotacion().add(anotacion);
					}
				});

				
				servPedido.insertarPedido(pedido);
				model.addAttribute("mensajeExito", "Pedido realizado con éxito.");
			} catch (Exception e) {
				model.addAttribute("mensajeError", "Ocurrió un error al procesar el pedido.");
			}
		} else {
			model.addAttribute("mensajeError", "Debe seleccionar al menos un ejemplar.");
		}

		model.addAttribute("pedido", pedido);
		model.addAttribute("ejemplaresEnPedido", pedido.getEjemplares());

		return "CestaCompra";
	}

	@GetMapping("/RealizarPedido")
	public String mostrarRealizarPedido(Model model) {

		List<Planta> plantas = servPlanta.obtenerPlantasConEjemplares();

		for (Planta planta : plantas) {

			List<Ejemplar> ejemplaresDisponibles = planta.getEjemplares().stream().filter(Ejemplar::isDisponible)
					.collect(Collectors.toList());

			planta.setEjemplares(ejemplaresDisponibles);
		}

		model.addAttribute("plantas", plantas);
		model.addAttribute("pedido", new Pedido());
		return "RealizarPedido";
	}

}
