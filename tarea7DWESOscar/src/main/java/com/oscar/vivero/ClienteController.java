package com.oscar.vivero;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Cliente;
import com.oscar.vivero.servicios.ServiciosCliente;

public class ClienteController {
	
	@Autowired
	ServiciosCliente servcliente;

	@PostMapping("/CamposCliente")
	public String RegistrarCliente(@ModelAttribute Cliente RegistroCliente, Model model) {

		Cliente c = new Cliente();

		String nombre = RegistroCliente.getNombre();
		Date fecahanac = RegistroCliente.getFechanac();
		String nif = RegistroCliente.getNif();
		String direccion = RegistroCliente.getDireccion();
		String email = RegistroCliente.getEmail();
		String telefono = RegistroCliente.getTelefono();

		c.setNombre(nombre);
		c.setFechanac(fecahanac);
		c.setNif(nif);
		c.setDireccion(direccion);
		c.setEmail(email);
		c.setTelefono(telefono);

		boolean camposValidos = servcliente.validarCliente(nombre, email);

		if (!camposValidos) {
			model.addAttribute("error", " campos del Cliente Invalidos.");
			return "RegistroCliente";
		}

		servcliente.insertarCliente(c);
		
		return "/RegistroCliente";
	}

	@GetMapping("/RegistroCliente")
	public String mostrarRegistroCliente(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "RegistroCliente";
	}

}
