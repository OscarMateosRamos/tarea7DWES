package com.oscar.vivero;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.servicios.ServiciosPedido;

import jakarta.servlet.http.HttpSession;

@Controller
public class ConfirmarPedidoController {

	@Autowired
	private ServiciosPedido pedidoserv;

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

	
	
}
