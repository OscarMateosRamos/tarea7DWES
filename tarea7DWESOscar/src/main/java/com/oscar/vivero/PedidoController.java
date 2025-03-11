package com.oscar.vivero;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.servicios.ServiciosEjemplar;
import com.oscar.vivero.servicios.ServiciosPedido;

@Controller
public class PedidoController {

	@Autowired
	private ServiciosPedido servPedido;

	@Autowired
	private ServiciosEjemplar servEjemplar;

	@PostMapping("/CamposPedido")
	public String procesarPedido(@ModelAttribute Pedido pedido, Model model) {

		if (pedido.getCantidades() != null && !pedido.getCantidades().isEmpty()) {
			try {
				pedido.getCantidades().forEach((ejemplarCodigo, cantidad) -> {
					Long codigo = Long.valueOf(ejemplarCodigo);
					Ejemplar ejemplar = servEjemplar.buscarPorId(codigo);

					if (ejemplar != null && cantidad > 0) {
						pedido.agregarEjemplar(ejemplar, cantidad);

						Mensaje anotacion = new Mensaje();
						anotacion.setEjemplar(ejemplar);
						anotacion.getEjemplar().setNombre(servEjemplar.obtenerNombrePorId(codigo));

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

		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();
		model.addAttribute("ejemplares", ejemplares);
		model.addAttribute("pedido", new Pedido());
		return "RealizarPedido";
	}

	
//	@GetMapping("/PedidoRealizado")
//	public String mostrarRealizarPedido(Model model) {
//
//		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();
//		model.addAttribute("ejemplares", ejemplares);
//		model.addAttribute("pedido", new Pedido());
//		return "RealizarPedido";
//	}

}
