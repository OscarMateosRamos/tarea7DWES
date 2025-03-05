package com.oscar.vivero;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.servicios.ServiciosPedido;
import com.oscar.vivero.servicios.ServiciosPlanta;

@Controller
public class PedidoController {

	@Autowired
	ServiciosPedido servpedido;

	@Autowired
	ServiciosPlanta servPlanta;

	@PostMapping("/CamposPedido")
	public String RealizarPedido(@ModelAttribute Pedido RealizarPedido, Model model) {

		Pedido p = new Pedido();
		Date fechahora = Date.valueOf(LocalDate.now());
		p.setFecha(fechahora);

		Map<String, Integer> cantidades = RealizarPedido.getCantidades();

		List<Ejemplar> ejemplares = new LinkedList<>();

		for (Map.Entry<String, Integer> entry : cantidades.entrySet()) {
			String codigoPlanta = entry.getKey();
			Integer cantidad = entry.getValue();

			Planta planta = servPlanta.buscarPlantaPorCodigo(codigoPlanta);
			if (planta != null) {

				List<Ejemplar> ejemplaresDisponibles = planta.getEjemplares();

				for (int i = 0; i < cantidad; i++) {
					if (!ejemplaresDisponibles.isEmpty()) {
						Ejemplar ejemplar = ejemplaresDisponibles.remove(0);
						ejemplares.add(ejemplar);
					}
				}
			}
		}

		p.setEjemplares(ejemplares);
		servpedido.insertarPedido(p);

		return "RealizarPedido";
	}

	@GetMapping("/PedidoRealizado")
	public String mostrarRegistroCliente(Model model) {

		List<Planta> plantas = servPlanta.vertodasPlantas();
		model.addAttribute("plantas", plantas);

		model.addAttribute("pedido", new Pedido());

		return "RealizarPedido";
	}
}
