package com.oscar.vivero;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.servicios.ServiciosPedido;

@Controller
public class PedidoController {

	@Autowired
	ServiciosPedido servpedido;

	@PostMapping("/CamposPedido")
	public String RegistrarCliente(@ModelAttribute Pedido RealizarPedido, Model model) {

		Pedido p = new Pedido();

		Date fechahora = RealizarPedido.getFecha();
		List<Ejemplar> ejemplares = RealizarPedido.getEjemplares();
		
		fechahora = Date.valueOf(LocalDate.now());

		p.setFecha(fechahora);
		p.setEjemplares(ejemplares);
		
		servpedido.insertarPedido(p);

		return "/RealizarPedido";
	}

	@GetMapping("/PedidoRealizado")
	public String mostrarRegistroCliente(Model model) {
		model.addAttribute("pedido", new Pedido());
		return "RealizarPedido";
	}

}
