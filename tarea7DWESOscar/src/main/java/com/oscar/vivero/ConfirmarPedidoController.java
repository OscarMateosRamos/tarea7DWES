package com.oscar.vivero;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.servicios.ServiciosCestaCompra;
import com.oscar.vivero.servicios.ServiciosCliente;
import com.oscar.vivero.servicios.ServiciosCredenciales;
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

	@Autowired
	private ServiciosCestaCompra cestaserv;

	@Autowired
	private ServiciosCredenciales credencialesserv;

	@PostMapping("/HacerPedido")
	public String HacerPedido(HttpSession session, Model model) {
//Añadir un nuevo Pedido
		String usuario = (String) session.getAttribute("usuario");
		ArrayList<CestaCompra> lista = (ArrayList<CestaCompra>) session.getAttribute("lista");

		Pedido p = new Pedido();

		p.setCliente(clienteserv.buscarClientePorId(credencialesserv.buscarCredencialPorUsuario(usuario).getId()));
		
		p.setFecha(Date.valueOf(LocalDate.now()));
		
	
		
		if (lista != null && usuario != null) {
			lista.removeIf(item -> item.getUsuario().equals(usuario));
			// lista.removeIf(item -> item.getCodigoPlanta().equals(codigo));
			session.setAttribute("lista", lista);

		}

//		// De la tabla Cesta Compra para que controla todas las cestas
		ArrayList<CestaCompra> cestaCompra = (ArrayList<CestaCompra>) cestaserv.verCestaCompra();

		if (cestaCompra == null || cestaCompra.isEmpty()) {
			model.addAttribute("mensaje", "No tienes productos en la cesta para realizar un pedido.");
			return "/cliente/RealizarPedido";
		}

		for (CestaCompra item : cestaCompra) {

			if (item.getUsuario().equalsIgnoreCase(usuario)) {
				cestaserv.eliminarDeCesta(item.getCodigoPlanta(), usuario);
			}
		}

		model.addAttribute("mensaje", "Pedido realizado con éxito.");
		return "/cliente/RealizarPedido";
	}

}
