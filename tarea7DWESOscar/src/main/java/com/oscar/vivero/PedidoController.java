package com.oscar.vivero;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.servicios.ServiciosCestaCompra;
import com.oscar.vivero.servicios.ServiciosEjemplar;
import com.oscar.vivero.servicios.ServiciosPedido;
import com.oscar.vivero.servicios.ServiciosPlanta;

import jakarta.servlet.http.HttpSession;

@SessionAttributes("cestaCompra")
@Controller
public class PedidoController {

	@Autowired
	private ServiciosPedido servPedido;

	@Autowired
	private ServiciosEjemplar servEjemplar;
	@Autowired
	private ServiciosPlanta servPlanta;

	@Autowired
	private ServiciosCestaCompra servCesta;

	@GetMapping("/RealizarPedido")
	public String mostrarRealizarPedido(HttpSession session, Model model) {

		List<Planta> plantas = servPlanta.obtenerPlantasConEjemplares();
		List<CestaCompra> lista = servCesta.verCestaCompra();
		String usuario = (String) session.getAttribute("usuario");

		// Para cada planta, calcular cantidad disponible real (descontando lo que hay
		// en la cesta)
		for (Planta planta : plantas) {
			long enCesta = 0;

			// Contar ejemplares disponibles
			long cantidadDisponible = planta.getEjemplares().stream().filter(Ejemplar::isDisponible).count();

			// Verificar si esta planta está en la cesta y sumar cuántos hay
			if (lista != null) {
				for (CestaCompra cestaCompra : lista) {
					if (cestaCompra.getCodigoPlanta().equalsIgnoreCase(planta.getCodigo())) {
						enCesta += cestaCompra.getCantidad();
					}
				}
			}

			;
			// Actualizar solo en memoria la cantidad disponible restante
			planta.setCantidadDisponible((int) (cantidadDisponible - enCesta));
		}

		// Cargar datos al modelo
		CestaCompra cestaCompra = new CestaCompra();
		model.addAttribute("plantas", plantas);
		model.addAttribute("pedido", cestaCompra);
		model.addAttribute("usuario", usuario);

		return "RealizarPedido";
	}

	@PostMapping("/añadirACesta")
	public String añadirACesta(@RequestParam("codigo") String codigo, @RequestParam("cantidad") int cantidad,
			HttpSession session, Model model) {

		ArrayList<CestaCompra> lista = (ArrayList<CestaCompra>) session.getAttribute("lista");

		if (lista == null) {
			lista = new ArrayList<>();
		}

		String usuario = (String) session.getAttribute("usuario");

		if (usuario == null) {
			model.addAttribute("error", "Debes estar autenticado para añadir productos a la cesta.");
			return "login";
		}

		boolean existe = false;

		for (CestaCompra item : lista) {
			if (item.getCodigoPlanta().equals(codigo)) {
				existe = true;
				item.setCantidad(item.getCantidad() + cantidad);
				servCesta.actualizarCesta(item);
				break;
			}
		}

		if (!existe) {
			CestaCompra c = new CestaCompra();
			c.setCodigoPlanta(codigo);
			c.setUsuario(usuario);
			c.setCantidad(cantidad);
			lista.add(c);
			servCesta.insertarCesta(c);

		}

		session.setAttribute("lista", lista);
		model.addAttribute("success", "Producto añadido a la cesta con éxito.");

		return "redirect:/CestaCompra";
	}

}
