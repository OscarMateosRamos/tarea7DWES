package com.oscar.vivero;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.servicios.ServiciosCestaCompra;
import com.oscar.vivero.servicios.ServiciosEjemplar;
import com.oscar.vivero.servicios.ServiciosPedido;
import com.oscar.vivero.servicios.ServiciosPlanta;

import jakarta.servlet.http.HttpSession;

@Controller
public class CestaCompraController {

	@Autowired
	private ServiciosPedido servPedido;

	@Autowired
	private ServiciosPlanta servPlanta;

	@Autowired
	private ServiciosCestaCompra servCesta;

	@Autowired
	private ServiciosEjemplar servEjemplar;

	@GetMapping("/CestaCompra")
	public String mostrarCesta(HttpSession session, Model model) {

		ArrayList<CestaCompra> lista = (ArrayList<CestaCompra>) session.getAttribute("lista");

		if (lista == null || lista.isEmpty()) {
			model.addAttribute("mensaje", "Tu cesta está vacía.");
		} else {
			model.addAttribute("lista", lista);
		}

		return "CestaCompra";
	}

	@GetMapping("/retirarDeCesta/{codigoPlanta}")
	public String retirarDeCesta(@PathVariable("codigoPlanta") String codigo, HttpSession session, Model model) {

		ArrayList<CestaCompra> lista = (ArrayList<CestaCompra>) session.getAttribute("lista");
		String usuario = (String) session.getAttribute("usuario");

		if (lista != null && usuario != null) {

			lista.removeIf(item -> item.getCodigoPlanta().equals(codigo));
			session.setAttribute("lista", lista);

			servCesta.eliminarDeCesta(codigo, usuario);
		}

		return "redirect:/CestaCompra";
	}

	@GetMapping("/ConfirmarPedido")
	public String confirmarPedido(HttpSession session, Model model) {
		ArrayList<CestaCompra> lista = (ArrayList<CestaCompra>) session.getAttribute("lista");

		if (lista == null || lista.isEmpty()) {
			model.addAttribute("mensaje", "No tienes productos en la cesta para confirmar.");
			return "ConfirmarPedido";
		}

		Pedido p = new Pedido();
		p.setFecha(Date.valueOf(LocalDate.now()));

		List<Ejemplar> todosEjemplares = new ArrayList<>();

		for (CestaCompra l : lista) {
			List<Ejemplar> ejemplaresTienda = servEjemplar.obtenerEjemplaresDisponiblesPorPlanta(l.getCodigoPlanta());

			int cantidad = l.getCantidad();
			List<Ejemplar> seleccionados = new ArrayList<>();

			for (Ejemplar ej : ejemplaresTienda) {
				if (ej.isDisponible() && cantidad > 0) {
					ej.setDisponible(false);
					seleccionados.add(ej);
					cantidad--;
				}
			}

			todosEjemplares.addAll(seleccionados);
		}

		p.setEjemplares(todosEjemplares);
		servPedido.insertarPedido(p);

		session.setAttribute("ultimoPedido", p);
		model.addAttribute("lista", lista);

		return "ConfirmarPedido";
	}

}
