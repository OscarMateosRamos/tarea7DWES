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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.servicios.ServiciosCestaCompra;
import com.oscar.vivero.servicios.ServiciosEjemplar;
import com.oscar.vivero.servicios.ServiciosPedido;
import com.oscar.vivero.servicios.ServiciosPlanta;

@SessionAttributes("cestaCompra")
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

		List<Planta> plantas = servPlanta.obtenerPlantasConEjemplares();

		for (Planta planta : plantas) {
			long cantidadDisponible = planta.getEjemplares().stream().filter(Ejemplar::isDisponible).count();
			planta.setCantidadDisponible(cantidadDisponible);
		}

		CestaCompra cestaCompra = new CestaCompra();

		Map<String, Integer> productosEnCesta = new HashMap<>();
		for (Planta planta : plantas) {
			productosEnCesta.put(planta.getCodigo(), 0);
		}

		model.addAttribute("productosEnCesta", productosEnCesta);

		model.addAttribute("plantas", plantas);
		model.addAttribute("pedido", cestaCompra);

		return "RealizarPedido";
	}

//	@PostMapping("/CamposPedido")
//	public String procesarPedido(@ModelAttribute Pedido pedido, Model model) {
//	if (pedido.getCantidades() == null || pedido.getCantidades().isEmpty()) {
//		model.addAttribute("mensajeError", "Debe seleccionar al menos un ejemplar.");
//			return "RealizarPedido";
//	}
//
//		try {
//			pedido.getCantidades().forEach((codigoPlanta, cantidad) -> {
//			if (cantidad > 0) {
//				Planta planta = servPlanta.buscarPlantaPorCodigo(codigoPlanta);
//					if (planta != null) {
//					long cantidadDisponible = planta.getCantidadDisponible();
//					if (cantidad <= cantidadDisponible) {
//							List<Ejemplar> ejemplaresDisponibles = planta.getEjemplares().stream()
//								.filter(Ejemplar::isDisponible).collect(Collectors.toList());
//
//						for (int i = 0; i < cantidad; i++) {
//							if (!ejemplaresDisponibles.isEmpty()) {
//								Ejemplar ejemplar = ejemplaresDisponibles.remove(0);
//
//								Mensaje anotacion = new Mensaje();
//								anotacion.setEjemplar(ejemplar);
//								anotacion.setFechahora(new java.sql.Date(System.currentTimeMillis()));
//								pedido.getAnotacion().add(anotacion);
//							}
//						}
//					} else {
//						throw new RuntimeException("Cantidad solicitada excede la disponible.");
//						}
//				}
//			}
//
//			servPedido.insertarPedido(pedido);
//			model.addAttribute("mensajeExito", "Pedido realizado con éxito.");
//		} catch (Exception e) {
//		model.addAttribute("mensajeError", "Error al procesar el pedido: " + e.getMessage());
//		}
//
//		model.addAttribute("pedido", pedido);
//		model.addAttribute("ejemplaresEnPedido", pedido.getEjemplares());
//
//		return "CestaCompra";
//	}

	@PostMapping("/añadirACesta")
	public String añadirACesta(@RequestParam("codigo") String codigo, @RequestParam("cantidad") int cantidad,
			Model model) {

		if (codigo != null && cantidad > 0) {
			CestaCompra cestaCompra = (CestaCompra) model.getAttribute("cestaCompra");

			if (cestaCompra != null) {

				cestaCompra.agregarPlanta(codigo, cantidad);

				model.addAttribute("success", "Producto añadido a la cesta con éxito.");
			} else {
				model.addAttribute("error", "No se encontró la cesta.");
			}
		} else {
			model.addAttribute("error", "Error al añadir a la cesta.");
		}

		return "redirect:/RealizarPedido";
	}

}
