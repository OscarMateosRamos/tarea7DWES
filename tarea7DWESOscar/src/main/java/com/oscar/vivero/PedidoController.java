package com.oscar.vivero;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.servicios.ServiciosEjemplar;
import com.oscar.vivero.servicios.ServiciosPedido;

@Controller
public class PedidoController {

	@Autowired
	private ServiciosPedido servPedido;

	@Autowired
	private ServiciosEjemplar servEjemplar;

	@PostMapping("/CamposPedido")
	public String procesarPedido(@ModelAttribute Pedido pedido, Model model) {

		servPedido.insertarPedido(pedido);

		model.addAttribute("pedido", pedido);
		return "CestaCompra";
	}
	
	

	@GetMapping("/RealizarPedido")
	public String mostrarRealizarPedido(Model model) {

		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();
		model.addAttribute("ejemplares", ejemplares);
		model.addAttribute("pedido", new Pedido());
		return "RealizarPedido";
	}

//	@GetMapping("/PedidoRealizado")
//	public String mostrarRealizarPedido(Model model) {
//
//		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();
//		model.addAttribute("ejemplares", ejemplares);
//		model.addAttribute("pedido", new Pedido());
//		return "RealizarPedido";
//	}

}
