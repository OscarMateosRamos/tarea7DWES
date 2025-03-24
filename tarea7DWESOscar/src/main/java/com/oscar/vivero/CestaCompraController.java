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
import com.oscar.vivero.modelo.Pedido;
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

		Long usuarioId = obtenerUsuarioId();

		Map<String, Integer> productosEnCesta = servCesta.obtenerProductosCesta(usuarioId);

		if (productosEnCesta == null || productosEnCesta.isEmpty()) {
			model.addAttribute("mensaje", "Tu cesta está vacía.");
		} else {
			model.addAttribute("productosEnCesta", productosEnCesta);
		}

		return "CestaCompra";
	}

	public Long obtenerUsuarioId() {

		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {

			Object principal = authentication.getPrincipal();

			if (principal instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) principal;

				if (userDetails instanceof Persona) {
					Persona persona = (Persona) userDetails;

					Long userId = persona.getId();
					return userId;
				}
			}
		}

		return null;
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
