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
import com.oscar.vivero.modelo.LineaPedido;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.modelo.Planta;
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

		List<LineaPedido> lista = (List<LineaPedido>) session.getAttribute("lista");
		if (lista == null || lista.isEmpty()) {
			model.addAttribute("mensaje", "No tienes productos en la cesta para realizar un pedido.");
			return "redirect:/RealizarPedido";
		}

		String usuario = (String) session.getAttribute("usuario");

		Cliente cliente = clienteserv
				.buscarClientePorIdCredencial(credencialesserv.buscarCredencialPorUsuario(usuario).getId());

		Pedido pedido = new Pedido();

		pedido.setIdCliente(cliente.getId());
		pedido.setFecha(Date.valueOf(LocalDate.now()));
		
		
		pedidoserv.insertar(pedido);
		
		long idPedido = pedido.getId();
		
		System.out.println("ID DE PEDIDO"+idPedido);
		for (LineaPedido l : lista) {
			Planta planta = plantaserv.buscarPlantaPorCodigo(l.getCodigoPlanta());
			if (planta.getCantidadDisponible() < l.getCantidad()) {
				model.addAttribute("mensaje", "No hay suficientes existencias para " + planta.getNombrecomun());
				return "redirect:/RealizarPedido";
			}

			// System.out.println("Cantidad disponible" + planta.getCantidadDisponible());
			planta.setCantidadDisponible(planta.getCantidadDisponible() - l.getCantidad());

			plantaserv.modificarPlanta(planta);
			
			
//			System.out.println("Cantidad disponible" + planta.getCantidadDisponible());

			List<Ejemplar> ejemplares = ejemplarserv.listaejemplaresPorTipoPlanta(l.getCodigoPlanta());

			int contador = 0;

			for (Ejemplar ej : ejemplares) {
				if (ej.isDisponible() && contador < l.getCantidad()) {
					System.out.println("Ejemplar: " + ej.getNombre());
					ej.setDisponible(false);
					ej.setIdPedido(idPedido);
					ejemplarserv.modificarEjemplar(ej);
					contador++;
				}
			}

		}

		session.removeAttribute("lista");
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
