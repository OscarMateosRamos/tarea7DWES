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
		int i = 0;
		int posicion = 0;

		if (lista != null) {

			for (CestaCompra l : lista) {

				if (l.getCodigoPlanta().equals(codigo)) {
					existe = true;
					posicion = i;
				} else {
					i = i++;
				}
			}

			lista.remove(posicion);
			session.setAttribute("lista", lista);

		}
		return "CestaCompra";
	}

	@GetMapping("/ConfirmarPedido")
	public void confirmarPedido(HttpSession session) {
		ArrayList<CestaCompra> lista = (ArrayList<CestaCompra>) session.getAttribute("lista");
		Pedido p = new Pedido();

		Ejemplar e = new Ejemplar();
		for (CestaCompra l : lista) {
			p.setFecha(Date.valueOf(LocalDate.now()));
			List<Ejemplar> ejemplaresTienda = servEjemplar.obtenerEjemplaresDisponiblesPorPlanta(l.getCodigoPlanta());

			ArrayList<Ejemplar> ejemplaresP = new ArrayList<Ejemplar>();

			int cantidad = l.getCantidad();

			for (Ejemplar ej : ejemplaresTienda) {
				if (ej.isDisponible() && cantidad > 0) {
					ej.setDisponible(false);
					ejemplaresP.add(ej);
					cantidad--;
				}
			}
			p.setEjemplares(ejemplaresTienda);

		}
		servPedido.insertarPedido(p);
	}
}
