package com.oscar.vivero;

import java.util.Map;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.servicios.ServiciosCestaCompra;
import com.oscar.vivero.servicios.ServiciosPedido;
import com.oscar.vivero.servicios.ServiciosPlanta;

import jakarta.transaction.Transactional;

@Controller
public class CestaCompraController {

	@Autowired
	private ServiciosPedido servPedido;

	@Autowired
	private ServiciosPlanta servPlanta;

	@Autowired
	private ServiciosCestaCompra servCesta;

	@GetMapping("/CestaCompra")
	public String mostrarCesta(Model model) {

		Map<String, Integer> productosEnCesta =  servCesta.obtenerProductosCesta(null);

		if (productosEnCesta == null || productosEnCesta.isEmpty()) {
			model.addAttribute("mensaje", "Tu cesta está vacía.");
		} else {
			model.addAttribute("productosEnCesta", productosEnCesta);
		}

		return "CestaCompra";
	}

	
	@PostMapping("/vaciarCesta")
	public String vaciarCesta() {
		servCesta.vaciarCesta();
		return "redirect:/CestaCompra";
	}

	@Transactional
	public CestaCompra obtenerCesta(Long id) {
		CestaCompra cesta = new CestaCompra();
		if (cesta != null) {
			Hibernate.initialize(cesta.getProductos());
		}
		return cesta;
	}

}