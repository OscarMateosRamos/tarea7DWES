package com.oscar.vivero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.servicios.ServiciosPedido;

import jakarta.servlet.http.HttpSession;

@Controller
public class CestaCompraController {

	@Autowired
	ServiciosPedido servPedido;

	@GetMapping("/CestaCompra")
	public String mostrarCestaCompra(HttpSession session, Model model) {

		CestaCompra cestaCompra = (CestaCompra) session.getAttribute("cestaCompra");
		if (cestaCompra == null) {
			cestaCompra = new CestaCompra();
			session.setAttribute("cestaCompra", cestaCompra);
		}

		model.addAttribute("productosEnCesta", cestaCompra.obtenerPedidos());
		model.addAttribute("total", cestaCompra.obtenerCantidadTotal());
		return "CestaCompra";
	}

	@GetMapping("/retirarDeCesta")
	public String retirarDeCesta(@RequestParam("idPedido") Long idPedido, HttpSession session, Model model) {
		if (idPedido == null || idPedido <= 0) {
			model.addAttribute("error", "ID de producto no válido.");
			return "CestaCompra";
		}

		CestaCompra cestaCompra = (CestaCompra) session.getAttribute("cestaCompra");
		if (cestaCompra == null) {
			model.addAttribute("error", "Tu cesta está vacía.");
			return "CestaCompra";
		}

		cestaCompra.retirarPedido(idPedido);

		model.addAttribute("productosEnCesta", cestaCompra.obtenerPedidos());
		model.addAttribute("total", cestaCompra.obtenerCantidadTotal());
		model.addAttribute("success", "Producto retirado de la cesta.");
		return "CestaCompra";
	}

	@GetMapping("/agregarACesta")
	public String agregarACesta(@RequestParam("idPedido") Long idPedido, HttpSession session, Model model) {
		if (idPedido == null || idPedido <= 0) {
			model.addAttribute("error", "ID de producto no válido.");
			return "CestaCompra";
		}

		Pedido pedido = servPedido.obtenerPedidoPorId(idPedido);
		if (pedido == null) {
			model.addAttribute("error", "Pedido no encontrado.");
			return "CestaCompra";
		}

		CestaCompra cestaCompra = (CestaCompra) session.getAttribute("cestaCompra");
		if (cestaCompra == null) {
			cestaCompra = new CestaCompra();
			session.setAttribute("cestaCompra", cestaCompra);
		}

		cestaCompra.agregarPedido(pedido);

		model.addAttribute("productosEnCesta", cestaCompra.obtenerPedidos());
		model.addAttribute("total", cestaCompra.obtenerCantidadTotal());
		model.addAttribute("success", "Producto añadido a la cesta.");

		return "CestaCompra";
	}
}
