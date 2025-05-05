package com.oscar.vivero;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Cliente;
import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.modelo.Planta;
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
	private ServiciosCredenciales credencialesserv;

	// Confirmar ...
//	@PostMapping("/HacerPedido")
//	public String HacerPedido(HttpSession session, Model model) {
//		// Añadir un nuevo Pedido
//		String usuario = (String) session.getAttribute("usuario");
//		ArrayList<CestaCompra> lista = (ArrayList<CestaCompra>) session.getAttribute("lista");
//
//		ArrayList<Ejemplar> ejemplaresPedido = new ArrayList<Ejemplar>();
//
//		Pedido p = new Pedido();
////		
////		p.setCliente(clienteserv.buscarClientePorIdCredencial(credencialesserv.buscarCredencialPorUsuario(usuario).getId()));
////		
//		System.out.println("Id del cliente"+credencialesserv.buscarCredencialPorUsuario(usuario).getId());
//		
//		System.out.println("Id del cliente"+clienteserv.buscarClientePorIdCredencial(credencialesserv.buscarCredencialPorUsuario(usuario).getId()));
//		
//		System.out.println("Id del cliente"+credencialesserv.buscarCredencialPorUsuario(usuario).getId());
//		p.setFecha(Date.valueOf(LocalDate.now()));
//
//		if (lista != null && usuario != null) {
//			lista.removeIf(item -> item.getUsuario().equals(usuario));
////			 lista.removeIf(item -> item.getCodigoPlanta().equals(codigo));
//			session.setAttribute("lista", lista);
//		}
//
////		// De la tabla Cesta Compra para que controla todas las cestas
//		ArrayList<CestaCompra> cestaCompra = (ArrayList<CestaCompra>) cestaserv.verCestaCompra();
//
//		if (cestaCompra == null || cestaCompra.isEmpty()) {
//			model.addAttribute("mensaje", "No tienes productos en la cesta para realizar un pedido.");
//			return "/cliente/RealizarPedido";
//		}
//
//		for (CestaCompra item : cestaCompra) {
//			if (item.getUsuario().equalsIgnoreCase(usuario)) {
//				cestaserv.eliminarDeCesta(item.getCodigoPlanta(), usuario);
//			}
//
//			plantaserv.actualizarCantidadDisponible(plantaserv.buscarPlantaPorCodigo(item.getCodigoPlanta()),
//					(int) (plantaserv.buscarPlantaPorCodigo(item.getCodigoPlanta()).getCantidadDisponible()
//							- item.getCantidad()));
//
//			java.util.List<Ejemplar> ejemplaresDisp = ejemplarserv
//					.obtenerEjemplaresDisponiblesPorPlanta(item.getCodigoPlanta());
//
//			for (int i = 0; i < item.getCantidad(); i++) {
//
//				System.out.println("Ejemplar: " + ejemplaresDisp.get(i).getNombre());
//
//				ejemplaresDisp.get(i).setDisponible(false);
//
//				ejemplaresPedido.add(ejemplaresDisp.get(i));
//
//				ejemplarserv.actualizarEjemplarAlRealizarPedido(ejemplaresDisp.get(i), "Pedido realizado");
//			}
//		}
//		p.setEjemplares(ejemplaresPedido);
//
//		pedidoserv.insertar(p);
//
//		model.addAttribute("mensaje", "Pedido realizado con éxito.");
//		return "/cliente/RealizarPedido";
//	}

	@PostMapping("/HacerPedido")
	public String HacerPedido(HttpSession session, Model model) {
		String usuario = (String) session.getAttribute("usuario");
		if (usuario == null) {
			model.addAttribute("mensaje", "Usuario no identificado.");
			return "/cliente/RealizarPedido";
		}

		Cliente cliente = obtenerClienteDesdeSesion(usuario);
		if (cliente == null) {
			model.addAttribute("mensaje", "No se pudo identificar al cliente.");
			return "/cliente/RealizarPedido";
		}

		ArrayList<CestaCompra> cestaCompra = (ArrayList<CestaCompra>) cestaserv.verCestaCompra();
		if (cestaCompra == null || cestaCompra.isEmpty()) {
			model.addAttribute("mensaje", "No tienes productos en la cesta para realizar un pedido.");
			return "/cliente/RealizarPedido";
		}

		ArrayList<Ejemplar> ejemplaresPedido = new ArrayList<>();

		for (CestaCompra item : cestaCompra) {
			if (!item.getUsuario().equalsIgnoreCase(usuario))
				continue;

			List<Ejemplar> ejemplaresProcesados = procesarEjemplaresDelItem(item, model);
			if (ejemplaresProcesados == null) {
				return "/cliente/RealizarPedido"; // Ya se agregó mensaje de error
			}

			ejemplaresPedido.addAll(ejemplaresProcesados);
			cestaserv.eliminarDeCesta(item.getCodigoPlanta(), usuario);
		}

		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setFecha(Date.valueOf(LocalDate.now()));
		pedido.setEjemplares(ejemplaresPedido);

		pedidoserv.insertar(pedido);
		limpiarListaSesion(session, usuario);

		model.addAttribute("mensaje", "Pedido realizado con éxito.");
		return "/cliente/RealizarPedido";
	}

	private List<Ejemplar> procesarEjemplaresDelItem(CestaCompra item, Model model) {
		List<Ejemplar> ejemplaresDisponibles = ejemplarserv
				.obtenerEjemplaresDisponiblesPorPlanta(item.getCodigoPlanta());

		if (ejemplaresDisponibles.size() < item.getCantidad()) {
			model.addAttribute("mensaje",
					"No hay suficientes ejemplares disponibles para la planta " + item.getCodigoPlanta() + ".");
			return null;
		}

		List<Ejemplar> ejemplaresProcesados = new ArrayList<>();
		for (int i = 0; i < item.getCantidad(); i++) {
			Ejemplar ejemplar = ejemplaresDisponibles.get(i);
			ejemplar.setDisponible(false);
			ejemplarserv.actualizarEjemplarAlRealizarPedido(ejemplar, "Pedido realizado");
			ejemplaresProcesados.add(ejemplar);
		}

		Planta planta = plantaserv.buscarPlantaPorCodigo(item.getCodigoPlanta());
		plantaserv.actualizarCantidadDisponible(planta, (int) (planta.getCantidadDisponible() - item.getCantidad()));

		return ejemplaresProcesados;
	}

}
