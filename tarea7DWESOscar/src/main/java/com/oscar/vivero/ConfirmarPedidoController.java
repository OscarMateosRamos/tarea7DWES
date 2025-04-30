package com.oscar.vivero;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Cliente;
import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.servicios.ServiciosCliente;
import com.oscar.vivero.servicios.ServiciosEjemplar;
import com.oscar.vivero.servicios.ServiciosPedido;
import com.oscar.vivero.servicios.ServiciosPlanta;

import jakarta.servlet.http.HttpSession;

@Controller
public class ConfirmarPedidoController {

	@Autowired
	private ServiciosPedido pedidoserv;

	@Autowired
	private ServiciosCliente clienteserv;

	@Autowired
	private ServiciosPlanta plantaserv;

	@Autowired
	private ServiciosEjemplar ejemplarserv;

	@GetMapping("/confirmarPedido")
	public String confirmarPedido(HttpSession session, Model model) {

		String usuario = (String) session.getAttribute("usuario");

		if (usuario == null) {
			model.addAttribute("error", "Debes estar autenticado para realizar un pedido.");
			return "login";
		}

		ArrayList<CestaCompra> lista = (ArrayList<CestaCompra>) session.getAttribute("lista");

		if (lista == null || lista.isEmpty()) {
			model.addAttribute("mensaje", "No tienes productos en la cesta para confirmar.");
		} else {
			model.addAttribute("lista", lista);
		}

		return "ConfirmarPedido";
	}

	@PostMapping("/RealizarPedido")
	public String realizarPedido(@AuthenticationPrincipal UserDetails userDetails, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Cliente cliente = obtenerClienteAutenticado();

		if (cliente == null) {
			redirectAttributes.addFlashAttribute("mensaje", "Debes estar autenticado para realizar un pedido.");
			return "redirect:/login";
		}

		List<CestaCompra> lista = (List<CestaCompra>) session.getAttribute("lista");

		if (lista == null || lista.isEmpty()) {
			redirectAttributes.addFlashAttribute("mensaje",
					"El carrito está vacío. No se ha podido confirmar el pedido.");
			return "redirect:/confirmarPedido";
		}

		LocalDateTime now = LocalDateTime.now();
		Date date = (Date) Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setFecha(date);

		List<Ejemplar> ejemplaresVendidos = new ArrayList<>();

		for (CestaCompra item : lista) {
			Planta tipoPlanta = plantaserv.buscarPlantaPorCodigo(item.getCodigoPlanta());

			if (tipoPlanta == null) {
				redirectAttributes.addFlashAttribute("mensaje", "Planta no encontrada.");
				return "redirect:/confirmarPedido";
			}

			int cantidadSolicitada = item.getCantidad();
			int stockActual = (int) tipoPlanta.getCantidadDisponible();

			if (stockActual < cantidadSolicitada) {
				redirectAttributes.addFlashAttribute("mensaje",
						"No hay suficiente stock para la planta: " + tipoPlanta.getNombrecomun());
				return "redirect:/confirmarPedido";
			}

			tipoPlanta.setCantidadDisponible(stockActual - cantidadSolicitada);
			plantaserv.insertarPlanta(tipoPlanta);

			List<Ejemplar> ejemplaresDisponibles = ejemplarserv
					.obtenerEjemplaresDisponiblesPorPlanta(tipoPlanta.getCodigo());

			for (int i = 0; i < cantidadSolicitada; i++) {
				Ejemplar ejemplar = ejemplaresDisponibles.get(i);
				ejemplar.setDisponible(false);
				ejemplarserv.actualizarEjemplarAlRealizarPedido(ejemplar, null);
				ejemplaresVendidos.add(ejemplar);
			}
		}

		pedido.setEjemplares(ejemplaresVendidos);
		pedidoserv.insertar(pedido);

		session.removeAttribute("lista");
		redirectAttributes.addFlashAttribute("mensaje", "Pedido realizado correctamente. ¡Gracias por tu compra!");

		return "redirect:/confirmarPedido";
	}

	public Cliente obtenerClienteAutenticado() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();

			return clienteserv.buscarPorUsuario(username);
		}
		return null;

	}

}
