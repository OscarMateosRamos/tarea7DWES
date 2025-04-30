package com.oscar.vivero;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
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

	@PostMapping("/HacerPedido")
	public String HacerPedido(HttpSession session, Model model) {

		String usuario = (String) session.getAttribute("usuario");
		if (usuario == null) {
			model.addAttribute("error", "Debes estar autenticado para realizar un pedido.");
			return "login";
		}

		ArrayList<CestaCompra> cestaCompra = (ArrayList<CestaCompra>) session.getAttribute("lista");

		if (cestaCompra == null || cestaCompra.isEmpty()) {
			model.addAttribute("mensaje", "No tienes productos en la cesta para realizar un pedido.");
			return "RealizarPedido";
		}

		for (CestaCompra item : cestaCompra) {
			String codigoPlanta = item.getCodigoPlanta();

			List<Ejemplar> ejemplaresDisponibles = ejemplarserv.obtenerEjemplaresDisponiblesPorPlanta(codigoPlanta);
			int cantidadRestante = item.getCantidad();
			System.out.println("Codigo de Planta: " + codigoPlanta + "Cantidad: " + item.getCantidad());
			for (Ejemplar ejemplar : ejemplaresDisponibles) {

				if (cantidadRestante > 0 && ejemplar.isDisponible()) {
					ejemplar.setDisponible(false);
					cantidadRestante--;

				}
			}

			if (cantidadRestante > 0) {
				model.addAttribute("mensaje",
						"No hay suficientes ejemplares disponibles para la planta " + codigoPlanta);
				return "RealizarPedido";
			}
		}

		model.addAttribute("mensaje", "Pedido realizado con éxito.");
		return "RealizarPedido";
	}

}
