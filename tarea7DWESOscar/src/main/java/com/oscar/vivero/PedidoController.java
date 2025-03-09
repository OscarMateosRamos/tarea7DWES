package com.oscar.vivero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.servicios.ServiciosPedido;

@Controller
public class PedidoController {

	@Autowired
	private ServiciosPedido servPedido;

	@PostMapping("/CamposPedido")
	public String procesarPedido(@ModelAttribute Pedido pedido, Model model) {

		servPedido.insertarPedido(pedido);

		model.addAttribute("pedido", pedido);

		return "ConfirmarPedido";
	}

	@GetMapping("/PedidoRealizado")
	public String mostrarRealizarPedido(Model model) {
		model.addAttribute("pedido", new Pedido());
		return "RealizarPedido";
	}

}
